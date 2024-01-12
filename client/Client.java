package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import caches.ByteManager;
import caches.Compte;
import caches.MessageC;
import caches.Notification;
import caches.Publication;
import enums.ErreurSocket;
import javafx.scene.image.Image;
import requete.*;

public class Client extends Thread {

    private Main main;
    private String pseudo;
    private Compte compte;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client(Main main, String pseudo, String ip) {
        this.main = main;
        this.pseudo = pseudo;

        try {

            this.socket = new Socket(ip, 8080);
            this.in = new DataInputStream(this.socket.getInputStream());
            this.out = new DataOutputStream(this.socket.getOutputStream());
            super.start();
            this.main.sauvegarderIdentifiant(ip, this.pseudo);
            this.out.writeUTF(this.pseudo);
            this.out.flush();

        } catch (IOException e) {

            if (e.getMessage().equals(ErreurSocket.ERREUR_IP.getErreur())) {
                this.main.erreurDansAdresse();
            }

        }
    }

    @Override
    public void run() {

        try {

            while (true) {

                if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown() ||
                        !this.main.isConnected()) {
                    this.fermer();
                    break;
                }

                if (in.available() > 0) {

                    String demande = this.in.readUTF();

                    if (demande.equals(Requete.CREER_COMPTE.getRequete())) {

                        this.main.demanderCreationDeCompte();

                    } else if (demande.equals(Requete.COMPTE_CREE.getRequete())) {

                        this.demanderPublications();
                        this.demanderComptes();
                        this.demanderNotifications();
                        this.main.mettrePage();

                    } else if (demande.equals(Requete.AVOIR_PUBLICATIONS.getRequete())) {

                        int arraySize = this.in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        List<Publication> retrievedPublicationList = ByteManager.convertBytesToList(receivedBytes,
                                Publication.class);

                        for (Publication publication : retrievedPublicationList)
                            this.main.afficherPublication(publication, false);

                    } else if (demande.equals(Requete.LIKER_PUBLICATION.getRequete())) {

                        int idPublication = this.in.readInt();
                        int like = this.in.readInt();
                        boolean isMe = this.in.readBoolean();
                        this.main.ajouterLike(idPublication, like, isMe);

                    } else if (demande.equals(Requete.DISLIKER_PUBLICATION.getRequete())) {

                        int idPublication = this.in.readInt();
                        int like = this.in.readInt();
                        boolean isMe = this.in.readBoolean();
                        this.main.removeLike(idPublication, like, isMe);

                    } else if (demande.equals(Requete.AVOIR_FOLLOW.getRequete())) {

                        int arraySize = this.in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        this.compte = ByteManager.fromBytes(receivedBytes, Compte.class);

                        this.main.ajouterMonCompte();

                        arraySize = this.in.readInt();
                        receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        Map<Compte, MessageC> comptes = ByteManager.convertBytesToMap(receivedBytes, Compte.class,
                                MessageC.class);
                        for (Map.Entry<Compte, MessageC> compte : comptes.entrySet())
                            this.main.afficherCompte(compte.getKey(), compte.getValue());

                    } else if (demande.equals(Requete.PUBLIER_PUBLICATION.getRequete())) {

                        int arraySize = this.in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        Publication publication = ByteManager.fromBytes(receivedBytes, Publication.class);

                        this.main.afficherPublication(publication, true);

                    } else if (demande.equals(Requete.VOIR_MESSAGES.getRequete())) {

                        int arraySize = this.in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        List<MessageC> retrievedMessageCList = ByteManager.convertBytesToList(receivedBytes,
                                MessageC.class);

                        for (MessageC msg : retrievedMessageCList)
                            this.main.afficherMessage(msg);

                    } else if (demande.equals(Requete.SUPPRIMER_PUBLICATION.getRequete())) {

                        int idPublication = this.in.readInt();

                        this.main.removePublication(idPublication);

                    } else if (demande.equals(Requete.VOIR_NOTIFICATIONS.getRequete())) {

                        int arraySize = this.in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        List<Notification> retrievedNotificationList = ByteManager.convertBytesToList(receivedBytes,
                                Notification.class);

                        for (Notification notification : retrievedNotificationList)
                            this.main.afficherNotification(notification);

                    } else if (demande.equals(Requete.ENREGISTRER_PROFIL.getRequete())) {

                        int arraySize = this.in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        boolean isMe = this.in.readBoolean();

                        Compte compte = ByteManager.fromBytes(receivedBytes, Compte.class);
                        Image image = Main.blobToImage(compte.getImage());

                        if (isMe) {
                            this.main.modifierCompteBarre(image);
                        }

                        this.main.modifierComptePublications(compte.getPseudo(), image);

                    } else if (demande.equals(Requete.ENVOYER_MESSAGE.getRequete())) {

                        int arraySize = this.in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        this.in.readFully(receivedBytes);

                        MessageC message = ByteManager.fromBytes(receivedBytes, MessageC.class);

                        this.main.afficherMessage(message);
                        this.main.changerDernierMessage( (message.getPseudoExpediteur().equals(this.pseudo) ? message.getPseudoDestinataire() : message.getPseudoExpediteur()) , message.getContent());

                    } else if (demande.equals(Requete.NOTIFICATIN_CONNEXION.getRequete())) {

                        this.main.afficherNotificationConnexion(this.in.readUTF());

                    } else if (demande.equals(Requete.SUPPRIMER_MESSAGE.getRequete())) {

                        int idMessage = this.in.readInt();

                        this.main.removeMessage(idMessage);

                    }

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void creerCompte() {
        try {
            this.out.writeUTF(Requete.CREER_COMPTE.getRequete());
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fermer() {
        try {
            this.out.writeUTF(Requete.FERMER.getRequete());
            this.out.flush();
            this.out.close();
            this.in.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void demanderPublications() {
        try {
            this.out.writeUTF(Requete.AVOIR_PUBLICATIONS.getRequete());
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void demanderComptes() {
        try {
            this.out.writeUTF(Requete.AVOIR_FOLLOW.getRequete());
            this.out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void likePublication(int id) {
        try {
            this.out.writeUTF(Requete.LIKER_PUBLICATION.getRequete());
            this.out.writeInt(id);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unlikePublication(int id) {
        try {
            this.out.writeUTF(Requete.DISLIKER_PUBLICATION.getRequete());
            this.out.writeInt(id);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publierPublication(String text, byte[] vocal) {
        try {
            this.out.writeUTF(Requete.PUBLIER_PUBLICATION.getRequete());
            this.out.writeUTF(text);
            this.out.writeBoolean(vocal != null);
            if (vocal != null) {
                this.out.writeInt(vocal.length);
                this.out.write(vocal);
            }
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMessages(String pseudo) {
        try {
            this.out.writeUTF(Requete.VOIR_MESSAGES.getRequete());
            this.out.writeUTF(pseudo);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supprimerPublication(int idPublication) {
        try {
            this.out.writeUTF(Requete.SUPPRIMER_PUBLICATION.getRequete());
            this.out.writeInt(idPublication);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demanderNotifications() {
        try {
            this.out.writeUTF(Requete.VOIR_NOTIFICATIONS.getRequete());
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enregistrerProfil() {
        try {
            this.out.writeUTF(Requete.ENREGISTRER_PROFIL.getRequete());
            byte[] bytes = ByteManager.getBytes(this.compte);
            this.out.writeInt(bytes.length);
            this.out.write(bytes);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void envoyerMessage(MessageC messageC) {
        try {
            this.out.writeUTF(Requete.ENVOYER_MESSAGE.getRequete());
            byte[] bytes = ByteManager.getBytes(messageC);
            this.out.writeInt(bytes.length);
            this.out.write(bytes);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supprimerMessage(int idMessage){
        try {
            this.out.writeUTF(Requete.SUPPRIMER_MESSAGE.getRequete());
            this.out.writeInt(idMessage);
            this.out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public Compte getCompte() {
        return this.compte;
    }

}

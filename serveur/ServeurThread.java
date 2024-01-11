package serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import caches.ByteManager;
import caches.Compte;
import caches.Publication;
import enums.Color;
import requete.Requete;

public class ServeurThread extends Thread {

    private Serveur serveur;
    private Compte compte;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ServeurThread(Serveur serveur, String pseudo, Socket socket) throws IOException {
        this.serveur = serveur;
        this.socket = socket;
        this.compte = new Compte(pseudo);
        this.in = new DataInputStream(this.socket.getInputStream());
        this.out = new DataOutputStream(this.socket.getOutputStream());
        if (!this.serveur.getConnexionMySQL().isPseudoAlreadyExists(pseudo)) {
            this.out.writeUTF(Requete.CREER_COMPTE.getRequete());
            this.out.flush();
        } else {
            this.compte = this.serveur.getConnexionMySQL().getCompteByPseudo(this.compte.getPseudo());
            this.out.writeUTF(Requete.COMPTE_CREE.getRequete());
            this.out.flush();
        }
    }

    @Override
    public void run() {
        try {

            while (true) {

                if (this.socket.isClosed())
                    break;

                if (this.in.available() > 0) {

                    String message = this.in.readUTF();

                    if (message.equals(Requete.AVOIR_PUBLICATIONS.getRequete())) {

                        this.sendAllPublication();

                    } else if (message.equals(Requete.CREER_COMPTE.getRequete())) {

                        this.creerCompte();

                    } else if (message.equals(Requete.FERMER.getRequete())) {

                        this.fermer();

                    } else if (message.equals(Requete.LIKER_PUBLICATION.getRequete())) {

                        this.likerPublication(this.in.readInt());

                    } else if (message.equals(Requete.DISLIKER_PUBLICATION.getRequete())) {

                        this.dislikerPublication(this.in.readInt());

                    } else if (message.equals(Requete.PUBLIER_PUBLICATION.getRequete())) {

                        String text = this.in.readUTF();
                        boolean hasVocal = this.in.readBoolean();

                        byte[] receivedBytes = null;

                        if (hasVocal) {
                            int arraySize = this.in.readInt();
                            receivedBytes = new byte[arraySize];
                            this.in.readFully(receivedBytes);
                        }

                        this.publierPublication(text, receivedBytes);

                    } else if (message.equals(Requete.AVOIR_FOLLOW.getRequete())) {

                        this.avoirCompte();

                    } else if (message.equals(Requete.VOIR_MESSAGES.getRequete())) {

                        this.voirMessages(this.in.readUTF());

                    } else if (message.equals(Requete.SUPPRIMER_PUBLICATION.getRequete())) {

                        this.supprimerPublication(this.in.readInt());

                    } else if (message.equals(Requete.VOIR_NOTIFICATIONS.getRequete())) {

                        this.voirNotifications();

                    }

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

        System.out.println(
                Color.BLUE.getCode() + this.compte.getPseudo() + Color.PURPLE.getCode() + " s'est déconnecté au serveur"
                        + Color.RED.getCode() + "." + Color.RESET.getCode());
    }

    public Compte getCompte() {
        return this.compte;
    }

    private void sendAllPublication() throws IOException {
        this.out.writeUTF(Requete.AVOIR_PUBLICATIONS.getRequete());
        byte[] listBytes = ByteManager.convertListToBytes(
                this.serveur.getConnexionMySQL().getPublicationsForUserAndFollowers(this.compte.getPseudo(), 10));
        this.out.writeInt(listBytes.length);
        this.out.write(listBytes);
        this.out.flush();
    }

    public void creerCompte() throws IOException {
        this.serveur.getConnexionMySQL().saveNewCompte(this.compte.getPseudo());
        this.compte = this.serveur.getConnexionMySQL().getCompteByPseudo(this.compte.getPseudo());
        this.out.writeUTF(Requete.COMPTE_CREE.getRequete());
        this.out.flush();
    }

    private void fermer() throws IOException {
        this.serveur.removeClient(this);
        this.out.close();
        this.in.close();
        this.socket.close();
    }

    private void likerPublication(int id) throws IOException {
        this.serveur.getConnexionMySQL().likePublication(this.compte.getPseudo(), id);
        int nbLike = this.serveur.getConnexionMySQL().nbLikePublications(id);
        for (ServeurThread client : this.serveur.getClients()) {
            if (this.serveur.getConnexionMySQL().isOwnerPublication(client.getCompte().getPseudo(), id) ||
                    this.serveur.getConnexionMySQL().hasFollowToSenderPublication(client.getCompte().getPseudo(), id)) {
                client.getOut().writeUTF(Requete.LIKER_PUBLICATION.getRequete());
                client.getOut().writeInt(id);
                client.getOut().writeInt(nbLike);
                client.getOut().writeBoolean(client.getCompte().getPseudo().equals(this.compte.getPseudo()));
                client.getOut().flush();
            }
        }
    }

    private void dislikerPublication(int id) throws IOException {
        this.serveur.getConnexionMySQL().unlikePublication(this.compte.getPseudo(), id);
        int nbLike = this.serveur.getConnexionMySQL().nbLikePublications(id);
        for (ServeurThread client : this.serveur.getClients()) {
            if (this.serveur.getConnexionMySQL().isOwnerPublication(client.getCompte().getPseudo(), id) ||
                    this.serveur.getConnexionMySQL().hasFollowToSenderPublication(client.getCompte().getPseudo(), id)) {
                client.getOut().writeUTF(Requete.DISLIKER_PUBLICATION.getRequete());
                client.getOut().writeInt(id);
                client.getOut().writeInt(nbLike);
                client.getOut().writeBoolean(client.getCompte().getPseudo().equals(this.compte.getPseudo()));
                client.getOut().flush();
            }
        }
    }

    public void publierPublication(String text, byte[] vocal) throws IOException {
        Publication publication = this.serveur.getConnexionMySQL().publierPublication(this.compte.getPseudo(), text,
                vocal);
        byte[] bytes = ByteManager.getBytes(publication);
        for (ServeurThread client : this.serveur.getClients()) {
            if (client.getCompte().getPseudo().equals(this.compte.getPseudo()) ||
                    this.serveur.getConnexionMySQL().hasFollowToSenderPublication(client.getCompte().getPseudo(),
                            publication.getIdPublication())) {
                client.getOut().writeUTF(Requete.PUBLIER_PUBLICATION.getRequete());
                client.getOut().writeInt(bytes.length);
                client.getOut().write(bytes);
                client.getOut().flush();
            }
        }
    }

    public void avoirCompte() throws IOException {
        this.out.writeUTF(Requete.AVOIR_FOLLOW.getRequete());
        byte[] listBytes = ByteManager.getBytes(this.compte);
        this.out.writeInt(listBytes.length);
        this.out.write(listBytes);
        listBytes = ByteManager.convertListToBytes(
                this.serveur.getConnexionMySQL().getFollow(this.compte.getPseudo()));
        this.out.writeInt(listBytes.length);
        this.out.write(listBytes);
        this.out.flush();
    }

    public void voirMessages(String pseudo) throws IOException {
        this.out.writeUTF(Requete.VOIR_MESSAGES.getRequete());
        byte[] listBytes = ByteManager.convertListToBytes(
                this.serveur.getConnexionMySQL().getMessages(this.compte.getPseudo(), pseudo));
        this.out.writeInt(listBytes.length);
        this.out.write(listBytes);
        this.out.flush();
    }

    public void supprimerPublication(int idPublication) throws IOException {
        for (ServeurThread client : this.serveur.getClients()) {
            if (this.serveur.getConnexionMySQL().isOwnerPublication(client.getCompte().getPseudo(), idPublication)) {
                client.getOut().writeUTF(Requete.SUPPRIMER_PUBLICATION.getRequete());
                client.getOut().writeInt(idPublication);
                client.getOut().flush();
            }
        }
        this.serveur.getConnexionMySQL().supprimerPublication(idPublication);
    }

    public void voirNotifications() throws IOException {
        this.out.writeUTF(Requete.VOIR_NOTIFICATIONS.getRequete());
        byte[] listBytes = ByteManager.convertListToBytes(
                this.serveur.getConnexionMySQL().getNotifications(this.compte.getPseudo()));
        this.out.writeInt(listBytes.length);
        this.out.write(listBytes);
        this.out.flush();
    }

    public synchronized DataOutputStream getOut() {
        return this.out;
    }

}

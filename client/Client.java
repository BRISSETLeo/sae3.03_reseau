package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import caches.ByteManager;
import caches.Publication;
import enums.ErreurSocket;
import requete.*;

public class Client extends Thread {

    private Main main;
    private String pseudo;
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

                if (this.socket.isClosed())
                    break;

                if (in.available() > 0) {

                    String demande = in.readUTF();

                    if (demande.equals(Requete.CREER_COMPTE.getRequete())) {

                        this.main.demanderCreationDeCompte();

                    } else if (demande.equals(Requete.COMPTE_CREE.getRequete())) {

                        this.demanderPublications();
                        this.main.mettreLaPageAccueil();

                    } else if (demande.equalsIgnoreCase(Requete.AVOIR_PUBLICATIONS.getRequete())) {

                        int arraySize = in.readInt();
                        byte[] receivedBytes = new byte[arraySize];
                        in.readFully(receivedBytes);

                        List<Publication> retrievedPublicationList = ByteManager.convertBytesToList(receivedBytes,
                                Publication.class);

                        for (Publication publication : retrievedPublicationList)
                            this.main.afficherPublication(publication);

                    } else if (demande.equalsIgnoreCase(Requete.LIKER_PUBLICATION.getRequete())) {

                        int idPublication = in.readInt();
                        int like = in.readInt();
                        boolean isMe = in.readBoolean();
                        this.main.ajouterLike(idPublication, like, isMe);

                    } else if (demande.equalsIgnoreCase(Requete.DISLIKER_PUBLICATION.getRequete())) {

                        int idPublication = in.readInt();
                        int like = in.readInt();
                        boolean isMe = in.readBoolean();
                        this.main.removeLike(idPublication, like, isMe);

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

}

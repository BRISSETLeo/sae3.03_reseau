package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import caches.Publications;
import sql.Requete;

public class ServerThread extends Thread {

    private String pseudo;
    private Socket socket;

    public ServerThread(String pseudo, Socket socket) throws IOException {
        this.pseudo = pseudo;
        this.socket = socket;
    }

    @Override
    public void run() {

        while (true) {
            try {
                DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
                if (dataInputStream.available() > 0) {
                    String message = dataInputStream.readUTF();
                    System.out.println("\u001B[33m" + "Message reçu de " + "\u001B[32m" + pseudo
                            + "\u001B[31m : \u001B[36m" + message + "\u001B[37m");
                    if (message.equalsIgnoreCase("Recherche des publications")) {
                        for (Publications publications : Requete
                                .getFollowersPublications(Server.getConnexionMySQL(), pseudo)) {
                            this.envoyerPublicationAuClient(this.socket, publications);
                        }
                    } else if (message.equalsIgnoreCase("Ajout de like")) {
                        String idPublication = dataInputStream.readUTF();
                        Publications publication = Requete.newLikes(Server.getConnexionMySQL(), idPublication);
                        for (Socket socket : Server.getSockets().values()) {
                            envoyerMisAJourLike(socket, publication);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void envoyerPublicationAuClient(Socket socket, Publications publication) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("Recherche des publications");
            dataOutputStream.writeUTF(publication.getIdPublication());
            dataOutputStream.writeUTF(publication.getNomUser());
            dataOutputStream.writeUTF(publication.getContenue());
            dataOutputStream.writeUTF(publication.getDate());
            dataOutputStream.writeInt(publication.getLikes());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void envoyerMisAJourLike(Socket socket, Publications publication) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("Demande de mis à jour des likes");
            dataOutputStream.writeUTF(publication.getIdPublication());
            dataOutputStream.writeInt(publication.getLikes());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

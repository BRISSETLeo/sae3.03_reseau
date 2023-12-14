package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import caches.Publications;
import sql.ConnexionMySQL;
import sql.Requete;

public class ServerThread extends Thread {

    private String pseudo;
    private Socket socket;
    private ConnexionMySQL connexionMySQL;

    public ServerThread(String pseudo, Socket socket, ConnexionMySQL connexionMySQL) throws IOException {
        this.pseudo = pseudo;
        this.socket = socket;
        this.connexionMySQL = connexionMySQL;
        Server.putSocket(pseudo, socket);
    }

    @Override
    public void run() {

        while (true) {
            try {
                DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
                if (dataInputStream.available() > 0) {
                    String message = dataInputStream.readUTF();
                    System.out.println(Server.ANSI_PURPLE + "Message reçu de " + Server.ANSI_BLUE + pseudo
                            + Server.ANSI_RED + " : " + Server.ANSI_BLACK + message);
                    if (message.equalsIgnoreCase("Recherche des publications")) {
                        for (Publications publications : Requete
                                .getFollowersPublications(this.connexionMySQL, pseudo)) {
                            this.envoyerPublicationAuClient(this.socket, publications);
                        }
                    } else if (message.equalsIgnoreCase("Ajout de like")) {
                        String idPublication = dataInputStream.readUTF();
                        Publications publication = Requete.newLikes(this.connexionMySQL, idPublication);
                        for (Socket socket : Server.getSockets().values()) {
                            envoyerMisAJourLike(socket, publication);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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

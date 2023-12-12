package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map.Entry;

import caches.Publications;
import sql.Requete;

public class ServerThread extends Thread {

    public ServerThread() throws IOException {
    }

    @Override
    public void run() {

        Socket misAJour = null;
        Publications modif = null;

        while (true) {
            for (Entry<String, Socket> s : Server.getSockets().entrySet()) {
                String pseudo = s.getKey();
                Socket socket = s.getValue();
                try {
                    if (misAJour != null) {
                        envoyerMisAJourLike(socket, modif);
                        if (misAJour == socket) {
                            misAJour = null;
                            modif = null;
                        }
                    }
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    if (dataInputStream.available() > 0) {
                        String message = dataInputStream.readUTF();
                        System.out.println(
                                "\u001B[33m" + "Message reçu de " + "\u001B[32m" + pseudo + "\u001B[31m : \u001B[36m"
                                        + message
                                        + "\u001B[37m");
                        if (message.equalsIgnoreCase("Recherche des publications")) {
                            for (Publications publications : Requete
                                    .getFollowersPublications(Server.getConnexionMySQL(), pseudo)) {
                                this.envoyerPublicationAuClient(socket, publications);
                            }
                        } else if (message.equalsIgnoreCase("Ajout de like")) {
                            misAJour = socket;
                            String idPublication = dataInputStream.readUTF();
                            modif = Requete.newLikes(Server.getConnexionMySQL(), idPublication);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

import graphique.Main;
import javafx.application.Platform;

public class Client extends Thread {

    private Socket socket;
    private final String ip;
    private final String pseudo;

    public Client(String ip, String pseudo) {

        this.socket = null;
        this.ip = ip;
        this.pseudo = pseudo;

    }

    @Override
    public void run() {

        try {
            this.socket = new Socket(ip, 3030);
            DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            dataOutputStream.writeUTF(pseudo);
            dataOutputStream.flush();
            while (!Main.getInstance().windowIsClosed()) {
                DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
                if (dataInputStream.available() > 0) {
                    String demande = dataInputStream.readUTF();
                    if (demande.equalsIgnoreCase("Recherche des publications")) {
                        String idPublication = dataInputStream.readUTF();
                        String nomUser = dataInputStream.readUTF();
                        String contenue = dataInputStream.readUTF();
                        String date = dataInputStream.readUTF();
                        int likes = dataInputStream.readInt();
                        Platform.runLater(() -> Main.getInstance().nouvellePublication(
                            Arrays.asList(idPublication, nomUser, contenue, date, likes + "")));
                    } else if (demande.equalsIgnoreCase("Demande de mis à jour des likes")) {
                        String idPublication = dataInputStream.readUTF();
                        int likes = dataInputStream.readInt();
                        Platform.runLater(() -> Main.getInstance().updateLike(idPublication, likes));
                    }
                }
            }
            this.socket.close();
        } catch (IOException ignored) {}
    }

    public boolean isConntected(){
        return this.socket != null && this.socket.isConnected();
    }

    public void demanderPublication() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            dataOutputStream.writeUTF("Recherche des publications");
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajouterLike(String idPublication) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            dataOutputStream.writeUTF("Ajout de like");
            dataOutputStream.writeUTF(idPublication);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

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

        while (this.socket == null) {
            try {
                this.socket = new Socket(ip, 3030);
                DataOutputStream dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
                dataOutputStream.writeUTF(pseudo);
                dataOutputStream.flush();
                while (!Main.getInstance().windowIsClosed()) {
                    DataInputStream dataInputStream = new DataInputStream(this.socket.getInputStream());
                    String demande = dataInputStream.readUTF();
                    if (demande.equalsIgnoreCase("Recherche des publications")) {
                        String idPublication = dataInputStream.readUTF();
                        String nomUser = dataInputStream.readUTF();
                        String contenue = dataInputStream.readUTF();
                        String date = dataInputStream.readUTF();
                        int likes = dataInputStream.readInt();
                        Platform.runLater(() -> Main.getInstance().nouvellePublication(
                                Arrays.asList(idPublication, nomUser, contenue, date, likes + "")));
                    }
                }
                this.socket.close();
            } catch (IOException ignored) {
                System.out.println("Veuillez vérifier que l'ip de connexion est valide.");
            }
        }

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

}

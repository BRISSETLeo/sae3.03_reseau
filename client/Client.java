package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.scene.control.TextField;

public class Client extends Thread {

    private Socket socket;
    private final String ip;
    private final String pseudo;
    private DataOutputStream dataOutput;
    private String message;

    public Client(String ip, String pseudo) {

        this.socket = null;
        this.ip = ip;
        this.pseudo = pseudo;
        this.message = null;

    }

    public void setMessage(TextField message) {
        this.message = message.getText();
        message.setText("");
    }

    @Override
    public void run() {

        while (this.socket == null) {
            try {
                this.socket = new Socket(ip, 3030);
                this.dataOutput = new DataOutputStream(this.socket.getOutputStream());
                this.dataOutput.writeUTF(pseudo);
                this.dataOutput.flush();
                while (true) {
                    if (this.message != null) {
                        System.out.println("Message envoyé : " + this.message);
                        this.dataOutput.writeUTF(this.message);
                        this.dataOutput.flush();
                        if (this.message.equals("QUITTER"))
                            break;

                        this.message = null;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.dataOutput.close();
                this.socket.close();
            } catch (IOException ignored) {
                System.out.println("Veuillez vérifier que l'ip de connexion est valide.");
            }
        }

    }

}

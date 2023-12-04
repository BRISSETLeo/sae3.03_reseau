package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {
    
    private Socket socket;
    private DataInputStream dataInputStream;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        System.out.println("Connexion réussie !");
    }

    @Override
    public void run() {
        
        while (true) {
            try{
                String message = this.dataInputStream.readUTF();
                if (message != null && message.equals("QUITTER")) {
                    break;
                }
                System.out.println("Message reçu : " + message);
            }catch(Exception ignored){
                ignored.printStackTrace();
                break;
            }
        }
        try {
            this.dataInputStream.close();
            this.socket.close();
            System.out.println("Le client a fermé sont écran.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}

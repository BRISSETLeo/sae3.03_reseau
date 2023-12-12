package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerThread extends Thread {

    public ServerThread() throws IOException {
        System.out.println("Connexion réussie !");
    }

    @Override
    public void run() {
        
        while (true) {
            try{
                for (Socket socket : Server.getSockets()) {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    if(dataInputStream.available() > 0){
                        String message = dataInputStream.readUTF();
                        if (message != null && message.equals("QUITTER")) {
                            break;
                        }
                        System.out.println("Message reçu : " + message);
                    }
                }
            }catch(Exception ignored){
                ignored.printStackTrace();
                break;
            }
        }
        
    }

}

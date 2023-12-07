package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        
        
        try {
            try (ServerSocket serverSocket = new ServerSocket(3030)) {
                while(true){
                    Socket socket = serverSocket.accept();
                    new ServerThread(socket).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
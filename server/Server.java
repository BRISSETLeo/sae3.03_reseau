package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import sql.ConnexionMySQL;

public class Server {

    private static ConnexionMySQL connexion;

    public static void main(String[] args) {

        connexion = new ConnexionMySQL();
        
        try {
            try (ServerSocket serverSocket = new ServerSocket(3030)) {
                System.out.println("Server Socket en attente...");
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
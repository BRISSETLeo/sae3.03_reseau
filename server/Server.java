package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import sql.ConnexionMySQL;

public class Server {

    private static ConnexionMySQL connexion;
    private static List<Socket> sockets;

    public static void main(String[] args) {

        connexion = new ConnexionMySQL();
        sockets = new ArrayList<Socket>();

        try {
            try (ServerSocket serverSocket = new ServerSocket(3030)) {
                System.out.println("Server Socket en attente...");
                new ServerThread().start();
                while (true) {
                    Socket socket = serverSocket.accept();
                    sockets.add(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized static List<Socket> getSockets() {
        return sockets;
    }

}
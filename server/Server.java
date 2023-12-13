package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import sql.ConnexionMySQL;
import sql.Requete;

public class Server {

    private static ConnexionMySQL connexionMySQL;
    private static Map<String, Socket> sockets;

    public static void main(String[] args) {

        Server.connexionMySQL = new ConnexionMySQL();
        Server.sockets = new HashMap<String, Socket>();

        try {
            try (ServerSocket serverSocket = new ServerSocket(3030)) {
                System.out.println("Server Socket en attente...");
                while (true) {
                    Socket socket = serverSocket.accept();
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String pseudo = dataInputStream.readUTF();
                    Server.sockets.put(pseudo, socket);
                    Requete.addUser(connexionMySQL, pseudo);
                    System.out.println(
                            "\u001B[32m" + pseudo + "\u001B[33m s'est connecté au serveur\u001B[31m.\u001B[37m");
                    new ServerThread(pseudo, socket).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized static Map<String, Socket> getSockets() {
        return Server.sockets;
    }

    public static ConnexionMySQL getConnexionMySQL() {
        return Server.connexionMySQL;
    }

}
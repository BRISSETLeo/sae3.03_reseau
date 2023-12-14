package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import sql.ConnexionMySQL;
import sql.Requete;

public class Server {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static Map<String, Socket> synchronizedMap = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {

        ConnexionMySQL connexionMySQL = new ConnexionMySQL();

        try {
            try (ServerSocket serverSocket = new ServerSocket(10000, 50, InetAddress.getByName("185.31.40.87"))) {
                System.out.println("Server Socket en attente...");
                while (true) {
                    Socket socket = serverSocket.accept();
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String pseudo = dataInputStream.readUTF();
                    Requete.addUser(connexionMySQL, pseudo);
                    System.out.println(
                            Server.ANSI_BLUE + pseudo + Server.ANSI_PURPLE + " s'est connecté au serveur"
                                    + Server.ANSI_RED + "." + Server.ANSI_BLACK);
                    new ServerThread(pseudo, socket, connexionMySQL).start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized static void putSocket(String pseudo, Socket socket) {
        Server.synchronizedMap.put(pseudo, socket);
    }

    public synchronized static Map<String, Socket> getSockets() {
        return Server.synchronizedMap;
    }

}
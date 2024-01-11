package serveur;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import enums.Color;
import serveur.sql.ConnexionMySQL;

public class Serveur {

    private List<ServeurThread> clients;
    private ConnexionMySQL connexionMySQL;

    public Serveur() {
        this.clients = new ArrayList<>();
        this.connexionMySQL = new ConnexionMySQL();

        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            Socket socket;
            DataInputStream in;

            String pseudo;
            ServeurThread serveurThread;

            while (true) {

                socket = serverSocket.accept();
                in = new DataInputStream(socket.getInputStream());
                pseudo = in.readUTF();
                System.out.println(
                        Color.BLUE.getCode() + pseudo + Color.PURPLE.getCode() + " s'est connecté au serveur"
                                + Color.RED.getCode() + "." + Color.RESET.getCode());
                serveurThread = new ServeurThread(this, pseudo, socket);
                this.addClient(serveurThread);
                serveurThread.start();

            }

        } catch (

        Exception e) {

            e.printStackTrace();

        }
    }

    public List<ServeurThread> getClients() {
        return this.clients;
    }

    public synchronized boolean removeClient(ServeurThread serveurThread) {
        return this.clients.remove(serveurThread);
    }

    public synchronized boolean addClient(ServeurThread serveurThread) {
        return this.clients.add(serveurThread);
    }

    public synchronized ConnexionMySQL getConnexionMySQL() {
        return this.connexionMySQL;
    }

    public static void main(String[] args) {

        new Serveur();

    }

}
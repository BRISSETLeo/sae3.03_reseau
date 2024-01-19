package serveur;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import serveur.sql.ConnexionMySQL;

public class Serveur {

    private Set<ServeurThread> clients;
    private ConnexionMySQL mysql;

    public Serveur() {

        this.clients = new HashSet<>();
        this.mysql = new ConnexionMySQL();

        try (ServerSocket server = new ServerSocket(25565)) {

            System.out.println("Le serveur est lancé.");

            new WriteThread(this).start();

            while (true) {

                ServeurThread client = new ServeurThread(this, server.accept());

                if (client.isConnected()) {
                    this.addClient(client);
                    client.start();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void addClient(ServeurThread client) {
        this.clients.add(client);
    }

    public synchronized void removeClient(ServeurThread client) {
        this.clients.remove(client);
    }

    public synchronized Set<ServeurThread> getClients() {
        return this.clients;
    }

    public synchronized ConnexionMySQL getSQL() {
        return this.mysql;
    }

    public static void main(String[] args) {
        new Serveur();
    }

}
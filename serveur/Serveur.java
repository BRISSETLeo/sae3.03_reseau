package serveur;

import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import serveur.sql.ConnexionMySQL;

/**
 * Classe représentant le serveur de l'application.
 */
public class Serveur {

    private Set<ServeurThread> clients;
    private ConnexionMySQL mysql;


    /**
     * Constructeur de la classe Serveur.
     */
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

    /**
     * Ajoute un client à l'ensemble des clients connectés.
     *
     * @param client Le client à ajouter.
     */    
    public synchronized void addClient(ServeurThread client) {
        this.clients.add(client);
    }

    /**
     * Supprime un client de l'ensemble des clients connectés.
     *
     * @param client Le client à supprimer.
     */
    public synchronized void removeClient(ServeurThread client) {
        this.clients.remove(client);
    }

    /**
     * Récupère l'ensemble des clients connectés au serveur.
     *
     * @return L'ensemble des clients connectés.
     */
    public synchronized Set<ServeurThread> getClients() {
        return this.clients;
    }

    /**
     * Récupère la connexion à la base de données MySQL.
     *
     * @return La connexion à la base de données MySQL.
     */
    public synchronized ConnexionMySQL getSQL() {
        return this.mysql;
    }

    /**
     * Point d'entrée de l'application Serveur.
     *
     * @param args Les arguments de la ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        new Serveur();
    }

}
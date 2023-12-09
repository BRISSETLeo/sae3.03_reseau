package sql;

import java.sql.*;

public class ConnexionMySQL {
    Connection mysql = null;
    boolean connecte = false;

    private static final String nomBD = "localhost";
    private static final String nomBase = "sysx";
    private static final String user = "root";
    private static final String password = "";

    public ConnexionMySQL() {
        String url = "jdbc:mysql://" + nomBD + ":3306/" + nomBase;

        // Connexion à la base de données
        try (Connection connexion = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connexion réussie.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    public Connection getConnexion() {
        return this.mysql;
    }

    public boolean getConnecte() {
        return this.connecte;
    }

    public Statement createStatement() {
        return null;
    }

}

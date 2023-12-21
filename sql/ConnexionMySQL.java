package sql;

import java.sql.*;

public class ConnexionMySQL {

    private static final String nomBD = "mysql-nocros.alwaysdata.net";
    private static final String nomBase = "nocros_sysx";
    private static final String user = "nocros";
    private static final String password = "53ea7CLG23z2TwkvkHGNi8u4GBd7s2X7";

    private Connection connection;

    public ConnexionMySQL() {
        String url = "jdbc:mysql://" + nomBD + ":3306/" + nomBase;

        // Connexion à la base de données
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion réussie.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

}

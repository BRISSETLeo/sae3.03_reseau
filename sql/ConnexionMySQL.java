package sql;

import java.sql.*;

public class ConnexionMySQL {
	Connection mysql=null;
    boolean connecte=false;
    
	public ConnexionMySQL() {
		String url = "jdbc:mysql://servinfo-maria:3306/DBsevellec";
        String utilisateur = "sevellec";
        String motDePasse = "sevellec";

        // Connexion à la base de données
        try (Connection connexion = DriverManager.getConnection(url, utilisateur, motDePasse)) {
            System.out.println("Connexion réussie.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
	}
    public Connection getConnexion(){
        return this.mysql;
    }
    public boolean getConnecte(){
        return this.connecte;
    }
    public Statement createStatement() {
        return null;
    }

}

package sql;


import java.sql.*;

public class Model {

    private ConnexionMySQL connexion;

    public Model() throws SQLException {
        connexion = new ConnexionMySQL("servinfo-maria", "DBsevellec", "sevellec", "sevellec");
    }

    public void executer() {
        if (connexion.getConnecte()) {
            System.out.println("connecté");
        }
    }
}
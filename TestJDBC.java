import java.sql.*;

public class TestJDBC {
    static ConnexionMySQL connexion;

    public static void main(String[] args) throws SQLException {
        connexion = new ConnexionMySQL("servinfo-maria", "DBsevellec", "sevellec", "sevellec");
        if (connexion.getConnecte()){
            System.out.println("connecté");
        }
    }
}

// javac *.java
// java -cp .:/usr/share/java/mariadb-java-client.jar TestJDBC
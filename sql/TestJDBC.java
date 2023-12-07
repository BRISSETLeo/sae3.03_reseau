package sql;
import java.sql.*;

public class TestJDBC {
    static ConnexionMySQL connexion;

    public static void main(String[] args) throws SQLException {
        connexion = new ConnexionMySQL("2001:660:6402:706:8669:93ff:fe0b:2406", "nocros_sysx", "nocros", "");
        if (connexion.getConnecte()){
            System.out.println("connecté");
        }
    }
}

// javac *.java
// java -cp .:/usr/share/java/mariadb-java-client.jar TestJDBC
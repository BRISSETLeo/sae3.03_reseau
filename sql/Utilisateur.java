package sql;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private String nom;



    public Utilisateur(String nom, List<Utilisateur> follower, List<Utilisateur> following) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }


}

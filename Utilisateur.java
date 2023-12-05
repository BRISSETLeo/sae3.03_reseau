import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    private String nom;
    private List<Utilisateur> follower;
    private List<Utilisateur> following;


    public Utilisateur(String nom, List<Utilisateur> follower, List<Utilisateur> following) {
        this.nom = nom;
        this.follower = new ArrayList<Utilisateur>();
        this.following = new ArrayList<Utilisateur>();
    }

    public String getNom() {
        return nom;
    }

    public List<Utilisateur> getFollower() {
        return follower;
    }

    public List<Utilisateur> getFollowing() {
        return following;
    }

}

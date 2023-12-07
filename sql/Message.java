package sql;
import java.sql.Date;

public class Message {
    private int id;
    private String contenu;
    private Utilisateur auteur;
    private Date date;
    private int nbLikes;

    public Message(int id, String contenu, Utilisateur auteur, Date date, int nbLikes) {
        this.id = id;
        this.contenu = contenu;
        this.auteur = auteur;
        this.date = date;
        this.nbLikes = nbLikes;
    }

    public int getId() {
        return id;
    }

    public String getContenu() {
        return contenu;
    }

    public Utilisateur getAuteur() {
        return auteur;
    }

    public Date getDate() {
        return date;
    }

    public int getNbLikes() {
        return nbLikes;
    }

}

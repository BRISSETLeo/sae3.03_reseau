package caches;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.List;

public class Publication implements Serializable {

    private final int idPublication;
    private final Compte compte;
    private final String content;
    private final Blob vocal;
    private final Timestamp date;
    private final Blob photo;
    private final int likes;
    private final boolean callerIsLiker;
    private final List<Commentaire> commentaires;

    public Publication(int idPublication, Compte compte, String content, Blob vocal, Timestamp date, Blob photo,
            int likes,
            boolean callerIsLiker, List<Commentaire> commentaires) {
        this.commentaires = commentaires;
        this.idPublication = idPublication;
        this.compte = compte;
        this.content = content;
        this.vocal = vocal;
        this.date = date;
        this.photo = photo;
        this.likes = likes;
        this.callerIsLiker = callerIsLiker;
    }

    public int getIdPublication() {
        return this.idPublication;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public String getContent() {
        return this.content;
    }

    public Timestamp getDate() {
        return this.date;
    }

    public Blob getPhoto() {
        return this.photo;
    }

    public int getLikes() {
        return this.likes;
    }

    public boolean isCallerIsLiker() {
        return this.callerIsLiker;
    }

    public Blob getVocal() {
        return this.vocal;
    }

    public List<Commentaire> getCommentaires() {
        return this.commentaires;
    }

}

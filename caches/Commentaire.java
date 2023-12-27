package caches;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

public class Commentaire implements Serializable {

    private final int idCommentaire;
    private final String pseudo;
    private final int idPublication;
    private final String content;
    private final byte vocal;
    private final Timestamp date;
    private final Blob photo;

    public Commentaire(int idCommentaire, String pseudo, int idPublication, String content, byte vocal, Timestamp date,
            Blob photo) {
        this.idCommentaire = idCommentaire;
        this.pseudo = pseudo;
        this.idPublication = idPublication;
        this.content = content;
        this.vocal = vocal;
        this.date = date;
        this.photo = photo;
    }

    public int getIdCommentaire() {
        return this.idCommentaire;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public int getIdPublication() {
        return this.idPublication;
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

    public byte getVocal() {
        return this.vocal;
    }

}

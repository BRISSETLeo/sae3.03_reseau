package caches;

import java.io.Serializable;
import java.sql.Blob;

public class Compte implements Serializable {

    private final String pseudo;
    private Blob image;

    public Compte(String pseudo, Blob image) {
        this.pseudo = pseudo;
        this.image = image;
    }

    public Compte(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public Blob getImage() {
        return this.image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

}

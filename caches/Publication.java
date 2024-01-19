package caches;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Timestamp;

import javafx.scene.image.Image;

public class Publication implements Serializable {

    private final int idPublication;
    private final Compte compte;
    private final String contenu;
    private final Timestamp datePublication;
    private final byte[] image;
    private final boolean estCeQueJaiLike;
    private final int nbLike;

    public Publication(int idPublication, Compte compte, String contenu, Timestamp datePublication, byte[] image,
            boolean estCeQueJaiLike, int nbLike) {
        this.idPublication = idPublication;
        this.compte = compte;
        this.contenu = contenu;
        this.datePublication = datePublication;
        this.image = image;
        this.estCeQueJaiLike = estCeQueJaiLike;
        this.nbLike = nbLike;
    }

    public int getIdPublication() {
        return this.idPublication;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Timestamp getDatePublication() {
        return this.datePublication;
    }

    public byte[] getImageBytes() {
        return this.image;
    }

    public Image getImage() {
        return this.image == null ? null
                : new Image(new ByteArrayInputStream(this.image));
    }

    public boolean estCeQueJaiLike() {
        return this.estCeQueJaiLike;
    }

    public int getNbLike() {
        return this.nbLike;
    }

}

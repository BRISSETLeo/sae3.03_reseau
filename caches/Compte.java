package caches;

import java.io.Serializable;
import java.sql.Blob;

public class Compte implements Serializable {

    private final String pseudo;
    private Blob image;
    private int nbPublications;
    private int nbAbonnes;
    private int nbAbonnements;

    public Compte(String pseudo, Blob image, int nbPublications, int nbAbonnes, int nbAbonnements) {
        this.pseudo = pseudo;
        this.image = image;
        this.nbPublications = nbPublications;
        this.nbAbonnes = nbAbonnes;
        this.nbAbonnements = nbAbonnements;
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

    public int getNbPublications() {
        return this.nbPublications;
    }

    public int getNbAbonnes() {
        return this.nbAbonnes;
    }

    public int getNbAbonnements() {
        return this.nbAbonnements;
    }

}

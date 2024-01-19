package caches;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import client.utilitaire.IMAGE;
import javafx.scene.image.Image;

public class Compte implements Serializable {

    private final String pseudo;
    private byte[] photo;
    private final int nbPublications;
    private final int nbAbonnes;
    private final int nbAbonnements;
    private final boolean estCeQueJeLeSuis;

    public Compte(String pseudo, byte[] photo, int nbPublications, int nbAbonnes, int nbAbonnements,
            boolean estCeQueJeLeSuis) {
        this.pseudo = pseudo;
        this.photo = photo;
        this.nbPublications = nbPublications;
        this.nbAbonnes = nbAbonnes;
        this.nbAbonnements = nbAbonnements;
        this.estCeQueJeLeSuis = estCeQueJeLeSuis;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public byte[] getPhotoBytes() {
        return this.photo;
    }

    public Image getPhoto() {
        return this.photo == null ? new Image(IMAGE.PROFIL.getChemin())
                : new Image(new ByteArrayInputStream(this.photo));
    }

    public int getNbAbonnes() {
        return this.nbAbonnes;
    }

    public int getNbAbonnements() {
        return this.nbAbonnements;
    }

    public int getNbPublications() {
        return this.nbPublications;
    }

    public boolean estCeQueJeLeSuis() {
        return this.estCeQueJeLeSuis;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

}

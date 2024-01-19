package caches;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Timestamp;

import javafx.scene.image.Image;

public class Message implements Serializable {

    private final int idMessage;
    private final Compte compte;
    private final Compte compteDestinateur;
    private final String contenu;
    private final byte[] vocal;
    private final byte[] image;
    private final Timestamp date;
    private final boolean lu;

    public Message(int idMessage, Compte compte, Compte compteDestinateur, String contenu, byte[] vocal,
            byte[] image, Timestamp date, boolean lu) {
        this.idMessage = idMessage;
        this.compte = compte;
        this.compteDestinateur = compteDestinateur;
        this.contenu = contenu;
        this.vocal = vocal;
        this.image = image;
        this.date = date;
        this.lu = lu;
    }

    public int getIdMessage() {
        return this.idMessage;
    }

    public Compte getCompte() {
        return this.compte;
    }

    public Compte getCompteDestinateur() {
        return this.compteDestinateur;
    }

    public String getContenu() {
        return this.contenu;
    }

    public byte[] getVocal() {
        return this.vocal;
    }

    public Image getImage() {
        return this.image == null || this.image.length == 0 ? null
                : new Image(new ByteArrayInputStream(this.image));
    }

    public Timestamp getDate() {
        return this.date;
    }

    public boolean estLu() {
        return this.lu;
    }

}

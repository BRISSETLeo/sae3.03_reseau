package caches;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

public class MessageC implements Serializable {

    private int idMessage;
    private String message;
    private Timestamp date;
    private String pseudoExpediteur;
    private String pseudoDestinataire;
    private String content;
    private Blob vocal;
    private Blob photo;

    public MessageC(int idMessage, String pseudoExpediteur, String pseudoDestinataire, String content, Blob vocal,
            Timestamp date, Blob photo) {
        this.idMessage = idMessage;
        this.pseudoExpediteur = pseudoExpediteur;
        this.pseudoDestinataire = pseudoDestinataire;
        this.content = content;
        this.vocal = vocal;
        this.date = date;
        this.photo = photo;
    }

    public String getMessage() {
        return this.message;
    }

    public Timestamp getDate() {
        return this.date;
    }

    public String getPseudoExpediteur() {
        return this.pseudoExpediteur;
    }

    public String getPseudoDestinataire() {
        return this.pseudoDestinataire;
    }

    public String getContent() {
        return this.content;
    }

    public Blob getVocal() {
        return this.vocal;
    }

    public Blob getPhoto() {
        return this.photo;
    }

    public int getIdMessage() {
        return this.idMessage;
    }

}

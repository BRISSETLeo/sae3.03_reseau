package caches;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {

    private int idMessage;
    private String pseudo;
    private String message;
    private Timestamp date;
    private String pseudoExpediteur;
    private String pseudoDestinataire;
    private String content;
    private byte vocal;
    private byte photo;


    public Message(int idMessage, String pseudoExpediteur, String pseudoDestinataire, String content, byte vocal, 
    Timestamp date, byte photo){
        this.idMessage = idMessage;
        this.pseudoExpediteur = pseudoExpediteur;
        this.pseudoDestinataire = pseudoDestinataire;
        this.content = content;
        this.vocal = vocal;
        this.date = date;
        this.photo = photo;
    }

    public String getPseudo() {
        return this.pseudo;
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

    public byte getVocal() {
        return this.vocal;
    }

    public byte getPhoto() {
        return this.photo;
    }

    public int getIdMessage() {
        return this.idMessage;
    }

    @Override
    public String toString() {
        return this.pseudo + " : " + this.message + " (" + this.date + ")";
    }
    
}

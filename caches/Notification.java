package caches;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notification implements Serializable {

    private int idNotification;
    private String pseudo;
    private String pseudoNotif;
    private String type;
    private int id;
    private Timestamp date;
    private boolean lu;

    public Notification(int idNotification, String pseudo, String pseudoNotif, String type, int id,
            Timestamp date, boolean lu) {
        this.idNotification = idNotification;
        this.pseudo = pseudo;
        this.pseudoNotif = pseudoNotif;
        this.type = type;
        this.id = id;
        this.date = date;
        this.lu = lu;
    }

    public int getIdNotification() {
        return this.idNotification;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getPseudoNotif() {
        return this.pseudoNotif;
    }

    public String getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public Timestamp getDate() {
        return this.date;
    }

    public boolean isLu() {
        return this.lu;
    }

}

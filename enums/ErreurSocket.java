package enums;

public enum ErreurSocket {

    ERREUR_IP("Connection refused: connect");

    private String erreur;

    ErreurSocket(String erreur) {
        this.erreur = erreur;
    }

    public String getErreur() {
        return this.erreur;
    }

}

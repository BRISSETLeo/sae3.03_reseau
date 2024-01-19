package serveur.sql;

public enum InformationsBD {

    URLBD("localhost"),
    NOMBASE("nocros_sysx4"),
    NOMUTILISATEUR("root"),
    MOTDEPASSE("");

    private final String valeur;

    InformationsBD(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return this.valeur;
    }

}

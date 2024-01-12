package serveur.sql;

public enum InformationsBD {

    //URLBD("localhost"),
    //NOMBASE("nocros_sysx2"),
    //NOMUTILISATEUR("root"),
    //MOTDEPASSE("");

    URLBD("servinfo-maria"),
    NOMBASE("DBbrisset"),
    NOMUTILISATEUR("brisset"),
    MOTDEPASSE("brisset");

    /*
     * URLBD("mysql-nocros.alwaysdata.net"),
     * NOMBASE("nocros_sysx2"),
     * NOMUTILISATEUR("nocros"),
     * MOTDEPASSE("53ea7CLG23z2TwkvkHGNi8u4GBd7s2X7");
     */

    private final String valeur;

    InformationsBD(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return this.valeur;
    }

}

package enums;

public enum CheminCSS {

    PAGE_CONNEXION("./client/css/ConnexionCSS.css"),
    ALERT("./client/css/AlertCSS.css"),
    NABIGATION("./client/css/NavigationCSS.css"),
    BARRE("./client/css/BarreCSS.css"),
    ACCUEIL("./client/css/AccueilCSS.css"),
    PUBLICATION("./client/css/PublicationCSS.css"),
    TEXTEFIELD("./client/css/TextFieldCSS.css"),
    COMPTEBOX("./client/css/CompteBoxCSS.css"),
    POPUP("./client/css/PopupCSS.css"),
    PROFIL("./client/css/ProfilCSS.css"),
    MESSAGE("./client/css/MessageCSS.css");

    private String chemin;

    CheminCSS(String chemin) {
        this.chemin = chemin;
    }

    public String getChemin() {
        return this.chemin;
    }

}

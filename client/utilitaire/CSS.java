package client.utilitaire;

public enum CSS {

    CONNEXION("./client/ressources/css/connexion.css"),
    NAVIGATION("./client/ressources/css/navigation.css"),
    MON_PROFIL("./client/ressources/css/monprofil.css"),
    RECHERCHE("./client/ressources/css/recherche.css"),
    PUBLICATION_VUE("./client/ressources/css/publicationvue.css"),
    PUBLICATION("./client/ressources/css/publication.css"),
    NEW_PUBLICATION("./client/ressources/css/newpublication.css"),
    MESSAGE("./client/ressources/css/message.css"),
    HISTORIQUE("./client/ressources/css/historique.css"),
    VOCAL_ANIMATION("./client/ressources/css/vocalanimation.css"),;

    private final String chemin;

    CSS(String chemin) {
        this.chemin = chemin;
    }

    public String getChemin() {
        return this.chemin;
    }

}

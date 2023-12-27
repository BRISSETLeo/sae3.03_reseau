package requete;

public enum Requete {

    CREER_COMPTE("CreerCompte"),
    COMPTE_CREE("CompteCree"),

    AVOIR_PUBLICATIONS("AvoirPublications"),
    LIKER_PUBLICATION("LikerPublication"),
    DISLIKER_PUBLICATION("DislikerPublication"),
    SUPPRIMER_PUBLICATION("SupprimerPublication"),
    COMMENTER_PUBLICATION("CommenterPublication"),

    AVOIR_COMPTES("AvoirComptes"),
    AVOIR_FOLLOWERS("AvoirFollowers"),
    AVOIR_FOLLOWINGS("AvoirFollowings"),

    AJOUTER_FOLLOW("AjouterFollow"),
    RETIRER_FOLLOW("RetirerFollow"),

    FERMER("Fermer");

    private final String requete;

    Requete(String requete) {
        this.requete = requete;
    }

    public String getRequete() {
        return this.requete;
    }

}

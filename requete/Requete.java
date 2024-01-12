package requete;

public enum Requete {

    CREER_COMPTE("CreerCompte"),
    COMPTE_CREE("CompteCree"),

    AVOIR_PUBLICATIONS("AvoirPublications"),
    LIKER_PUBLICATION("LikerPublication"),
    DISLIKER_PUBLICATION("DislikerPublication"),
    SUPPRIMER_PUBLICATION("SupprimerPublication"),
    COMMENTER_PUBLICATION("CommenterPublication"),

    UNFOLLOW("UnFollow"),
    FOLLOW("Follow"),

    AVOIR_COMPTES("AvoirComptes"),
    AVOIR_FOLLOWERS("AvoirFollowers"),
    AVOIR_FOLLOWINGS("AvoirFollowings"),

    AJOUTER_FOLLOW("AjouterFollow"),
    RETIRER_FOLLOW("RetirerFollow"),

    PUBLIER_PUBLICATION("PublierPublication"),

    VOIR_NOTIFICATIONS("VoirNotifications"),

    AVOIR_FOLLOW("AvoirFollow"),

    ENREGISTRER_PROFIL("EnregistrerProfil"),

    NOTIFICATIN_CONNEXION("NotificationConnexion"),

    AFFICHER_PROFIL("AfficherProfil"),

    VOIR_MESSAGES("VoirMessages"),
    ENVOYER_MESSAGE("EnvoyerMessage"),
    SUPPRIMER_MESSAGE("SupprimerMessage"),

    FERMER("Fermer");

    private final String requete;

    Requete(String requete) {
        this.requete = requete;
    }

    public String getRequete() {
        return this.requete;
    }

}

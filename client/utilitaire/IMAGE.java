package client.utilitaire;

public enum IMAGE {

    IP_ADDRESS("./client/ressources/images/ipAddress.png"),
    USER("./client/ressources/images/user.png"),
    LOGO("./client/ressources/images/logo.png"),
    DECONNECTER("./client/ressources/images/deconnecter.png"),
    PROFIL("./client/ressources/images/profil.png"),
    ACCUEIL("./client/ressources/images/accueil.png"),
    NOTIFICATION("./client/ressources/images/notification.png"),
    MESSAGE("./client/ressources/images/message.png"),
    PUBLICATION("./client/ressources/images/publication.png"),
    RECHERCHER("./client/ressources/images/rechercher.png"),
    LIKE("./client/ressources/images/like.png"),
    UNLIKE("./client/ressources/images/unlike.png"),
    CORBEILLE("./client/ressources/images/corbeille.png"),
    ENVOYER("./client/ressources/images/envoyer.png"),
    MICRO_START("./client/ressources/images/microStart.png"),
    INSERER_IMAGE("./client/ressources/images/ajouterImage.png"),
    IMAGE("./client/ressources/images/image.png"),
    MICRO_STOP("./client/ressources/images/microStop.png"),
    PLAY("./client/ressources/images/play.png"),
    PAUSE("./client/ressources/images/pause.png"),;

    private final String chemin;

    IMAGE(String chemin) {
        this.chemin = chemin;
    }

    public String getChemin() {
        return this.chemin;
    }

}

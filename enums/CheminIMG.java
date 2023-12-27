package enums;

public enum CheminIMG {

    LOGO("./client/images/sysx.png"),
    ACCUEIL("./client/images/accueil.png"),
    NOTIFICATION("./client/images/notification.png"),
    MESSAGE("./client/images/message.png"),
    PUBLICATION("./client/images/publication.png"),
    LIKE("./client/images/like.png"),
    UNLIKE("./client/images/unlike.png"),
    MICROPHONE("./client/images/microphone.png"),
    MICROPHONE_2("./client/images/microphone_2.png"),
    PLAY("./client/images/play.png"),
    PAUSE("./client/images/pause.png"),
    STOP("./client/images/stop.png");

    private String chemin;

    CheminIMG(String chemin) {
        this.chemin = chemin;
    }

    public String getChemin() {
        return this.chemin;
    }

}

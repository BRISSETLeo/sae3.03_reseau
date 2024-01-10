package enums;

public enum CheminIMG {

    LOGO("./client/static/images/sysx.png"),
    ACCUEIL("./client/static/images/accueil.png"),
    NOTIFICATION("./client/static/images/notification.png"),
    MESSAGE("./client/static/images/message.png"),
    PUBLICATION("./client/static/images/publication.png"),
    LIKE("./client/static/images/like.png"),
    UNLIKE("./client/static/images/unlike.png"),
    MICROPHONE("./client/static/images/microphone.png"),
    MICROPHONE_2("./client/static/images/microphone_2.png"),
    PLAY("./client/static/images/play.png"),
    PAUSE("./client/static/images/pause.png"),
    STOP("./client/static/images/stop.png"),
    NO_PP("./client/static/images/nopp.png"),;

    private String chemin;

    CheminIMG(String chemin) {
        this.chemin = chemin;
    }

    public String getChemin() {
        return this.chemin;
    }

}

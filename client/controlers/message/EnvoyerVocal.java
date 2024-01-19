package client.controlers.message;

import client.Main;
import client.vocal.Son;
import client.vues.Message;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EnvoyerVocal implements EventHandler<ActionEvent> {

    private final Main main;
    private final Message message;
    private Son son;

    public EnvoyerVocal(Main main, Message message) {
        this.main = main;
        this.message = message;
    }

    @Override
    public void handle(ActionEvent event) {

        if (this.son == null) {
            this.son = new Son(this.main, this.message.getPseudoCorrespondant());
            this.son.start();
            this.message.getEnregistrerVocal().changerImage(this.message.getMicroStop());
        } else {
            this.son.stopperVocal();
            this.son = null;
            this.message.getEnregistrerVocal().changerImage(this.message.getMicroStart());
        }

    }

}

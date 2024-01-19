package client.controlers.message;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AfficherMessage implements EventHandler<MouseEvent> {

    private final Main main;

    public AfficherMessage(Main main) {
        this.main = main;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.getClient().afficherMessage(this.main.getPageDeMessage().getPseudoCorrespondant());

    }

}

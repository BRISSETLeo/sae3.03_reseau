package client.controlers.publication;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AfficherPublication implements EventHandler<MouseEvent> {

    private final Main main;

    public AfficherPublication(Main main) {
        this.main = main;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.afficherPageDeNewPublication();

    }

}

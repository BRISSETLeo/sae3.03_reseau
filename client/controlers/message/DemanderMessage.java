package client.controlers.message;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class DemanderMessage implements EventHandler<MouseEvent> {

    private final Main main;
    private final String pseudo;

    public DemanderMessage(Main main, String pseudo) {
        this.main = main;
        this.pseudo = pseudo;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.getClient().demanderMessage(this.pseudo);

    }

}

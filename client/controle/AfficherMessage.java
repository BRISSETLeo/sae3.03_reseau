package client.controle;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AfficherMessage implements EventHandler<MouseEvent> {

    private Main main;
    private String pseudo;

    public AfficherMessage(Main main, String pseudo) {
        this.main = main;
        this.pseudo = pseudo;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.afficherMessage(this.pseudo);

    }

}

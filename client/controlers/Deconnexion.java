package client.controlers;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Deconnexion implements EventHandler<MouseEvent> {

    private final Main main;

    public Deconnexion(Main main) {
        this.main = main;
    }

    @Override
    public void handle(MouseEvent event) {
        this.main.seDeconnecter();
    }

}

package client.controlers.accueil;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AfficherAccueil implements EventHandler<MouseEvent> {

    private final Main main;

    public AfficherAccueil(Main main) {
        this.main = main;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.afficherPageDAccueil();

    }

}

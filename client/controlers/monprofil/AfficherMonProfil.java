package client.controlers.monprofil;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AfficherMonProfil implements EventHandler<MouseEvent> {

    private Main main;

    public AfficherMonProfil(Main main) {
        this.main = main;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.afficherPageDeMonProfil();

    }

}

package client.controlers.profil;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AfficherProfil implements EventHandler<MouseEvent> {

    private final Main main;
    private final String pseudo;

    public AfficherProfil(Main main, String pseudo) {
        this.main = main;
        this.pseudo = pseudo;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.getClient().demanderProfil(this.pseudo);

    }

}

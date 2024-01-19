package client.controlers.connexion;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SeConnecterEntrer implements EventHandler<KeyEvent> {

    private final SeConnecter seConnecter;

    public SeConnecterEntrer(SeConnecter seConnecter) {
        this.seConnecter = seConnecter;
    }

    @Override
    public void handle(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {

            this.seConnecter.handle();

        }

    }

}

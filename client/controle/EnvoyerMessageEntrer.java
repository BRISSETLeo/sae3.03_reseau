package client.controle;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EnvoyerMessageEntrer implements EventHandler<KeyEvent> {

    private Main main;

    public EnvoyerMessageEntrer(Main main) {
        this.main = main;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            this.main.envoyerMessage();
        }
    }

}

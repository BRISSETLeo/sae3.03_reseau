package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class EnvoyerMessageEntrer implements EventHandler<KeyEvent>{
    
    private Main main;

    public EnvoyerMessageEntrer(Main main) {
        this.main = main;
    }

    @Override
    public void handle(KeyEvent arg0) {
        this.main.envoyerMessage();
    }
    
}

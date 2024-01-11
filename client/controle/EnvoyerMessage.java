package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EnvoyerMessage implements EventHandler<ActionEvent>{
    
    private Main main;

    public EnvoyerMessage(Main main) {
        this.main = main;
    }

    @Override
    public void handle(ActionEvent arg0) {
        this.main.envoyerMessage();
    }
    
}

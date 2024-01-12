package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SupprimerMessage implements EventHandler<ActionEvent> {
    
    private Main main;
    private int idMessage;

    public SupprimerMessage(Main main, int idMessage) {
        this.main = main;
        this.idMessage = idMessage;
    }

    @Override
    public void handle(ActionEvent event) {
        
        this.main.supprimerMessage(this.idMessage);
        
    }

}

package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SupprimerNotification implements EventHandler<ActionEvent> {
    
    private Main main;
    private int idNotification;

    public SupprimerNotification(Main main, int idNotification) {
        this.main = main;
        this.idNotification = idNotification;
    }

    @Override
    public void handle(ActionEvent event) {
        
        this.main.supprimerNotification(this.idNotification);
        
    }

}

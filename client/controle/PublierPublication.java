package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class PublierPublication implements EventHandler<ActionEvent> {
    
    private Main main;

    public PublierPublication(Main main){
        this.main = main;
    }

    @Override
    public void handle(ActionEvent event) {
        
        this.main.publierPublication();

    }

    

    
}

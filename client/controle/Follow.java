package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Follow implements EventHandler<ActionEvent> {
    
    private Main main;
    private String pseudoFollow;
    
    public Follow(Main main, String pseudoFollow) {
        this.main = main;
        this.pseudoFollow = pseudoFollow;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.follow(this.pseudoFollow);

    }

}

package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class UnFollow  implements EventHandler<ActionEvent> {
    
    private Main main;
    private String pseudoUnFollow;
    
    public UnFollow(Main main, String pseudoUnFollow) {
        this.main = main;
        this.pseudoUnFollow = pseudoUnFollow;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.unfollow(this.pseudoUnFollow);

    }

}

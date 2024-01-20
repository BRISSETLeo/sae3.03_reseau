package client.controlers.profil;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EcrireNewMessage implements EventHandler<ActionEvent> {
    
    private final Main main;
    private final String pseudo;

    public EcrireNewMessage(Main main, String pseudo) {
        this.main = main;
        this.pseudo = pseudo;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.getClient().afficherMessage(this.pseudo);
        this.main.getClient().demanderMessage(this.pseudo);

    }

}

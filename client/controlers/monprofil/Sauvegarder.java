package client.controlers.monprofil;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Sauvegarder implements EventHandler<ActionEvent> {

    private final Main main;

    public Sauvegarder(Main main) {
        this.main = main;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.getClient().sauvegarderProfil();

    }

}

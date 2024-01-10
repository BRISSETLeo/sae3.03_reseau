package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SupprimerPublication implements EventHandler<ActionEvent> {

    private Main main;
    private int id;

    public SupprimerPublication(Main main, int id) {
        this.main = main;
        this.id = id;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.supprimerPublication(this.id);

    }

}

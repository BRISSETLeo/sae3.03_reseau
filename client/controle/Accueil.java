package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Accueil implements EventHandler<ActionEvent> {

    private Main main;

    public Accueil(Main main) {
        this.main = main;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.afficherPageAccueil();

    }

}

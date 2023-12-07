package graphique.controller;

import graphique.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EnvoyerMessage implements EventHandler<ActionEvent> {

    private final CliquerConnexion cliquerConnexion;

    public EnvoyerMessage(CliquerConnexion cliquerConnexion) {
        this.cliquerConnexion = cliquerConnexion;
    }

    @Override
    public void handle(ActionEvent event) {

        Main.getClient().setMessage(this.cliquerConnexion.t);

    }

}

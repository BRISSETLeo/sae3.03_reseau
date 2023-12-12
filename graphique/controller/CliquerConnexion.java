package graphique.controller;

import client.Client;
import graphique.Main;
import graphique.page.Connexion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class CliquerConnexion implements EventHandler<ActionEvent> {

    private final Connexion connexion;

    public CliquerConnexion(Connexion connexion) {
        this.connexion = connexion;
    }

    @Override
    public void handle(ActionEvent event) {

        if (this.connexion.getPseudo().getText().equals("")) {
            return;
        }

        Main instance = Main.getInstance();

        Client client = new Client(this.connexion.getIp().getText(), this.connexion.getPseudo().getText());
        instance.setClient(client);
        client.start();
        instance.changerWindow(instance.getAccueil(), 1000, 700);
        client.demanderPublication();

    }

}

package graphique.controller;

import client.Client;
import graphique.Main;
import graphique.page.Accueil;
import graphique.page.Connexion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class CliquerConnexion implements EventHandler<ActionEvent> {

    private final Connexion connexion;

    public CliquerConnexion(Connexion connexion) {
        this.connexion = connexion;
    }

    @Override
    public void handle(ActionEvent event) {

        Client client = new Client(this.connexion.getIp().getText(), this.connexion.getPseudo().getText());
        Main.setClient(client);
        client.start();
        Main.getStage().setScene(new Scene(new Accueil(), 1000, 700));

    }

}

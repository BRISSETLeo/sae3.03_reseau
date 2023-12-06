package graphique.controller;

import client.Client;
import graphique.Main;
import graphique.page.Connexion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CliquerConnexion implements EventHandler<ActionEvent> {

    private final Connexion connexion;
    public TextField t;

    public CliquerConnexion(Connexion connexion) {
        this.connexion = connexion;
    }

    @Override
    public void handle(ActionEvent event) {

        Client client = new Client(this.connexion.getIp().getText(), this.connexion.getPseudo().getText());
        Main.setClient(client);
        client.start();
        Button b = new Button("Envoyer");
        b.setOnAction(new EnvoyerMessage(this));
        this.t = new TextField("");
        Main.getBorderPane().setCenter(new VBox(b, t));

    }

}

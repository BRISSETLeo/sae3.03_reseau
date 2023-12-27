package client.controle;

import client.Main;
import client.graphisme.Connexion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ConnexionC implements EventHandler<ActionEvent> {

    private Main main;
    private Connexion connexion;

    public ConnexionC(Main main, Connexion connexion) {
        this.main = main;
        this.connexion = connexion;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.connecterLeClient(this.connexion.getAdresse(), this.connexion.getPseudo());

    }

}

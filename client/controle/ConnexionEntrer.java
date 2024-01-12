package client.controle;

import client.Main;
import client.graphisme.Connexion;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ConnexionEntrer implements EventHandler<KeyEvent> {

    private Main main;
    private Connexion connexion;

    public ConnexionEntrer(Main main, Connexion connexion) {
        this.main = main;
        this.connexion = connexion;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            this.main.connecterLeClient(this.connexion.getAdresse(), this.connexion.getPseudo());
        }
    }

}
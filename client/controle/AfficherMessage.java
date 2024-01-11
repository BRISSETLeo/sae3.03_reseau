package client.controle;

import caches.Compte;
import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class AfficherMessage implements EventHandler<MouseEvent> {

    private Main main;
    private Compte compte;

    public AfficherMessage(Main main, Compte compte) {
        this.main = main;
        this.compte = compte;
    }

    @Override
    public void handle(MouseEvent event) {

        this.main.afficherMessage(this.compte);

    }

}

package client.controlers.recherche;

import client.Main;
import client.vues.Recherche;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class RechercherCompte implements EventHandler<KeyEvent> {

    private final Main main;
    private final Recherche recherche;

    public RechercherCompte(Main main, Recherche recherche) {
        this.main = main;
        this.recherche = recherche;
    }

    @Override
    public void handle(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {

            String recherche = this.recherche.getRechercheField().getText();

            if (recherche.isBlank()) {
                this.recherche.clear();
            } else {
                this.main.getClient().rechercherComptes(recherche);
            }

        }

    }

}

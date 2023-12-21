package graphique.controller;

import graphique.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AjouterLike implements EventHandler<ActionEvent> {

    private String idPublication;

    public AjouterLike(String idPublication) {
        this.idPublication = idPublication;
    }

    @Override
    public void handle(ActionEvent arg0) {
        Main.getInstance().getClient().ajouterLike(this.idPublication);
    }

}

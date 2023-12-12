package graphique.controller;

import graphique.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class PagePublication implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {

        Main instance = Main.getInstance();
        instance.getAccueil().setPageCenter(instance.getPublication());

    }

}

package client.graphisme;

import caches.MessageC;
import client.Main;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Messagerie extends VBox {

    private Main main;

    public Messagerie(Main main) {
        this.main = main;

        super.getChildren().addAll(new Label("Messages"));

    }

    public void ajouterMessage(MessageC message) {
        super.getChildren().add(new Label(message.getIdMessage() + ""));
        super.getChildren().add(new Label(message.getPseudoExpediteur()));
        super.getChildren().add(new Label(message.getPseudoDestinataire()));
        super.getChildren().add(new Label(message.getContent()));
        super.getChildren().add(new Label(message.getDate() + ""));
    }

    public void clear() {
        super.getChildren().clear();
    }

}

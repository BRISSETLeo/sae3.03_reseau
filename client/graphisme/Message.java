package client.graphisme;

import caches.Compte;
import client.Main;
import client.controle.AfficherMessage;
import enums.CheminCSS;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Message extends VBox {

    private Main main;

    public Message(Main main) {
        this.main = main;

        super.getChildren().addAll(new Label("Messages"));

    }

    public void ajouterCompte(Compte compte) {
        HBox compteBox = new CompteBox(compte);
        super.getStylesheets().add(CheminCSS.COMPTEBOX.getChemin());
        compteBox.setOnMouseClicked(new AfficherMessage(this.main, compte.getPseudo()));

        HBox.setMargin(compteBox, new Insets(10, 10, 10, 10));
        HBox.setHgrow(compteBox, Priority.ALWAYS);

        super.getChildren().add(compteBox);
    }

}

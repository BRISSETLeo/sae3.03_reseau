package client.graphisme;

import caches.Compte;
import caches.MessageC;
import client.Main;
import client.controle.AfficherMessage;
import client.controle.CloseRight;
import client.graphisme.affichage.ButtonG;
import enums.CheminCSS;
import enums.CheminIMG;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Message extends VBox {

    private Main main;

    public Message(Main main) {
        this.main = main;

        ButtonG close = new ButtonG(CheminIMG.CLOSE.getChemin());
        close.setOnAction(new CloseRight(this.main));
        HBox hBox = new HBox(close);
        hBox.getStylesheets().add(CheminCSS.PROFIL.getChemin());

        super.getChildren().addAll(hBox, new Label("Messages"));

        super.getStylesheets().add(CheminCSS.MESSAGE.getChemin());
    }

    public void ajouterCompte(Compte compte, MessageC message) {
        HBox compteBox = new CompteBox(this.main, compte);
        super.getStylesheets().add(CheminCSS.COMPTEBOX.getChemin());
        compteBox.setOnMouseClicked(new AfficherMessage(this.main, compte));

        Label content = new Label(message.getContent());
        if (message.isLu() || message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) {
            content.getStyleClass().add("lu");
        } else {
            content.getStyleClass().add("non-lu");
        }
        ((VBox) compteBox.getChildren().get(1)).getChildren().add(content);

        HBox.setMargin(compteBox, new Insets(10, 10, 10, 10));
        HBox.setHgrow(compteBox, Priority.ALWAYS);

        super.getChildren().add(compteBox);
    }

}

package client.graphisme;

import javafx.scene.control.ScrollPane;
import java.text.SimpleDateFormat;

import caches.Compte;
import caches.MessageC;
import client.Main;
import client.controle.EnvoyerMessage;
import client.graphisme.affichage.LabelF;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import enums.CheminCSS;

public class Messagerie extends ScrollPane {

    private Main main;
    private Compte compteFollow;
    private VBox messagesContainer;

    private String pseudoDest;
    private TextField newMessage;

    public Messagerie(Main main) {
        this.main = main;

        this.messagesContainer = new VBox(); 
        super.setContent(this.messagesContainer); 

        super.getStylesheets().add(CheminCSS.TEXTEFIELD.getChemin());

        super.setFitToWidth(true); 
    }

    public void ajouterMessage(MessageC message) {
        VBox contentBox = new VBox();
        CompteBox compteBox = new CompteBox(this.main, this.compteFollow);
        Label content = new Label(message.getContent());
        LabelF date = new LabelF(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(message.getDate()));
        this.newMessage = new TextField();
        HBox compteDateBox = new HBox();
        Button envoyer = new Button("Envoyer");
        envoyer.setOnAction(new EnvoyerMessage(this.main));

        this.pseudoDest = (message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) ? 
        message.getPseudoDestinataire() : message.getPseudoExpediteur();

        messagesContainer.setAlignment(Pos.BOTTOM_LEFT); 
        messagesContainer.setFillWidth(true);
        VBox.setMargin(this.newMessage, new Insets(10, 0, 10, 0));

        contentBox.setPadding(new javafx.geometry.Insets(10));
        compteDateBox.setPadding(new Insets(5, 0, 5, 0));

        this.newMessage.setPromptText("Votre message...");

        compteDateBox.getChildren().addAll(compteBox, Main.createRegion(), date);
        contentBox.getChildren().add(compteDateBox);
        contentBox.getChildren().addAll(content);

        messagesContainer.getChildren().addAll(contentBox, this.newMessage, envoyer);

    }

    public void clear() {
        messagesContainer.getChildren().clear();
    }

    public String getPseudoDest() {
        return this.pseudoDest;
    }

    public String getNewMessage() {
        return this.newMessage.getText();
    }

    public void setCompteFollow(Compte compteFollow) {
        this.compteFollow = compteFollow;
    }
}  

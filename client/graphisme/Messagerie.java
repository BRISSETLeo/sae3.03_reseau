package client.graphisme;

import javafx.scene.control.ScrollPane;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caches.Compte;
import caches.MessageC;
import client.Main;
import client.controle.EnvoyerMessage;
import client.controle.SupprimerMessage;
import client.graphisme.affichage.ButtonG;
import client.graphisme.affichage.ImageViewS;
import client.graphisme.affichage.LabelF;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import enums.CheminCSS;
import enums.CheminIMG;
import enums.FontP;

public class Messagerie extends VBox {

    Map<Integer,VBox> listMessages;

    private Main main;
    private Compte compteFollow;
    private VBox messagesContainer;

    private String pseudoDest;
    private TextField newMessage;
	private Compte compteUser;
    private ScrollPane scrollPane;

    public Messagerie(Main main) {
        this.main = main;
        this.listMessages = new HashMap<>();
        this.messagesContainer = new VBox(); 
        scrollPane = new ScrollPane(messagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        this.newMessage = new TextField();
        this.newMessage.setPromptText("Votre message...");

        super.getStylesheets().add(CheminCSS.TEXTEFIELD.getChemin());

        Button envoyer = new Button("Envoyer");
        envoyer.setOnAction(new EnvoyerMessage(this.main));

        HBox inputBox = new HBox();
        inputBox.getChildren().addAll(this.newMessage, envoyer);
        HBox.setHgrow(this.newMessage, Priority.ALWAYS);

        
        super.getChildren().addAll(scrollPane,inputBox);
        

    }

    public void ajouterMessage(MessageC message) {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
        this.compteUser = this.main.getCompte();
        VBox contentBox = new VBox();
        CompteBox compteBox = new CompteBox(this.main, ((message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) ? compteUser : compteFollow));
        Label content = new Label(message.getContent());
        content.setWrapText(true);
        content.setFont(FontP.FONT_15.getFont());
        LabelF date = new LabelF(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(message.getDate()));
        HBox compteDateBox = new HBox();

        this.pseudoDest = (message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) ? 
        message.getPseudoDestinataire() : message.getPseudoExpediteur();

        contentBox.setPadding(new javafx.geometry.Insets(10));
        compteDateBox.setPadding(new Insets(5, 0, 5, 0));

        compteDateBox.getChildren().addAll(compteBox, Main.createRegion(), date);
        Button supprimerMessage = new Button("Supprimer");
        supprimerMessage.setOnAction(new SupprimerMessage(this.main, message.getIdMessage()));
        HBox deleteBox = new HBox(Main.createRegion(),  supprimerMessage);
        contentBox.getChildren().addAll(compteDateBox, content);
        if (message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) {
            contentBox.getChildren().add(deleteBox);
        }

        messagesContainer.getChildren().addAll(contentBox);
        newMessage.clear();
        listMessages.put(message.getIdMessage(), contentBox);
        

    }

    public void ajouteMessage2 (MessageC message) {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
        this.compteUser = this.main.getCompte();
        this.pseudoDest = (message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) ? 
        message.getPseudoDestinataire() : message.getPseudoExpediteur();
        Label content = new Label(message.getContent());
        HBox compteDateBox = new HBox();

        VBox contentBoxSender = new VBox();
        VBox contentBoxReceiver = new VBox();

        CompteBox compteBox = new CompteBox(this.main, ((message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) ? compteUser : compteFollow));
    }

    public void clear() {
        this.messagesContainer.getChildren().clear();
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

    public void removeMessage(int idMessage) {
        double vValue = scrollPane.getVvalue();
        Platform.runLater(() -> scrollPane.setVvalue(vValue));
        this.messagesContainer.getChildren().remove(this.listMessages.get(idMessage));
        this.listMessages.remove(idMessage);
    }
}  

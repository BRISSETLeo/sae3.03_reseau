package client.graphisme;

import javafx.scene.control.ScrollPane;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import caches.Compte;
import caches.MessageC;
import client.Main;
import client.controle.EnvoyerMessage;
import client.controle.EnvoyerMessageEntrer;
import client.controle.SupprimerMessage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import enums.CheminCSS;
import enums.FontP;

public class Messagerie extends VBox {

    private Map<Integer, VBox> listMessages;

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
        this.messagesContainer = new VBox(20);
        this.scrollPane = new ScrollPane(messagesContainer);
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        this.newMessage = new TextField();
        this.newMessage.setPromptText("Votre message...");

        Button envoyer = new Button("Envoyer");
        envoyer.setOnAction(new EnvoyerMessage(this.main));
        this.newMessage.setOnKeyPressed(new EnvoyerMessageEntrer(this.main));

        HBox inputBox = new HBox();
        inputBox.getChildren().addAll(this.newMessage, envoyer);
        HBox.setHgrow(this.newMessage, Priority.ALWAYS);

        super.getStylesheets().add(CheminCSS.TEXTEFIELD.getChemin());
        super.getChildren().addAll(scrollPane, inputBox);
        VBox.setVgrow(this.scrollPane, Priority.ALWAYS);

        this.scrollPane.setVvalue(1.0);
    }

    public void ajouterMessage(MessageC message) {

        this.compteUser = this.main.getCompte();
        this.pseudoDest = (message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo()))
                ? message.getPseudoDestinataire()
                : message.getPseudoExpediteur();
        boolean ouPlacerLeMessage = message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())
                || message.getPseudoDestinataire().equals(this.compteFollow.getPseudo());
        CompteBox compteBox = new CompteBox(this.main,
                (message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo())) ? this.compteUser
                        : this.compteFollow,
                !ouPlacerLeMessage);
        compteBox.changerFont();

        Label msg = new Label(message.getContent());
        msg.setWrapText(true);
        msg.setFont(FontP.FONT_15.getFont());

        Label date = new Label(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(message.getDate()));
        HBox compteDateBox = new HBox();

        VBox content = new VBox(compteDateBox, msg);

        if (ouPlacerLeMessage) {
            compteDateBox.getChildren().addAll(compteBox, Main.createRegion(), date);
            compteDateBox.setAlignment(Pos.CENTER_LEFT);
            compteBox.setAlignment(Pos.CENTER_LEFT);
            content.setAlignment(Pos.CENTER_LEFT);
        } else {
            compteDateBox.getChildren().addAll(date, Main.createRegion(), compteBox);
            compteDateBox.setAlignment(Pos.CENTER_RIGHT);
            compteBox.setAlignment(Pos.CENTER_RIGHT);
            content.setAlignment(Pos.CENTER_RIGHT);
        }

        this.messagesContainer.getChildren().add(content);

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
        this.messagesContainer.getChildren().remove(this.listMessages.get(idMessage));
        this.listMessages.remove(idMessage);
        this.mettreToutEnBas();
    }

    private void mettreToutEnBas() {
        this.scrollPane.applyCss();
        this.scrollPane.layout();

        double scrollValue = this.scrollPane.getVmax();
        this.scrollPane.setVvalue(scrollValue);
    }

    /*
     * this.compteUser = this.main.getCompte();
     * if (this.compteUser == null || this.compteFollow == null)
     * return;
     * 
     * boolean isSentMessage =
     * message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo());
     * 
     * VBox contentBox = new VBox();
     * CompteBox compteBox = new CompteBox(this.main, (isSentMessage ?
     * this.compteUser : this.compteFollow));
     * 
     * Label content = new Label(message.getContent());
     * content.setWrapText(true);
     * content.setFont(FontP.FONT_15.getFont());
     * 
     * Label date = new Label(new
     * SimpleDateFormat("dd/MM/yyyy HH:mm").format(message.getDate()));
     * HBox compteDateBox = new HBox(Main.createRegion(), date);
     * 
     * this.pseudoDest =
     * (message.getPseudoExpediteur().equals(this.main.getCompte().getPseudo()))
     * ? message.getPseudoDestinataire()
     * : message.getPseudoExpediteur();
     * 
     * Button supprimerMessage = new Button("Supprimer");
     * supprimerMessage.setOnAction(new SupprimerMessage(this.main,
     * message.getIdMessage()));
     * 
     * contentBox.getChildren().addAll(compteBox, content, compteDateBox,
     * new HBox(Main.createRegion(), supprimerMessage));
     * 
     * this.messagesContainer.getChildren().add(contentBox);
     * 
     * this.newMessage.clear();
     * 
     * this.listMessages.put(message.getIdMessage(), contentBox);
     * 
     * contentBox.setAlignment(Pos.CENTER);
     * 
     * this.mettreToutEnBas();
     */
}

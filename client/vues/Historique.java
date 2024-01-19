package client.vues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caches.Compte;
import caches.Message;
import client.Main;
import client.controlers.message.DemanderMessage;
import client.utilitaire.CSS;
import client.utilitaire.IMAGE;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Historique extends VBox {

    private final Main main;

    private final Map<String, ProfilBox> photoProfil;
    private final Map<String, VBox> containerMessage;

    private final VBox container;

    public Historique(Main main) {

        this.main = main;
        this.container = new VBox(10);
        this.photoProfil = new HashMap<>();
        this.containerMessage = new HashMap<>();

        ScrollPane scrollPane = new ScrollPane(this.container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        super.getChildren().add(scrollPane);
        super.getStylesheets().add(CSS.HISTORIQUE.getChemin());
        super.getStyleClass().add("container");

    }

    public void afficherHistorique(List<Message> messages) {

        this.container.getChildren().clear();

        for (caches.Message message : messages) {

            VBox container = new VBox();

            Label pseudo = new Label();
            Label lastMsg = new Label();
            Compte compte;

            if ((message.getContenu() == null || message.getContenu().isBlank()) && message.getVocal() != null) {
                lastMsg.setText("Message vocal");
                lastMsg.setGraphic(null);
                lastMsg.setContentDisplay(ContentDisplay.TEXT_ONLY);
            } else if (message.getImage() != null) {
                ImageView imgV = new ImageView(IMAGE.IMAGE.getChemin());
                imgV.setFitHeight(10);
                imgV.setFitWidth(10);
                imgV.setPreserveRatio(true);
                if (message.getContenu() != null && !message.getContenu().isBlank())
                    lastMsg.setText(message.getContenu());
                lastMsg.setGraphic(imgV);
                lastMsg.setContentDisplay(ContentDisplay.LEFT);
            } else {
                lastMsg.setGraphic(null);
                lastMsg.setText(message.getContenu());
                lastMsg.setContentDisplay(ContentDisplay.TEXT_ONLY);
            }

            if (message.getCompteDestinateur().getPseudo().equals(this.main.getMonCompte().getPseudo())) {
                compte = message.getCompte();
                pseudo.setText(message.getCompte().getPseudo());
                if (message.estLu())
                    lastMsg.getStyleClass().add("lu");
                else
                    lastMsg.getStyleClass().add("non-lu");
            } else {
                compte = message.getCompteDestinateur();
                pseudo.setText(message.getCompteDestinateur().getPseudo());
                lastMsg.getStyleClass().add("lu");
            }
            ProfilBox imageProfilBox = new ProfilBox(compte, 15);

            this.photoProfil.put(pseudo.getText(), imageProfilBox);

            container.setOnMouseClicked(new DemanderMessage(this.main, pseudo.getText()));

            container.getChildren().addAll(new HBox(imageProfilBox), lastMsg);
            container.getStyleClass().add("msg");

            this.containerMessage.put(pseudo.getText(), container);

            this.container.getChildren().add(container);

        }

    }

    public void updateLastMessage(Message message, boolean lu, boolean remonterEnHaut) {

        this.update(message,
                this.containerMessage.keySet().stream().filter(pseudo -> pseudo.equals(message.getCompte().getPseudo())
                        || pseudo.equals(message.getCompteDestinateur().getPseudo())).findFirst().get(),
                lu, remonterEnHaut);
    }

    private void update(Message message, String pseudo, boolean lu, boolean remonterEnHaut) {

        if (!this.containerMessage.containsKey(pseudo))
            return;

        Label lastMsg = (Label) this.containerMessage.get(pseudo).getChildren().get(1);
        lastMsg.getStyleClass().clear();
        if ((message.getContenu() == null || message.getContenu().isBlank()) && message.getVocal() != null) {
            lastMsg.setText("Message vocal");
            lastMsg.setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else if ((message.getContenu() == null || message.getContenu().isBlank()) && message.getImage() != null) {
            ImageView imgV = new ImageView(IMAGE.IMAGE.getChemin());
            imgV.setFitHeight(10);
            imgV.setFitWidth(10);
            imgV.setPreserveRatio(true);
            lastMsg.setGraphic(imgV);
            lastMsg.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
            lastMsg.setText(message.getContenu());
            lastMsg.setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        if (message.getCompte().getPseudo().equals(this.main.getMonCompte().getPseudo())) {
            lastMsg.getStyleClass().add("lu");
        } else {
            if (message.estLu() || lu)
                lastMsg.getStyleClass().add("lu");
            else
                lastMsg.getStyleClass().add("non-lu");
        }

        this.containerMessage.get(pseudo).getChildren().set(1, lastMsg);

        if (this.containerMessage.containsKey(pseudo) && remonterEnHaut) {
            VBox container = this.containerMessage.get(pseudo);
            this.container.getChildren().remove(container);
            this.container.getChildren().add(0, container);
        }

    }

}

package client.vues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.Main;
import client.controlers.message.EnvoyerMessage;
import client.controlers.message.EnvoyerVocal;
import client.controlers.message.InsererImage;
import client.controlers.message.TextFieldHandler;
import client.utilitaire.CSS;
import client.utilitaire.IMAGE;
import client.vocal.GestionSon;
import client.vues.nodes.Button;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Message extends VBox {

    private final Main main;
    private String pseudoCorrespondant;

    private final ScrollPane scrollPane;
    private final VBox container;
    private final TextField ecrireMessage;
    private final HBox ecrireMessageBox;
    private final Button envoyerMessage;
    private final Button enregistrerVocal;
    private final VBox containerScrollSearch;

    private final Image microStart;
    private final Image microStop;

    private final Historique historique;

    private final GestionSon gestionSon;

    private final Map<Integer, HBox> messages;

    public Message(Main main) {

        this.main = main;
        this.gestionSon = new GestionSon();

        SplitPane splitPane = new SplitPane();
        this.scrollPane = new ScrollPane();
        this.container = new VBox(10);
        this.container.getStyleClass().add("container");
        this.messages = new HashMap<>();

        this.historique = new Historique(this.main);
        this.scrollPane.setStyle("-fx-background-color: transparent;");

        this.microStart = new Image(IMAGE.MICRO_START.getChemin());
        this.microStop = new Image(IMAGE.MICRO_STOP.getChemin());

        Button insererImage = new Button(IMAGE.INSERER_IMAGE.getChemin(), 25);
        insererImage.setOnAction(new InsererImage(this));
        this.ecrireMessage = new TextField();
        this.ecrireMessage.setPromptText("Ecrivez un message...");
        this.ecrireMessage.getStyleClass().add("ecrire-message");
        this.ecrireMessage.setOnKeyPressed(new TextFieldHandler(this));
        this.enregistrerVocal = new Button(this.microStart, 25);
        this.enregistrerVocal.setOnAction(new EnvoyerVocal(this.main, this));
        this.envoyerMessage = new Button(IMAGE.ENVOYER.getChemin(), 25);
        this.envoyerMessage.setOnAction(new EnvoyerMessage(this.main, this));
        this.ecrireMessageBox = new HBox(insererImage, this.ecrireMessage, this.enregistrerVocal, this.envoyerMessage);
        this.envoyerMessage.setVisible(false);
        this.envoyerMessage.setManaged(false);
        this.ecrireMessageBox.setVisible(false);

        this.containerScrollSearch = new VBox(this.scrollPane, this.ecrireMessageBox);

        VBox.setVgrow(this.scrollPane, Priority.ALWAYS);

        splitPane.getItems().addAll(this.containerScrollSearch, this.historique);
        splitPane.setDividerPositions(0.7);

        splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                splitPane.setDividerPosition(0, 0.7);
            }
        });

        this.scrollPane.setContent(this.container);
        this.scrollPane.setFitToWidth(true);

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        HBox.setHgrow(this.ecrireMessage, Priority.ALWAYS);

        super.getChildren().add(splitPane);
        super.getStylesheets().add(CSS.MESSAGE.getChemin());

    }

    public void afficherMessagePrive(List<caches.Message> messages, String personneConcerne) {

        this.container.getChildren().clear();

        this.pseudoCorrespondant = personneConcerne;

        for (caches.Message message : messages) {

            this.historique.updateLastMessage(message, true, false);

            VBox container = new MessageBox(this, message, this.main);
            HBox hBox;
            if (container.getChildren().get(0) instanceof VBox)
            hBox = new HBox(container, Main.createRegion());
            else
                hBox = new HBox(Main.createRegion(), container);

            this.container.getChildren().add(hBox);
            this.messages.put(message.getIdMessage(), hBox);

        }

        this.ecrireMessageBox.setVisible(true);
        Platform.runLater(() -> scrollPane.setVvalue(1));

    }

    public void ajouterMessage(caches.Message message) {

        boolean lu = this.main.clientEstSurLaPageDesMessages() && this.pseudoCorrespondant != null;
        if (this.pseudoCorrespondant != null) {
            lu = lu && (this.pseudoCorrespondant.equals(message.getCompte().getPseudo())
                    || this.pseudoCorrespondant.equals(message.getCompteDestinateur().getPseudo()));
        }

        this.historique.updateLastMessage(message, lu, true);

        if (this.pseudoCorrespondant == null || (!this.pseudoCorrespondant.equals(message.getCompte().getPseudo())
                && !this.pseudoCorrespondant.equals(message.getCompteDestinateur().getPseudo())))
            return;

        boolean updateScroll = false;

        if (scrollPane.getVvalue() == 1.0)
            updateScroll = true;

        if (message.getCompte().getPseudo().equals(this.main.getMonCompte().getPseudo())) {
            this.ecrireMessage.clear();
            if (this.containerScrollSearch.getChildren().get(1) instanceof VBox)
                this.imageRetire();
            this.envoyerMessage.setVisible(false);
            this.envoyerMessage.setManaged(false);
            this.enregistrerVocal.setVisible(true);
            this.enregistrerVocal.setManaged(true);
        }

        VBox container = new MessageBox(this, message, this.main);
        if (container.getChildren().get(0) instanceof VBox)
            this.container.getChildren().add(new HBox(container, Main.createRegion()));
        else
            this.container.getChildren().add(new HBox(Main.createRegion(), container));

        if (updateScroll)
            Platform.runLater(() -> scrollPane.setVvalue(1));

    }

    public void afficherHistorique(List<caches.Message> messages) {
        this.historique.afficherHistorique(messages);
    }

    public Button getEnvoyerMessage() {
        return this.envoyerMessage;
    }

    public TextField getEcrireMessage() {
        return this.ecrireMessage;
    }

    public Button getEnregistrerVocal() {
        return this.enregistrerVocal;
    }

    public void redimenssionerScroll() {
        this.scrollPane.applyCss();
        this.scrollPane.layout();
        this.scrollPane.requestLayout();
        double vValue = this.scrollPane.getVvalue();
        Platform.runLater(() -> {
            this.scrollPane.setVvalue(vValue);
        });
    }

    public String getPseudoCorrespondant() {
        return this.pseudoCorrespondant;
    }

    public Image getMicroStart() {
        return this.microStart;
    }

    public Image getMicroStop() {
        return this.microStop;
    }

    public void switchImageMicro() {
        if (((ImageView) this.enregistrerVocal.getGraphic()).getImage().equals(this.microStart))
            this.enregistrerVocal.changerImage(this.microStop);
        else
            this.enregistrerVocal.changerImage(this.microStart);
    }

    public GestionSon getGestionSon() {
        return this.gestionSon;
    }

    public void supprimerMessage(int idMessage) {
        if (this.messages.containsKey(idMessage))
            this.container.getChildren().remove(this.messages.get(idMessage));
        this.redimenssionerScroll();
    }

    public void imageAjoute(Image image) {
        javafx.scene.control.Button supprimerImg = new javafx.scene.control.Button("X");
        supprimerImg.setOnAction(e -> this.imageRetire());
        this.containerScrollSearch.getChildren().add(1,
                new VBox(supprimerImg, new client.vues.nodes.ImageView(image, 200, 100)));
        this.ecrireMessage.requestFocus();
        this.enregistrerVocal.setVisible(false);
        this.enregistrerVocal.setManaged(false);
        this.envoyerMessage.setVisible(true);
        this.envoyerMessage.setManaged(true);
    }

    public void imageRetire() {
        this.containerScrollSearch.getChildren().remove(1);
        boolean isBlank = this.ecrireMessage.getText().isBlank();
        this.enregistrerVocal.setVisible(isBlank);
        this.enregistrerVocal.setManaged(isBlank);
        this.envoyerMessage.setVisible(!isBlank);
        this.envoyerMessage.setManaged(!isBlank);
    }

    public VBox getContainerScrollSearch() {
        return this.containerScrollSearch;
    }

}

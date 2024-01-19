package client.vues;

import client.Main;
import client.controlers.publication.ChangerImage;
import client.controlers.publication.GlisserImage;
import client.controlers.publication.LacherImage;
import client.controlers.publication.PublierPublication;
import client.utilitaire.CSS;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Publication extends VBox {

    private final TextArea contenuPublication;
    private final ImageView insertionDImage;

    private final Button supprimerImage;

    public Publication(Main main) {

        int maxCarac = 500;

        Label nbCaracLabel = new Label("0/" + maxCarac);

        this.insertionDImage = new ImageView();
        this.insertionDImage.setFitHeight(300);
        this.insertionDImage.setFitWidth(500);
        this.insertionDImage.setPreserveRatio(true);

        Label label = new Label("Insérer une image");
        label.getStyleClass().add("insertion-image");
        label.setGraphic(this.insertionDImage);
        label.setContentDisplay(ContentDisplay.CENTER);

        label.setOnMouseClicked(new ChangerImage(this));
        label.setOnDragOver(new GlisserImage(this));
        label.setOnDragDropped(new LacherImage(this));

        Tooltip changerImage = new Tooltip("Ajouter une image a la publication");
        changerImage.setShowDelay(Duration.seconds(0));
        changerImage.setHideDelay(Duration.seconds(0));
        Tooltip.install(label, changerImage);

        Button publier = new Button("Publier");
        publier.setOnMouseClicked(new PublierPublication(main, this));
        HBox publierBox = new HBox(publier);

        this.supprimerImage = new Button("Supprimer l'image");
        this.supprimerImage.setOnAction(event -> {
            this.insertionDImage.setImage(null);
            this.supprimerImage.setVisible(false);
        });
        this.supprimerImage.getStyleClass().add("supprimer-image");
        this.supprimerImage.setVisible(false);

        this.contenuPublication = new TextArea();
        this.contenuPublication.setWrapText(true);
        this.contenuPublication.setPromptText("Contenu de la publication..");
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                this.clearErreur();
                if (change.getControlNewText().length() <= maxCarac) {
                    return change;
                }
                return null;
            }
            return change;
        });
        this.contenuPublication.setTextFormatter(textFormatter);
        this.contenuPublication.textProperty().addListener((observable, oldValue, newValue) -> {
            nbCaracLabel.setText(newValue.length() + "/" + maxCarac);
        });

        super.getChildren().addAll(new VBox(this.contenuPublication,
                new HBox(Main.createRegion(), nbCaracLabel),
                Main.createRegion(), label, this.supprimerImage), Main.createRegion(), publierBox);
        super.getStylesheets().add(CSS.NEW_PUBLICATION.getChemin());
        super.getStyleClass().add("container");

    }

    public ImageView getImage() {
        return this.insertionDImage;
    }

    public void changerImage(Image image) {
        this.insertionDImage.setImage(image);
        this.supprimerImage.setVisible(true);
    }

    public TextArea getContenuPublication() {
        return this.contenuPublication;
    }

    public void setErreur() {
        this.contenuPublication.getStyleClass().add("erreur");
    }

    private void clearErreur() {
        this.contenuPublication.getStyleClass().remove("erreur");
    }

    public ImageView getInsertionDImage() {
        return this.insertionDImage;
    }

    public void clearAll() {
        this.contenuPublication.clear();
        this.insertionDImage.setImage(null);
        this.supprimerImage.setVisible(false);
    }

}

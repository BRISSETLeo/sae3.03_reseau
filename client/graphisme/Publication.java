package client.graphisme;

import client.Main;
import client.controle.JouerSon;
import client.controle.StartVocal;
import client.controle.StopVocal;
import client.controle.StopperSon;
import enums.CheminIMG;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Publication extends VBox {

    private TextArea publication;
    private Button enregistrerVocal;
    private Main main;
    private ImageView startVocalView;
    private ImageView stopVocalView;
    private HBox vocalBox;
    private Button playButton;
    private Button arretButton;

    public Publication(Main main) {
        this.main = main;

        this.publication = new TextArea();
        this.publication.setPromptText("Contenu de la publication..");

        this.enregistrerVocal = new Button("");

        this.startVocalView = new ImageView(CheminIMG.MICROPHONE.getChemin());
        this.startVocalView.setFitHeight(20);
        this.startVocalView.setFitWidth(20);

        this.stopVocalView = new ImageView(CheminIMG.MICROPHONE_2.getChemin());
        this.stopVocalView.setFitHeight(20);
        this.stopVocalView.setFitWidth(20);

        this.enregistrerVocal.setGraphic(this.startVocalView);
        this.enregistrerVocal.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.enregistrerVocal.setOnAction(new StartVocal(this.main));

        this.vocalBox = new HBox();

        Label messageVocal = new Label("Message vocal");
        this.playButton = new Button();
        this.playButton.setOnAction(new JouerSon(this.main));
        this.arretButton = new Button();
        this.arretButton.setOnAction(new StopperSon(this.main));

        ImageView playImg = this.createImageView(CheminIMG.PLAY.getChemin());
        ImageView arretImg = this.createImageView(CheminIMG.STOP.getChemin());

        this.playButton.setGraphic(playImg);
        this.playButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.arretButton.setGraphic(arretImg);
        this.arretButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.vocalBox.getChildren().addAll(messageVocal, this.playButton, this.arretButton);
        this.vocalBox.setVisible(false);

        Button publier = new Button("Publier");

        this.getChildren().addAll(publication, this.enregistrerVocal, this.vocalBox, publier);
    }

    public void changerEnregistrerVocal() {
        if (this.enregistrerVocal.getGraphic().equals(this.stopVocalView)) {
            this.enregistrerVocal.setGraphic(this.startVocalView);
            this.enregistrerVocal.setOnAction(new StartVocal(this.main));
        } else {
            this.enregistrerVocal.setGraphic(this.stopVocalView);
            this.enregistrerVocal.setOnAction(new StopVocal(this.main));
        }
    }

    private ImageView createImageView(String chemin) {
        ImageView img = new ImageView(chemin);
        img.setFitHeight(20);
        img.setFitWidth(20);
        return img;
    }

    public void messageVocal() {
        this.vocalBox.setVisible(true);
    }

}

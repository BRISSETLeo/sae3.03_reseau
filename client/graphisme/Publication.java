package client.graphisme;

import java.util.List;

import client.Main;
import client.controle.JouerSon;
import client.controle.PauseSon;
import client.controle.PublierPublication;
import client.controle.StartVocal;
import client.controle.StopVocal;
import client.controle.StopperSon;
import client.controle.Unpause;
import enums.CheminCSS;
import enums.CheminFONT;
import enums.CheminIMG;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Publication extends VBox {

    private Main main;

    private Button enregistrerVocal;

    private ImageView startVocalView;
    private ImageView stopVocalView;

    private VBox combinaisonVocal;
    private HBox vocalBox;

    private Button playPauseButton;
    private Button arretButton;

    private ImageView playImg;
    private ImageView pauseImg;

    private TextArea publication;

    private Label erreur;
    private Label aucunSon;

    public Publication(Main main) {
        this.main = main;

        this.publication = new TextArea();
        this.publication.setPromptText("Contenu de la publication..");
        int maxLength = 500;

        this.aucunSon = new Label("Aucun son n'a été enregistré.");

        Label label = new Label("0/" + maxLength);

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                if (change.getControlNewText().length() <= maxLength) {
                    return change;
                }
                return null;
            }
            return change;
        });
        this.publication.textProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(newValue.length() + "/" + maxLength);
        });
        this.publication.setWrapText(true);
        this.publication.setTextFormatter(textFormatter);

        this.enregistrerVocal = new Button("");

        this.startVocalView = this.createImageView(CheminIMG.MICROPHONE.getChemin());
        this.stopVocalView = this.createImageView(CheminIMG.MICROPHONE_2.getChemin());

        this.enregistrerVocal.setGraphic(this.startVocalView);
        this.enregistrerVocal.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.enregistrerVocal.setOnAction(new StartVocal(this.main));

        this.combinaisonVocal = new VBox(2);
        this.vocalBox = new HBox(2);

        this.playPauseButton = new Button();
        this.playPauseButton.setOnAction(new JouerSon(this.main));
        this.arretButton = new Button();
        this.arretButton.setOnAction(new StopperSon(this.main));

        this.playImg = this.createImageView(CheminIMG.PLAY.getChemin());
        this.pauseImg = this.createImageView(CheminIMG.PAUSE.getChemin());
        ImageView arretImg = this.createImageView(CheminIMG.STOP.getChemin());

        this.playPauseButton.setGraphic(this.playImg);
        this.playPauseButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.arretButton.setGraphic(arretImg);
        this.arretButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.erreur = new Label("Vous ne pouvez pas publier si aucun des champs n'est rempli.");
        this.erreur.setStyle("-fx-text-fill: red;");
        this.erreur.setVisible(false);
        this.erreur.setWrapText(true);

        this.combinaisonVocal.getChildren().addAll(this.vocalBox, new HBox(2, this.playPauseButton,
                this.arretButton));
        this.combinaisonVocal.setVisible(false);

        Button publier = new Button("Publier");
        publier.getStyleClass().add("publier");
        publier.setOnAction(new PublierPublication(this.main));

        Region vRegion = new Region();
        VBox.setVgrow(vRegion, Priority.ALWAYS);

        Region hRegion = new Region();
        HBox.setHgrow(hRegion, Priority.ALWAYS);

        Label pubiLabel = new Label("Publication");

        this.aucunSon.setVisible(false);

        Font font = Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 20);
        pubiLabel.setFont(font);
        publier.setFont(font);

        super.getStylesheets().add(CheminCSS.PUBLICATION.getChemin());
        super.getChildren().addAll(pubiLabel, this.publication, new HBox(this.enregistrerVocal, this.aucunSon, hRegion, label),
                this.combinaisonVocal, vRegion,
                this.erreur, publier);
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

    public void messageVocal(List<Double> averages) {
        this.vocalBox.getChildren().clear();
        this.combinaisonVocal.setVisible(true);
        for (Double average : averages) {
            this.drawBar(average, false);
        }

    }

    private void drawBar(double amplitude, boolean vocalVide) {
        double barHeight = 5;
        if (!vocalVide) {
            barHeight = amplitude * 100;
            if (amplitude == 0 || barHeight < 1)
                return;
        }
        double barWidth = 3;
        Rectangle bar = new Rectangle(barWidth, barHeight);
        bar.getStyleClass().add("black-rectangle");
        this.vocalBox.getChildren().add(bar);
    }

    public void updateSon(int nbSecMax, int nbSecActuel) {

        int nbBarMax = this.vocalBox.getChildren().size();

        int partColoration = nbBarMax / (nbSecMax + 1);
        partColoration += (partColoration * nbSecActuel);

        for (int i = 0; i < nbBarMax; ++i) {
            if (i >= partColoration && nbSecMax != nbSecActuel)
                break;

            Rectangle rectangle = (Rectangle) this.vocalBox.getChildren().get(i);
            rectangle.getStyleClass().clear();
            rectangle.getStyleClass().add("black-rectangle");
        }

        if (nbSecMax == nbSecActuel) {
            this.arreterSon();
        }

    }

    public void jouerSon() {
        this.reprendreSon();
        for (Node children : this.vocalBox.getChildren()) {
            children.getStyleClass().clear();
            children.getStyleClass().add("grey-rectangle");
        }
    }

    public void arreterSon() {
        this.playPauseButton.setGraphic(this.playImg);
        this.playPauseButton.setOnAction(new JouerSon(this.main));
        for (Node children : this.vocalBox.getChildren()) {
            children.getStyleClass().clear();
            children.getStyleClass().add("black-rectangle");
        }
    }

    public void mettreEnPauseSon() {
        this.playPauseButton.setGraphic(this.playImg);
        this.playPauseButton.setOnAction(new Unpause(this.main));
    }

    public void reprendreSon() {
        this.playPauseButton.setGraphic(this.pauseImg);
        this.playPauseButton.setOnAction(new PauseSon(this.main));
    }

    public TextArea getPublication() {
        return this.publication;
    }

    public void erreur() {
        this.erreur.setVisible(true);
    }

    public void aucunSon(){
        this.aucunSon.setVisible(true);
    }

    public void resetSon(){
        this.aucunSon.setVisible(false);
    }

    public void reset(){
        this.publication.setText("");
        this.vocalBox.getChildren().clear();
        this.combinaisonVocal.setVisible(false);
        this.erreur.setVisible(false);
        this.aucunSon.setVisible(false);
    }

}

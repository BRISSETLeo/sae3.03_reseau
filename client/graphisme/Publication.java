package client.graphisme;

import java.util.List;

import client.Main;
import client.controle.JouerSon;
import client.controle.PauseSon;
import client.controle.PublierPublication;
import client.controle.StartVocal;
import client.controle.StopVocal;
import client.controle.StopperSon;
import client.controle.SupprimerVocal;
import client.controle.Unpause;
import client.graphisme.affichage.ButtonF;
import client.graphisme.affichage.ButtonG;
import client.graphisme.affichage.ImageViewS;
import client.graphisme.affichage.LabelF;
import client.graphisme.affichage.TextAreaPubli;
import enums.CheminCSS;
import enums.CheminIMG;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Publication extends VBox {

    private Main main;

    private ButtonG enregistrerVocal;

    private Image startVocalView;
    private Image stopVocalView;
    private Image playImg;
    private Image pauseImg;

    private VBox combinaisonVocal;
    private HBox vocalBox;

    private ButtonG playPauseButton;
    private ButtonG arretButton;

    private TextAreaPubli publication;

    private Label erreur;
    private Label aucunSon;

    private JouerSon jouerSon;
    private PauseSon pauseSon;
    private Unpause unpauseSon;

    private StartVocal startVocal;
    private StopVocal stopVocal;

    public Publication(Main main) {
        this.main = main;

        int maxLength = 500;

        this.publication = new TextAreaPubli(maxLength);
        this.aucunSon = new Label("Aucun son n'a été enregistré.");
        this.erreur = new Label("Vous ne pouvez pas publier si aucun des champs n'est rempli.");
        this.startVocalView = new Image(CheminIMG.MICROPHONE.getChemin());
        this.stopVocalView = new Image(CheminIMG.MICROPHONE_2.getChemin());
        this.playImg = new Image(CheminIMG.PLAY.getChemin());
        this.pauseImg = new Image(CheminIMG.PAUSE.getChemin());
        this.enregistrerVocal = new ButtonG(this.startVocalView);
        this.playPauseButton = new ButtonG(this.playImg);
        this.arretButton = new ButtonG(new ImageViewS(CheminIMG.STOP.getChemin()));
        this.combinaisonVocal = new VBox(2);
        this.vocalBox = new HBox(2);
        this.jouerSon = new JouerSon(this.main, null);
        this.pauseSon = new PauseSon(this.main, null);
        this.unpauseSon = new Unpause(this.main, null);
        this.startVocal = new StartVocal(this.main);
        this.stopVocal = new StopVocal(this.main);

        Label label = new Label("0/" + maxLength);
        this.publication.textProperty().addListener((observable, oldValue, newValue) -> {
            this.enleverErreur();
            label.setText(newValue.length() + "/" + maxLength);
        });

        this.enregistrerVocal.setOnAction(this.startVocal);
        this.playPauseButton.setOnAction(this.jouerSon);
        this.arretButton.setOnAction(new StopperSon(this.main, null));

        this.erreur.getStyleClass().add("erreur");
        this.erreur.setVisible(false);
        this.erreur.setWrapText(true);

        ButtonG supprimerVocal = new ButtonG(new ImageViewS(CheminIMG.CORBEILLE.getChemin()));
        supprimerVocal.setOnAction(new SupprimerVocal(this.main));

        this.combinaisonVocal.getChildren().addAll(this.vocalBox, new HBox(2, this.playPauseButton,
                this.arretButton, supprimerVocal));
        this.combinaisonVocal.setVisible(false);

        ButtonF publier = new ButtonF("Publier");
        publier.getStyleClass().add("publier");
        publier.setOnAction(new PublierPublication(this.main));

        this.aucunSon.setVisible(false);

        super.getStylesheets().add(CheminCSS.PUBLICATION.getChemin());
        super.getChildren().addAll(new LabelF("Publication"), this.publication,
                new HBox(this.enregistrerVocal, this.aucunSon, Main.createRegion(), label),
                this.combinaisonVocal, Main.createRegion(), this.erreur, publier);
    }

    public void mettreEnregistrementButtonAOff() {
        this.enregistrerVocal.setOnAction(this.startVocal);
        this.enregistrerVocal.setGraphic(this.startVocalView);
    }

    public void mettreEnregistrementButtonAOn() {
        this.enregistrerVocal.setOnAction(this.stopVocal);
        this.enregistrerVocal.setGraphic(this.stopVocalView);
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
        byte[] audioJouerActuellement = this.main.getAudioJouerActuellement();
        if (audioJouerActuellement != null)
            this.main.arreterSon(audioJouerActuellement);
        this.reprendreSon();
        for (Node children : this.vocalBox.getChildren()) {
            children.getStyleClass().clear();
            children.getStyleClass().add("grey-rectangle");
        }
    }

    public void arreterSon() {
        this.playPauseButton.setGraphic(this.playImg);
        this.playPauseButton.setOnAction(new JouerSon(this.main, null));
        for (Node children : this.vocalBox.getChildren()) {
            children.getStyleClass().clear();
            children.getStyleClass().add("black-rectangle");
        }
    }

    public void mettreEnPauseSon() {
        this.playPauseButton.setOnAction(this.unpauseSon);
        this.playPauseButton.setGraphic(this.playImg);
    }

    public void reprendreSon() {
        this.playPauseButton.setOnAction(this.pauseSon);
        this.playPauseButton.setGraphic(this.pauseImg);
    }

    public TextArea getPublication() {
        return this.publication;
    }

    public void erreur() {
        this.erreur.setVisible(true);
    }

    public void aucunSon() {
        this.aucunSon.setVisible(true);
    }

    public void resetSon() {
        this.vocalBox.getChildren().clear();
        this.combinaisonVocal.setVisible(false);
        this.enleverErreur();
        this.arreterSon();
        this.aucunSon.setVisible(false);
    }

    public void enleverErreur() {
        this.erreur.setVisible(false);
    }

    public void reset() {
        this.publication.setText("");
        this.vocalBox.getChildren().clear();
        this.combinaisonVocal.setVisible(false);
        this.erreur.setVisible(false);
        this.aucunSon.setVisible(false);
    }

}

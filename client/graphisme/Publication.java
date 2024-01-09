package client.graphisme;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import client.Main;
import client.controle.JouerSon;
import client.controle.StartVocal;
import client.controle.StopVocal;
import client.controle.StopperSon;
import enums.CheminCSS;
import enums.CheminFONT;
import enums.CheminIMG;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Publication extends VBox {

    private Main main;

    private TextArea publication;
    private Button enregistrerVocal;

    private ImageView startVocalView;
    private ImageView stopVocalView;

    private HBox vocalBox;
    private Button playButton;
    private Button arretButton;
    private Label tempsVocal;

    public Publication(Main main) {
        this.main = main;

        this.publication = new TextArea();
        this.publication.setPromptText("Contenu de la publication..");

        this.enregistrerVocal = new Button("");

        this.startVocalView = this.createImageView(CheminIMG.MICROPHONE.getChemin());
        this.stopVocalView = this.createImageView(CheminIMG.MICROPHONE_2.getChemin());

        this.enregistrerVocal.setGraphic(this.startVocalView);
        this.enregistrerVocal.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.enregistrerVocal.setOnAction(new StartVocal(this.main));

        this.vocalBox = new HBox(2);

        this.tempsVocal = new Label();
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

        // this.vocalBox.getChildren().addAll(this.tempsVocal, this.playButton,
        // this.arretButton);
        this.vocalBox.setVisible(false);

        Button publier = new Button("Publier");
        publier.getStyleClass().add("publier");

        Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);

        Font font = Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 15);
        publier.setFont(font);
        this.publication.setFont(font);

        super.getStylesheets().add(CheminCSS.PUBLICATION.getChemin());
        super.getChildren().addAll(publication, this.enregistrerVocal, this.vocalBox, region, publier);
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

    public void messageVocal(byte[] audio, String temps) {
        this.playAudio(audio);
        this.vocalBox.setVisible(true);
        this.tempsVocal(temps);
    }

    public void tempsVocal(String temps) {
        this.tempsVocal.setText(temps);
    }

    int MAX_BARS = 30;
    int barIndex = 0;

    private void playAudio(byte[] audioData) {
        try {
            AudioInputStream audioInputStream = new AudioInputStream(
                    new ByteArrayInputStream(audioData), this.main.getAudioFormat(), audioData.length);

            // Obtenir le format audio du flux d'entrée
            AudioFormat audioFormat = audioInputStream.getFormat();
            int frameSize = audioFormat.getFrameSize();

            byte[] buffer = new byte[1024 * frameSize]; // Ajustez la taille du tampon selon vos besoins

            int bytesRead;
            int samplesPerSecond = (int) audioFormat.getSampleRate();
            int samplesPerBar = Math.max(1, samplesPerSecond / MAX_BARS);

            this.vocalBox.getChildren().clear();
            barIndex = 0;
            while ((bytesRead = audioInputStream.read(buffer)) != -1 && barIndex < MAX_BARS) {
                processAudioBuffer(buffer, bytesRead, samplesPerBar, audioFormat);
            }

            audioInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processAudioBuffer(byte[] buffer, int bytesRead, int samplesPerBar, AudioFormat audioFormat) {
        int sampleSize = audioFormat.getSampleSizeInBits() / 8;
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead);

        double sum = 0;

        for (int i = 0; i < samplesPerBar && byteBuffer.remaining() >= sampleSize; i++) {
            double sampleValue;

            if (sampleSize == 1) {
                sampleValue = byteBuffer.get() / 128.0;
            } else {
                sampleValue = byteBuffer.getShort() / 32768.0;
            }

            sum += Math.abs(sampleValue);
        }

        double averageAmplitude = sum / samplesPerBar;
        drawBar(averageAmplitude);

        barIndex++;
    }

    private void drawBar(double amplitude) {
        double barHeight = amplitude * 100;
        if (barHeight < 1)
            return;
        double barWidth = 3;
        Rectangle bar = new Rectangle(barWidth, barHeight);
        this.vocalBox.getChildren().add(bar);
    }

}

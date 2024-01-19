package client.vues;

import client.controlers.message.EcouterVocal;
import client.controlers.message.EnleverEcouteVocal;
import client.utilitaire.IMAGE;
import client.vues.nodes.Button;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PatternVocal extends HBox {

    private final byte[] vocal;

    private final Button playPauseButton;

    private final Image play;
    private final Image pause;

    private final EcouterVocal ecouterVocal;
    private final EnleverEcouteVocal enleverEcouteVocal;

    public PatternVocal(Message message, byte[] vocal, boolean mettreDroite) {
        super(5);

        this.vocal = vocal;

        this.play = new Image(IMAGE.PLAY.getChemin());
        this.pause = new Image(IMAGE.PAUSE.getChemin());

        this.playPauseButton = new Button(this.play, 25);
        this.playPauseButton.setOnAction(this.ecouterVocal = new EcouterVocal(message, this));
        this.enleverEcouteVocal = new EnleverEcouteVocal(message, this);
        super.getChildren().add(this.playPauseButton);

        Rectangle bar = new Rectangle(3, 3);
        super.getChildren().add(bar);
        bar = new Rectangle(3, 3);
        super.getChildren().add(bar);

        for (int i = 0; i < 3; ++i) {
            bar = new Rectangle(3, 8);
            super.getChildren().add(bar);
            bar = new Rectangle(3, 12);
            super.getChildren().add(bar);
            bar = new Rectangle(3, 16);
            super.getChildren().add(bar);
            bar = new Rectangle(3, 12);
            super.getChildren().add(bar);
            bar = new Rectangle(3, 8);
            super.getChildren().add(bar);
        }

        bar = new Rectangle(3, 3);
        super.getChildren().add(bar);
        bar = new Rectangle(3, 3);
        super.getChildren().add(bar);

        if (mettreDroite)
            super.setAlignment(Pos.CENTER_RIGHT);
        else
            super.setAlignment(Pos.CENTER_LEFT);

    }

    public Image getPlay() {
        return this.play;
    }

    public Image getPause() {
        return this.pause;
    }

    public byte[] getVocal() {
        return this.vocal;
    }

    public void switchPlayPause() {
        if (((ImageView) this.playPauseButton.getGraphic()).getImage().equals(this.play)) {
            this.playPauseButton.changerImage(this.pause);
            this.playPauseButton.setOnAction(this.enleverEcouteVocal);
        } else {
            this.playPauseButton.changerImage(this.play);
            this.playPauseButton.setOnAction(this.ecouterVocal);
        }
    }

    public void updateBars(double progress) {
        int numBarsToUpdate = (int) ((double) 19 * progress);

        for (int i = 0; i < 19; i++) {
            Rectangle rectangle = (Rectangle) super.getChildren().get(i + 1);

            if (i < numBarsToUpdate) {
                rectangle.setFill(Color.WHITE);
            }
        }
    }

    public void resetBars() {
        for (int i = 0; i < 19; i++) {
            Rectangle rectangle = (Rectangle) super.getChildren().get(i + 1);
            rectangle.setFill(Color.BLACK);
        }
    }

}

package client.graphisme;

import caches.Compte;
import client.Main;
import client.graphisme.affichage.LabelF;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class CompteBox extends HBox {

    private Circle circle;

    public CompteBox(Main main, Compte compte) {
        super(10);

        this.circle = new Circle();
        this.circle.setRadius(20);

        this.circle.setFill(new ImagePattern(Main.blobToImage(compte.getImage())));

        super.setCursor(Cursor.HAND);
        super.setAlignment(Pos.CENTER_LEFT);
        super.getChildren().addAll(this.circle, new LabelF(compte.getPseudo()));

    }

    public Image getPhotoProfil() {
        return ((ImagePattern) this.circle.getFill()).getImage();
    }

    public void setPhotoProfil(Image image) {
        this.circle.setFill(new ImagePattern(image));
    }

}

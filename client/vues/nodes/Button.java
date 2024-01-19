package client.vues.nodes;

import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;

public class Button extends javafx.scene.control.Button {

    public Button(String chemin, int taille) {
        super.setGraphic(new ImageView(chemin, taille));
        super.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    public Button(Image image, int taille) {
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
        imageView.setFitHeight(taille);
        imageView.setFitWidth(taille);
        imageView.setPreserveRatio(true);
        super.setGraphic(imageView);
        super.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    public void changerImage(Image image) {
        ((javafx.scene.image.ImageView) super.getGraphic()).setImage(image);
    }

}

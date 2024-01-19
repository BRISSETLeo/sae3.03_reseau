package client.vues.nodes;

import javafx.scene.image.Image;

public class ImageView extends javafx.scene.image.ImageView {

    public ImageView(String chemin, double largeur, double hauteur) {
        super(chemin);
        super.setFitWidth(largeur);
        super.setFitHeight(hauteur);
        super.setPreserveRatio(true);
    }

    public ImageView(Image image, double largeur, double hauteur) {
        super(image);
        super.setFitWidth(largeur);
        super.setFitHeight(hauteur);
        super.setPreserveRatio(true);
    }

    public ImageView(String chemin, double size) {
        this(chemin, size, size);
    }

    public ImageView(double size) {
        super.setFitWidth(size);
        super.setFitHeight(size);
        super.setPreserveRatio(true);
    }

}

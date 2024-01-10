package client.graphisme.affichage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewS extends ImageView {

    public ImageViewS(String chemin) {
        super(chemin);
        super.setFitHeight(25);
        super.setFitWidth(25);
        super.setPreserveRatio(true);
    }

    public ImageViewS(Image image) {
        super(image);
        super.setFitHeight(25);
        super.setFitWidth(25);
        super.setPreserveRatio(true);
    }

    public ImageViewS() {
        super.setFitHeight(25);
        super.setFitWidth(25);
        super.setPreserveRatio(true);
    }

}

package client.graphisme.affichage;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ButtonG extends Button {

    private ImageView imageView;

    public ButtonG(String text) {
        if (!text.equals(""))
            this.imageView = new ImageViewS(text);
        else
            this.imageView = new ImageViewS();
        super.setGraphic(this.imageView);
        super.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    public ButtonG(Image image) {
        this.imageView = new ImageViewS(image);
        super.setGraphic(this.imageView);
        super.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    public ButtonG(ImageView imageView) {
        this.imageView = imageView;
        super.setGraphic(this.imageView);
        super.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    public ButtonG() {
        this("");
    }

    public void setGraphic(Image image) {
        this.imageView.setImage(image);
    }

}
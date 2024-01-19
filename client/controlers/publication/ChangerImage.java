package client.controlers.publication;

import java.io.File;

import client.vues.Publication;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class ChangerImage implements EventHandler<MouseEvent> {

    private final Publication publication;
    private final FileChooser fileChooser;

    public ChangerImage(Publication publication) {
        this.publication = publication;
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        this.fileChooser.setTitle("Choisir une image");
    }

    @Override
    public void handle(MouseEvent event) {

        File selectedFile = this.fileChooser.showOpenDialog(this.publication.getScene().getWindow());
        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            this.publication.changerImage(selectedImage);
        }

    }

}

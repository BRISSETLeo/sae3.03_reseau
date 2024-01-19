package client.controlers.message;

import java.io.File;

import client.vues.Message;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

public class InsererImage implements EventHandler<ActionEvent> {

    private final Message message;
    private final FileChooser fileChooser;

    public InsererImage(Message message) {
        this.message = message;
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        this.fileChooser.setTitle("Choisir une image");
    }

    @Override
    public void handle(ActionEvent event) {

        File selectedFile = this.fileChooser.showOpenDialog(this.message.getScene().getWindow());
        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            this.message.imageAjoute(selectedImage);
        }

    }

}

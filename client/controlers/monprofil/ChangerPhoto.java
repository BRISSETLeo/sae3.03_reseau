package client.controlers.monprofil;

import java.io.File;

import client.vues.MonProfil;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class ChangerPhoto implements EventHandler<MouseEvent> {

    private final MonProfil profil;
    private final FileChooser fileChooser;

    public ChangerPhoto(MonProfil profil) {
        this.profil = profil;
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        this.fileChooser.setTitle("Choisir une image");
    }

    @Override
    public void handle(MouseEvent event) {

        File selectedFile = this.fileChooser.showOpenDialog(this.profil.getScene().getWindow());
        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            this.profil.changerImage(selectedImage);
        }

    }

}

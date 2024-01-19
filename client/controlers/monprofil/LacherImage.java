package client.controlers.monprofil;

import java.io.File;

import client.vues.MonProfil;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;

public class LacherImage implements EventHandler<DragEvent> {

    private final MonProfil monProfil;

    public LacherImage(MonProfil monProfil) {
        this.monProfil = monProfil;
    }

    @Override
    public void handle(DragEvent event) {

        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles()) {
            File imageFile = dragboard.getFiles().get(0);
            String extension = imageFile.getName().substring(imageFile.getName().lastIndexOf(".") + 1);
            if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg")) {
                return;
            }
            Image image = new Image(imageFile.toURI().toString());
            this.monProfil.changerImage(image);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();

    }

}

package client.controle;

import java.io.File;

import client.graphisme.Profil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

public class ChoosePP implements EventHandler<ActionEvent> {

    private Profil profil;
    private FileChooser fileChooser;
    
    public ChoosePP(Profil profil){
        this.profil = profil;
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("Choisir une image");
    }

    @Override
    public void handle(ActionEvent event) {
        
        File selectedFile = fileChooser.showOpenDialog(profil.getScene().getWindow());
        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            this.profil.changerImage(selectedImage);
        }

    }

}

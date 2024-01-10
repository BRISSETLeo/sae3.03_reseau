package client.graphisme;

import java.io.ByteArrayInputStream;
import java.sql.Blob;

import caches.Compte;
import enums.CheminFONT;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class CompteBox extends HBox {
    
    public CompteBox(Compte compte) {
        super(10);

        Label pseudo = new Label(compte.getPseudo());
        pseudo.setFont(Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 20));
        
        ImageView photoProfil = new ImageView(new Image("./client/images/noPP.png"));
        photoProfil.setFitHeight(25);
        photoProfil.setFitWidth(25);
        photoProfil.setPreserveRatio(true);

        Blob image = compte.getImage();

        if(image != null){
            try{
                int blobLength = (int) image.length();
                byte[] bytes = image.getBytes(1, blobLength);
                photoProfil.setImage(new Image(new ByteArrayInputStream(bytes)));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        super.setAlignment(Pos.CENTER_LEFT);
        super.getChildren().addAll(photoProfil, pseudo);
        
    }

}

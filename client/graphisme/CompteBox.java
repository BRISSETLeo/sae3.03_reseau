package client.graphisme;

import java.io.ByteArrayInputStream;
import java.sql.Blob;

import caches.Compte;
import client.graphisme.affichage.ImageViewS;
import client.graphisme.affichage.LabelF;
import enums.CheminIMG;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CompteBox extends HBox {

    public CompteBox(Compte compte) {
        super(10);

        ImageView photoProfil = new ImageViewS(CheminIMG.NO_PP.getChemin());

        Blob image = compte.getImage();

        if (image != null) {
            try {
                int blobLength = (int) image.length();
                byte[] bytes = image.getBytes(1, blobLength);
                photoProfil.setImage(new Image(new ByteArrayInputStream(bytes)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.setAlignment(Pos.CENTER_LEFT);
        super.getChildren().addAll(photoProfil, new LabelF(compte.getPseudo()));

    }

}

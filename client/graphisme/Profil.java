package client.graphisme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

import caches.Compte;
import client.Main;
import client.controle.ChoosePP;
import client.controle.EnregistrerProfil;
import client.graphisme.affichage.LabelF;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Profil extends VBox {

    private Main main;
    private Circle circle;

    public Profil(Main main){
        this.main = main;
        this.circle = new Circle();
        this.circle.setRadius(100);
    }
    
    public void ajouterCompte(){
        Compte compte = this.main.getCompte();

        Button selectImageButton = new Button("Sélectionner une image");
        selectImageButton.setOnAction(new ChoosePP(this));

        Button enregistrer = new Button("Enregistrer les modifications");
        enregistrer.setOnAction(new EnregistrerProfil(this.main));

        this.circle.setFill(new ImagePattern(Main.blobToImage(this.main.getCompte().getImage())));

        super.getChildren().addAll(new LabelF(compte.getPseudo()), this.circle, selectImageButton, enregistrer);
    }

    public void changerImage(Image image){
        this.circle.setFill(new ImagePattern(image));
        try{
            this.main.getCompte().setImage(this.imageToBlob(image));
        }catch(IOException | SQLException e){
            e.printStackTrace();
        }
    }

    public Blob imageToBlob(Image image) throws IOException, SQLException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            InputStream is = getImageInputStream(image);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            byte[] imageData = baos.toByteArray();
            return new SerialBlob(imageData);
        }
    }

    private InputStream getImageInputStream(Image image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
        return new ByteArrayInputStream(out.toByteArray());
    }

}

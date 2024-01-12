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
import client.controle.CloseRight;
import client.controle.EnregistrerProfil;
import client.graphisme.affichage.ButtonG;
import client.graphisme.affichage.LabelF;
import enums.CheminCSS;
import enums.CheminIMG;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Profil extends VBox {

    private Main main;
    private Circle circle;
    private VBox content;
    private Compte compte;

    public Profil(Main main) {
        this.main = main;
        this.content = new VBox(20);
        this.circle = new Circle();
        this.circle.setRadius(100);
        ButtonG close = new ButtonG(CheminIMG.CLOSE.getChemin());
        close.setOnAction(new CloseRight(this.main));
        HBox hBox = new HBox(close);
        hBox.getStylesheets().add(CheminCSS.PROFIL.getChemin());
        ScrollPane scrollPane = new ScrollPane(this.content);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        super.getChildren().addAll(hBox, scrollPane);
    }

    public void afficherProfil(Compte compte, boolean isMe) {
        this.content.getChildren().clear();
        this.compte = compte;

        this.circle.setFill(new ImagePattern(Main.blobToImage(this.main.getCompte().getImage())));

        VBox profil = new VBox(20, new LabelF(compte.getPseudo()), this.circle);

        HBox informations = new HBox(10);

        VBox publications = new VBox(new Label(compte.getNbPublications()+""), new Label("Publication(s)"));
        VBox abonnes = new VBox(new Label(compte.getNbAbonnes()+""), new Label("Abonné(s)"));
        VBox abonnements = new VBox(new Label(compte.getNbAbonnements()+""), new Label("Abonnement(s)"));

        publications.setAlignment(Pos.CENTER);
        abonnes.setAlignment(Pos.CENTER);
        abonnements.setAlignment(Pos.CENTER);

        informations.getChildren().addAll(
            publications,
            abonnes,
            abonnements
        );

        informations.setAlignment(Pos.CENTER);

        profil.getChildren().add(informations);

        if(isMe){
            Button selectImageButton = new Button("Sélectionner une image");
            selectImageButton.setOnAction(new ChoosePP(this));

            Button enregistrer = new Button("Enregistrer les modifications");
            enregistrer.setOnAction(new EnregistrerProfil(this.main));
            profil.getChildren().addAll(selectImageButton, enregistrer);
        }

        profil.setAlignment(Pos.CENTER);

        this.content.getChildren().add(profil);
    }

    public void changerImage(Image image) {
        this.circle.setFill(new ImagePattern(image));
        try {
            this.main.getCompte().setImage(this.imageToBlob(image));
        } catch (IOException | SQLException e) {
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

    public Compte getPseudo() {
        return this.compte;
    }

}

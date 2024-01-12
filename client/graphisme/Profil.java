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

    private Label nbPublications;
    private Label nbAbonnes;
    private Label nbAbonnements;

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

        this.circle.setFill(new ImagePattern(Main.blobToImage(compte.getImage())));

        VBox profil = new VBox(20, new LabelF(compte.getPseudo()), this.circle);

        HBox informations = new HBox(10);

        this.nbPublications = new Label(compte.getNbPublications()+"");
        this.nbAbonnes = new Label(compte.getNbAbonnes()+"");
        this.nbAbonnements = new Label(compte.getNbAbonnements()+"");

        VBox publications = new VBox(this.nbPublications, new Label("Publication(s)"));
        VBox abonnes = new VBox(this.nbAbonnes, new Label("Abonné(s)"));
        VBox abonnements = new VBox(this.nbAbonnements, new Label("Abonnement(s)"));

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

    public void ajouterPublication(String pseudo){
        if(this.compte != null && this.compte.getPseudo().equals(pseudo)){
            this.nbPublications.setText((Integer.parseInt(this.nbPublications.getText())+1)+"");
        }
    }

    public void removePublication(String pseudo){
        if(this.compte != null && this.compte.getPseudo().equals(pseudo)){
            this.nbPublications.setText((Integer.parseInt(this.nbPublications.getText())-1)+"");
        }
    }
    
    public void ajouterAbonne(String pseudo){
        if(this.compte != null && this.compte.getPseudo().equals(pseudo)){
            this.nbAbonnes.setText((Integer.parseInt(this.nbAbonnes.getText())+1)+"");
        }
    }

    public void removeAbonne(String pseudo){
        if(this.compte != null && this.compte.getPseudo().equals(pseudo)){
            this.nbAbonnes.setText((Integer.parseInt(this.nbAbonnes.getText())-1)+"");
        }
    }

    public void ajouterAbonnement(String pseudo){
        if(this.compte != null && this.compte.getPseudo().equals(pseudo)){
            this.nbAbonnements.setText((Integer.parseInt(this.nbAbonnements.getText())+1)+"");
        }
    }

    public void removeAbonnement(String pseudo){
        if(this.compte != null && this.compte.getPseudo().equals(pseudo)){
            this.nbAbonnements.setText((Integer.parseInt(this.nbAbonnements.getText())-1)+"");
        }
    }

    public void changerImage(Image image) {
        this.circle.setFill(new ImagePattern(image));
        try {
            this.main.getCompte().setImage(this.imageToBlob(image));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateImage(String pseudo, Image image) {
        if(this.compte != null && this.compte.getPseudo().equals(pseudo))
            this.circle.setFill(new ImagePattern(image));
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

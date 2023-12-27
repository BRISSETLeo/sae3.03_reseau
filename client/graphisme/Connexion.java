package client.graphisme;

import java.util.Optional;

import client.controle.*;
import enums.*;
import client.Main;
import enums.CheminIMG;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Connexion extends HBox {

    private Main main;

    private TextField pseudoField;
    private TextField adresseField;

    public Connexion(Main main) {
        this.main = main;

        ImageView logo = new ImageView(CheminIMG.LOGO.getChemin());

        this.pseudoField = new TextField();
        this.pseudoField.setPromptText("Identifiant (Ex: Noah)");

        this.adresseField = new TextField();
        this.adresseField.setPromptText("Adresse IP (Ex:127.0.0.1)");

        String identifiant = this.main.demanderIdentifiant();
        if (identifiant != null) {
            String[] identifiants = identifiant.split(":");
            this.pseudoField.setText(identifiants[1]);
            this.adresseField.setText(identifiants[0]);
        }

        Button connexionButton = new Button("Se connecter");
        connexionButton.setOnAction(new ConnexionC(this.main, this));

        Font font = Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 30);
        this.pseudoField.setFont(font);
        this.adresseField.setFont(font);
        connexionButton.setFont(Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 50));

        super.getStylesheets().add(CheminCSS.PAGE_CONNEXION.getChemin());
        super.getChildren().addAll(logo, new VBox(50, this.pseudoField, this.adresseField, connexionButton));
    }

    public String getPseudo() {
        return this.pseudoField.getText();
    }

    public String getAdresse() {
        return this.adresseField.getText();
    }

    public void erreurDansAdresse() {
        this.adresseField.getStyleClass().add("erreur");
    }

    public void erreurDansPseudo() {
        this.pseudoField.getStyleClass().add("erreur");
    }

    public void demanderCreationDeCompte() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().getStylesheets().add(CheminCSS.ALERT.getChemin());
        alert.setTitle("Création de compte");
        alert.setHeaderText("Votre pseudo n'a pas été trouvé");
        alert.setContentText("Voulez-vous inscrire votre pseudo dans notre base de donnée ?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {

            this.main.creerCompte();

        } else {

            this.main.fermer();

        }
    }

}

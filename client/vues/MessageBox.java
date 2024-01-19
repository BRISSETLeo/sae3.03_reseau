package client.vues;

import client.Main;
import client.controlers.message.SupprimerMessage;
import client.controlers.monprofil.AfficherMonProfil;
import client.controlers.profil.AfficherProfil;
import client.utilitaire.IMAGE;
import client.vues.nodes.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MessageBox extends VBox {

    public MessageBox(Message pageMessage, caches.Message message, Main main) {

        VBox container = new VBox(5);

        ProfilBox imageProfilBox = new ProfilBox(message.getCompte(), 20);

        Label pseudo = new Label(message.getCompte().getPseudo());
        Label date = new Label(message.getDate().toString());

        pseudo.setWrapText(true);
        date.setWrapText(true);

        Label contenuMessage = new Label(message.getContenu());
        contenuMessage.setWrapText(true);

        boolean mettreDroite = true;

        if (pseudo.getText().equals(main.getMonCompte().getPseudo())) {
            imageProfilBox.setOnMouseClicked(new AfficherMonProfil(main));
            container.getStyleClass().add("mon-message");
            Button supprimer = new Button(IMAGE.CORBEILLE.getChemin(), 15);
            supprimer.setOnAction(new SupprimerMessage(main, message.getIdMessage()));
            container.getChildren().add(supprimer);
            super.getChildren().addAll(Main.createRegion(), container);
        } else {
            imageProfilBox
                    .setOnMouseClicked(new AfficherProfil(main, pseudo.getText()));
            container.getStyleClass().add("son-message");
            super.getChildren().addAll(container, Main.createRegion());
            mettreDroite = false;
        }

        Image image = message.getImage();
        ImageView imageView = null;

        if (image != null) {
            imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
        }

        container.getChildren().addAll(new HBox(imageProfilBox, Main.createRegion(),
                date), imageView != null ? imageView : new StackPane(),
                message.getVocal() != null ? new PatternVocal(pageMessage, message.getVocal(), mettreDroite)
                        : contenuMessage);

    }

}

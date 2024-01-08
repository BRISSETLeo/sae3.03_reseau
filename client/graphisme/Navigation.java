package client.graphisme;

import client.Main;
import client.controle.NewPublication;
import enums.CheminCSS;
import enums.CheminFONT;
import enums.CheminIMG;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Navigation extends VBox {

    public Navigation(Main main) {
        Button accueil = new Button("Accueil");
        Button notif = new Button("Notifications");
        Button msg = new Button("Messages");
        msg.setOnAction(new client.controle.Message(main));
        Button publi = new Button("Publication");
        publi.setOnAction(new NewPublication(main));

        Font font = Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 20);
        accueil.setFont(font);
        notif.setFont(font);
        msg.setFont(font);
        publi.setFont(font);

        ImageView accueilImg = new ImageView(CheminIMG.ACCUEIL.getChemin());
        ImageView notifImg = new ImageView(CheminIMG.NOTIFICATION.getChemin());
        ImageView msgImg = new ImageView(CheminIMG.MESSAGE.getChemin());
        ImageView publiImg = new ImageView(CheminIMG.PUBLICATION.getChemin());

        accueil.setGraphic(accueilImg);
        notif.setGraphic(notifImg);
        msg.setGraphic(msgImg);
        publi.setGraphic(publiImg);

        this.setSize(accueilImg, notifImg, msgImg, publiImg);

        super.getStylesheets().add(CheminCSS.NABIGATION.getChemin());
        super.getChildren().addAll(accueil, notif, msg, publi);
    }

    private void setSize(ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
        }
    }

}

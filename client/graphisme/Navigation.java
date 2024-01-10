package client.graphisme;

import client.Main;
import client.controle.NewPublication;
import client.graphisme.affichage.ButtonF;
import client.graphisme.affichage.ImageViewS;
import enums.CheminCSS;
import enums.CheminIMG;
import javafx.scene.layout.VBox;

public class Navigation extends VBox {

    public Navigation(Main main) {

        ButtonF accueil = new ButtonF("Accueil");
        accueil.setOnAction(new client.controle.Accueil(main));

        ButtonF notif = new ButtonF("Notifications");

        ButtonF msg = new ButtonF("Messages");
        msg.setOnAction(new client.controle.Message(main));

        ButtonF publi = new ButtonF("Publication");
        publi.setOnAction(new NewPublication(main));

        accueil.setGraphic(new ImageViewS(CheminIMG.ACCUEIL.getChemin()));
        notif.setGraphic(new ImageViewS(CheminIMG.NOTIFICATION.getChemin()));
        msg.setGraphic(new ImageViewS(CheminIMG.MESSAGE.getChemin()));
        publi.setGraphic(new ImageViewS(CheminIMG.PUBLICATION.getChemin()));

        super.getStylesheets().add(CheminCSS.NABIGATION.getChemin());
        super.getChildren().addAll(accueil, notif, msg, publi);
    }

}

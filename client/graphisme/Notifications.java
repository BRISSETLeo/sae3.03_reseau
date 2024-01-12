package client.graphisme;

import caches.Notification;
import client.Main;
import client.controle.CloseRight;
import client.graphisme.affichage.ButtonG;
import enums.CheminCSS;
import enums.CheminIMG;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Notifications extends VBox {

    private VBox content;

    public Notifications(Main main) {
        this.content = new VBox(5);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(this.content);

        ButtonG close = new ButtonG(CheminIMG.CLOSE.getChemin());
        close.setOnAction(new CloseRight(main));
        HBox hBox = new HBox(close);
        hBox.getStylesheets().add(CheminCSS.PROFIL.getChemin());

        super.getChildren().addAll(hBox, scrollPane);
    }

    public void ajouterNotification(Notification notification) {
        VBox contenant = new VBox();
        contenant.getChildren().add(new Label(notification.getIdNotification() + ""));
        contenant.getChildren().add(new Label(notification.getPseudo()));
        contenant.getChildren().add(new Label(notification.getPseudoNotif()));
        contenant.getChildren().add(new Label(notification.getType()));
        contenant.getChildren().add(new Label(notification.getId() + ""));
        contenant.getChildren().add(new Label(notification.getDate().toString()));
        contenant.getChildren().add(new Label(notification.isLu() + ""));
        this.content.getChildren().add(contenant);
    }

    public void supprimerNotification(Notification notification) {
    }

}

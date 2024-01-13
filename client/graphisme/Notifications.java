package client.graphisme;

import java.util.HashMap;
import java.util.Map;

import caches.Notification;
import client.Main;
import client.controle.CloseRight;
import client.controle.SupprimerNotification;
import client.graphisme.affichage.ButtonG;
import client.graphisme.affichage.ImageViewS;
import enums.CheminCSS;
import enums.CheminIMG;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Notifications extends VBox {

    private Main main;
    private VBox content;
    private Map<Integer, VBox> listNotifications;

    public Notifications(Main main) {
        this.main = main;
        this.content = new VBox(5);
        this.listNotifications = new HashMap<>();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setContent(this.content);

        ButtonG close = new ButtonG(CheminIMG.CLOSE.getChemin());
        close.setOnAction(new CloseRight(main));
        HBox hBox = new HBox(close);
        hBox.getStylesheets().add(CheminCSS.PROFIL.getChemin());

        super.getChildren().addAll(hBox, scrollPane);
        super.getStylesheets().add(CheminCSS.NOTIFICATION.getChemin());
    }

    public void ajouterNotification(Notification notification) {
        VBox contenant = new VBox();
        contenant.getStyleClass().add("notification-container");

        HBox messageBox = new HBox();

        Label messageLabel = new Label(
                "Vous avez un nouveau " + notification.getType() + " de " + notification.getPseudoNotif());
        messageLabel.setWrapText(true);
        messageLabel.getStyleClass().add("notification-label");

        // Ajouter votre ImageView pour la croix
        ImageView crossIcon = new ImageView();
        ButtonG fermer = new ButtonG(crossIcon = new ImageViewS(CheminIMG.CORBEILLE.getChemin()));
        fermer.setOnAction(new SupprimerNotification(this.main, notification.getId()));
        crossIcon.setFitWidth(16);
        crossIcon.setFitHeight(16);
        crossIcon.getStyleClass().add("close-icon");

        messageBox.getChildren().addAll(messageLabel, Main.createRegion(), fermer);
        messageBox.setAlignment(Pos.CENTER_LEFT); // Alignez le texte et l'icône à gauche dans le HBox

        Label dateLabel = new Label(notification.getDate().toString());
        dateLabel.getStyleClass().add("notification-date");

        contenant.getChildren().addAll(messageBox, dateLabel);
        this.content.getChildren().add(contenant);
        listNotifications.put(notification.getId(), contenant);
        System.out.println(listNotifications);
    }

    public void removeNotification(int id_notification) {
        this.content.getChildren().remove(listNotifications.get(id_notification));
        this.listNotifications.remove(id_notification);

    }

    public boolean demanderSupprimerPublication() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(CheminCSS.ALERT.getChemin());
        alert.setTitle("Supprimer la notification");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette notification ?");
        alert.setContentText("Cette action est irréversible.");
        return alert.showAndWait().get().getButtonData().isDefaultButton();
    }

}

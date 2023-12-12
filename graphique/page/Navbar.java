package graphique.page;

import graphique.controller.PageAccueil;
import graphique.controller.PagePublication;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class Navbar extends VBox {

    public Navbar() {

        super.getStyleClass().add("navBar");

        Button accueil = new Button("Accueil");
        accueil.setOnAction(new PageAccueil());
        Button notif = new Button("Notifications");
        Button msg = new Button("Messages");
        Button publi = new Button("Publication");
        publi.setOnAction(new PagePublication());

        ImageView accueilImg = new ImageView(new Image("graphique/images/accueil.png"));
        ImageView notifImg = new ImageView(new Image("graphique/images/notification.png"));
        ImageView msgImg = new ImageView(new Image("graphique/images/message.png"));
        ImageView publiImg = new ImageView(new Image("graphique/images/publication.png"));

        this.setSize(accueilImg, notifImg, msgImg, publiImg);
        this.setStyle(accueil, notif, msg, publi);

        accueil.setGraphic(accueilImg);
        notif.setGraphic(notifImg);
        msg.setGraphic(msgImg);
        publi.setGraphic(publiImg);

        super.getChildren().addAll(accueil, notif, msg, publi);

    }

    private void setSize(ImageView... imageViews) {
        for (ImageView imageView : imageViews) {
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
        }
    }

    private void setStyle(Button... buttons) {
        for (Button button : buttons) {
            button.getStyleClass().add("button");
        }
    }

}

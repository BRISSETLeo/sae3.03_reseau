package client.vues;

import client.Main;
import client.controlers.Deconnexion;
import client.controlers.accueil.AfficherAccueil;
import client.controlers.message.AfficherMessage;
import client.controlers.monprofil.AfficherMonProfil;
import client.controlers.publication.AfficherPublication;
import client.utilitaire.CSS;
import client.utilitaire.IMAGE;
import client.vues.nodes.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Navigation extends VBox {

    private final Main main;

    private final ProfilBox profilBox;

    public Navigation(Main main) {

        this.main = main;

        double size = 30;

        this.profilBox = new ProfilBox(this.main.getMonCompte(), size / 2);

        ImageView imageAccueil = new ImageView(IMAGE.ACCUEIL.getChemin(), size);
        Label accueil = new Label("Accueil");

        ImageView imageNotification = new ImageView(IMAGE.NOTIFICATION.getChemin(), size);
        Label notification = new Label("Notification");

        ImageView imageMessage = new ImageView(IMAGE.MESSAGE.getChemin(), size);
        Label message = new Label("Message");

        ImageView imagePublication = new ImageView(IMAGE.PUBLICATION.getChemin(), size);
        Label publication = new Label("Publication");

        ImageView imageDeconnecter = new ImageView(IMAGE.DECONNECTER.getChemin(), size);
        Label deconnecter = new Label("Déconnexion");

        HBox imageAccueilBox = new HBox(10, imageAccueil, accueil);
        HBox imageNotificationBox = new HBox(10, imageNotification, notification);
        HBox imageMessageBox = new HBox(10, imageMessage, message);
        HBox imagePublicationBox = new HBox(10, imagePublication, publication);
        HBox imageDeconnecterBox = new HBox(10, imageDeconnecter, deconnecter);

        this.profilBox.setOnMouseClicked(new AfficherMonProfil(this.main));
        imageAccueilBox.setOnMouseClicked(new AfficherAccueil(this.main));
        imagePublicationBox.setOnMouseClicked(new AfficherPublication(this.main));
        imageMessageBox.setOnMouseClicked(new AfficherMessage(this.main));
        imageDeconnecterBox.setOnMouseClicked(new Deconnexion(this.main));

        super.getChildren().addAll(this.profilBox, imageAccueilBox, imageNotificationBox, imageMessageBox,
                imagePublicationBox, Main.createRegion(), imageDeconnecterBox);
        super.getStylesheets().add(CSS.NAVIGATION.getChemin());
        super.getStyleClass().add("container");

    }

    public void init() {
        this.profilBox.init(this.main.getMonCompte());
    }

    public void changerImage() {
        this.profilBox.changerImage(this.main.getMonCompte().getPhoto());
    }
}

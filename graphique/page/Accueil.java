package graphique.page;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Accueil extends BorderPane {

    private ScrollPane scrollPane;

    public Accueil() {

        VBox leftBox = new VBox();

        Button accueil = new Button("Accueil");
        Button notif = new Button("Notifications");
        Button msg = new Button("Messages");
        Button publi = new Button("Publication");

        leftBox.getChildren().addAll(accueil, notif, msg, publi);

        this.scrollPane = new ScrollPane();

        super.setLeft(leftBox);

    }

}

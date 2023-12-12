package graphique.page;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TopBar extends HBox {

    private TextField recherche;

    public TopBar() {

        super.getStyleClass().add("topBar");

        ImageView sysX = new ImageView(new Image("graphique/images/sysx.png"));

        sysX.setFitWidth(100);
        sysX.setFitHeight(100);

        this.recherche = new TextField();
        this.recherche.setPromptText("Rechercher...");

        HBox hBox = new HBox(70, sysX, this.recherche);

        hBox.getStyleClass().add("containerRechercher");
        this.recherche.getStyleClass().add("recherche");

        super.getChildren().addAll(hBox);

    }

}

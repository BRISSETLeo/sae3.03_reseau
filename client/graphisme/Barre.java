package client.graphisme;

import enums.CheminCSS;
import enums.CheminFONT;
import enums.CheminIMG;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class Barre extends HBox {

    private TextField rechercheField;

    public Barre() {
        super(100);

        ImageView logo = new ImageView(CheminIMG.LOGO.getChemin());

        logo.setFitWidth(100);
        logo.setFitHeight(100);

        this.rechercheField = new TextField();
        this.rechercheField.setPromptText("Rechercher...");

        this.rechercheField.setFont(Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 30));

        super.getStyleClass().add("positionnement");
        super.getStylesheets().add(CheminCSS.BARRE.getChemin());
        super.getChildren().addAll(logo, this.rechercheField);
    }

}

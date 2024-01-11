package client.graphisme;

import client.Main;
import client.controle.ProfilC;
import enums.CheminCSS;
import enums.FontP;
import enums.CheminIMG;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class Barre extends HBox {

    private TextField rechercheField;
    private CompteBox compteBox;

    public Barre() {
        super(100);

        ImageView logo = new ImageView(CheminIMG.LOGO.getChemin());

        logo.setFitWidth(100);
        logo.setFitHeight(100);

        this.rechercheField = new TextField();
        this.rechercheField.setPromptText("Rechercher...");

        this.rechercheField.setFont(FontP.FONT_20.getFont());

        super.getStyleClass().add("positionnement");
        super.getStylesheets().add(CheminCSS.BARRE.getChemin());
        super.getChildren().addAll(logo, this.rechercheField);
    }

    public void ajouterCompte(Main main){
        this.compteBox = new CompteBox(main,main.getCompte());

        compteBox.setOnMouseClicked(new ProfilC(main));

        this.rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                compteBox.setVisible(false);
            } else {
                compteBox.setVisible(true);
            }
        });

        super.getChildren().add(compteBox);
    }

    public void modifierCompteBarre(Image image){
        this.compteBox.setPhotoProfil(image);
    }

}

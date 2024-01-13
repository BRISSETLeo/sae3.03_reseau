package client.graphisme;

import caches.Compte;
import client.Main;
import client.controle.ProfilC;
import client.graphisme.affichage.LabelF;
import enums.CheminCSS;
import enums.FontP;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class CompteBox extends HBox {

    private Circle circle;
    private LabelF pseudo;

    public CompteBox(Main main, Compte compte, boolean rightMessage) {
        super(10);

        this.circle = new Circle();
        this.circle.setRadius(20);

        this.circle.setFill(new ImagePattern(Main.blobToImage(compte.getImage())));

        super.setCursor(Cursor.HAND);
        super.setAlignment(Pos.CENTER_LEFT);
        this.pseudo = new LabelF(compte.getPseudo());
        VBox vBox = new VBox(this.pseudo);
        vBox.setAlignment(Pos.CENTER_LEFT);
        if (rightMessage) {
            super.getChildren().addAll(vBox, this.circle);
        } else {
            super.getChildren().addAll(this.circle, vBox);
        }
        super.getStylesheets().add(CheminCSS.COMPTEBOX.getChemin());
        super.setOnMouseClicked(new ProfilC(main, compte.getPseudo()));
        super.getStyleClass().add("compte-box");
    }

    public Image getPhotoProfil() {
        return ((ImagePattern) this.circle.getFill()).getImage();
    }

    public void setPhotoProfil(Image image) {
        this.circle.setFill(new ImagePattern(image));
    }

    public void changerFont() {
        this.pseudo.setFont(FontP.FONT_15.getFont());
        this.circle.setRadius(15);
    }

}

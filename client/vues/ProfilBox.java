package client.vues;

import caches.Compte;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ProfilBox extends HBox {

    private Compte compte;
    private final Circle circle;
    private final Label pseudo;

    public ProfilBox(Compte compte, double d) {

        this.circle = new Circle();
        this.circle.setRadius(d);

        this.pseudo = new Label();

        if (compte != null) {
            this.pseudo.setText(compte.getPseudo());
            this.circle.setFill(new ImagePattern(compte.getPhoto()));
        }

        super.getChildren().addAll(this.circle, this.pseudo);
        super.getStyleClass().add("image-profil");

    }

    public void changerImage(Image image) {
        this.circle.setFill(new ImagePattern(image));
    }

    public Circle getCircle() {
        return this.circle;
    }

    public void init(Compte compte) {
        this.compte = compte;
        this.pseudo.setText(compte.getPseudo());
        this.circle.setFill(new ImagePattern(compte.getPhoto()));
    }

    public Compte getCompte() {
        return this.compte;
    }

}

package client.vues;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caches.Compte;
import client.Main;
import client.controlers.monprofil.AfficherMonProfil;
import client.controlers.profil.AfficherProfil;
import client.controlers.recherche.RechercherCompte;
import client.utilitaire.CSS;
import client.utilitaire.IMAGE;
import client.vues.nodes.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Recherche extends VBox {

    private final Main main;

    private final TextField rechercheField;
    private final VBox container;
    private final Map<String, Circle> imagesDeProfils;

    public Recherche(Main main) {

        this.main = main;

        this.rechercheField = new TextField();
        this.rechercheField.setPromptText("Rechercher un profil...");
        this.rechercheField.setOnKeyPressed(new RechercherCompte(this.main, this));

        this.container = new VBox();

        this.imagesDeProfils = new HashMap<>();

        ScrollPane scrollPane = new ScrollPane(this.container);
        scrollPane.setFitToWidth(true);

        HBox rechercheBox = new HBox(new ImageView(IMAGE.RECHERCHER.getChemin(), 30), this.rechercheField);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        HBox.setHgrow(this.rechercheField, Priority.ALWAYS);

        super.getChildren().addAll(rechercheBox, scrollPane);
        super.getStylesheets().add(CSS.RECHERCHE.getChemin());

    }

    public TextField getRechercheField() {
        return this.rechercheField;
    }

    public void clear() {

        this.imagesDeProfils.clear();
        this.container.getChildren().clear();

    }

    public void afficherResultatDeLaRecherche(List<Compte> comptes) {

        this.clear();

        String monPseudo = this.main.getMonCompte().getPseudo();

        comptes.forEach(compte -> {

            Circle circle = new Circle();
            circle.setRadius(20);
            circle.setFill(new ImagePattern(compte.getPhoto()));

            this.imagesDeProfils.put(compte.getPseudo(), circle);

            HBox compteBox = new HBox(10, circle, new Label(compte.getPseudo()));
            if (compte.getPseudo().equals(monPseudo)) {
                compteBox.setOnMouseClicked(new AfficherMonProfil(this.main));
            } else {
                compteBox.setOnMouseClicked(new AfficherProfil(this.main, compte.getPseudo()));
            }

            this.container.getChildren().add(new HBox(compteBox));

        });

        if (comptes.isEmpty()) {

            Label aucunResultat = new Label("Aucun résultat n'a été trouvé pour " + this.rechercheField.getText());
            aucunResultat.setWrapText(true);

            this.container.getChildren()
                    .add(aucunResultat);

        }

    }

    public void changerImage(Compte compte) {

        Circle circle = this.imagesDeProfils.get(compte.getPseudo());

        if (circle != null) {
            circle.setFill(new ImagePattern(compte.getPhoto()));
        }

    }

}
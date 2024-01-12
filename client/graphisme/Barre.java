package client.graphisme;

import java.util.HashMap;
import java.util.Map;

import caches.Compte;
import client.Main;
import client.controle.Recherche;
import client.lexicographie.Trie;
import enums.CheminCSS;
import enums.CheminIMG;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class Barre extends StackPane {

    private HBox barre;
    private Label personneConnecte;

    private TextField rechercheField;
    private CompteBox compteBox;

    private Trie trie;
    private Map<String, Compte> comptes;

    public Barre(Main main) {
        this.barre = new HBox();
        this.personneConnecte = new Label();
        this.trie = new Trie();
        this.comptes = new HashMap<>();

        ImageView logo = new ImageView(CheminIMG.LOGO.getChemin());

        logo.setFitWidth(100);
        logo.setFitHeight(100);

        this.rechercheField = new TextField();
        this.rechercheField.setPromptText("Rechercher...");
        this.rechercheField.setOnKeyReleased(new Recherche(main));

        this.barre.getStyleClass().add("positionnement");
        this.barre.getChildren().addAll(logo, Main.createRegion(), this.rechercheField, Main.createRegion());

        this.personneConnecte.getStylesheets().add(CheminCSS.POPUP.getChemin());

        super.setAlignment(Pos.TOP_LEFT);
        super.getStylesheets().add(CheminCSS.BARRE.getChemin());
        super.getChildren().addAll(this.barre, this.personneConnecte);
    }

    public void ajouterCompte(Main main) {
        this.compteBox = new CompteBox(main, main.getCompte());

        this.rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 0) {
                compteBox.setVisible(false);
            } else {
                compteBox.setVisible(true);
            }
        });

        this.barre.getChildren().add(compteBox);
    }

    public void modifierCompteBarre(Image image) {
        this.compteBox.setPhotoProfil(image);
    }

    public void showNotification(String pseudo) {

        this.personneConnecte.setText(pseudo + " s'est connecté");
        this.personneConnecte.setTranslateX(-1000);
        this.personneConnecte.getStyleClass().add("personne-connecte");
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), this.personneConnecte);
        transition.setToX(0);
        transition.setOnFinished(event -> {
            new Thread(
                    () -> {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        TranslateTransition transition2 = new TranslateTransition(Duration.seconds(1),
                                this.personneConnecte);
                        transition2.setToX(-1000);
                        transition2.setOnFinished(e2 -> {
                            this.personneConnecte.setTranslateX(0);
                            this.personneConnecte.setText("");
                            this.personneConnecte.getStyleClass().clear();
                        });
                        transition2.play();
                    }).start();
        });
        transition.play();

    }

    public void insertLexicographique(Compte compte) {
        this.trie.insert(compte.getPseudo());
        this.comptes.put(compte.getPseudo().toLowerCase(), compte);
    }

    public String getResultat() {
        return this.rechercheField.getText();
    }

    public Trie getTrie() {
        return this.trie;
    }

    public void clearRecherche() {
        this.rechercheField.clear();
    }

    public Map<String, Compte> getComptes() {
        return this.comptes;
    }

}

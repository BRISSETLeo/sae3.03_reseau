package client.vues;

import caches.Compte;
import client.Main;
import client.controlers.monprofil.ChangerPhoto;
import client.controlers.monprofil.GlisserImage;
import client.controlers.monprofil.LacherImage;
import client.controlers.monprofil.Sauvegarder;
import client.utilitaire.CSS;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MonProfil extends VBox {

    private final Main main;

    private final ProfilBox profilBox;
    private final Label pseudoLabel;
    private final Label nbPublications;
    private final Label nbAbonnes;
    private final Label nbAbonnements;

    public MonProfil(Main main) {

        this.main = main;

        this.profilBox = new ProfilBox(this.main.getMonCompte(), 70);

        this.pseudoLabel = new Label();
        this.pseudoLabel.getStyleClass().add("pseudo");

        this.nbPublications = new Label();
        this.nbAbonnes = new Label();
        this.nbAbonnements = new Label();

        this.profilBox.getCircle().setOnMouseClicked(new ChangerPhoto(this));
        this.profilBox.getCircle().setOnDragOver(new GlisserImage(this));
        this.profilBox.getCircle().setOnDragDropped(new LacherImage(this));

        VBox nbPublicationsBox = new VBox(5, this.nbPublications, new Label("Publication(s)"));
        VBox nbAbonnesBox = new VBox(5, this.nbAbonnes, new Label("Abonné(s)"));
        VBox nbAbonnementsBox = new VBox(5, this.nbAbonnements, new Label("Abonnement(s)"));

        Tooltip changerImage = new Tooltip("Changer l'image de profil");
        changerImage.setShowDelay(Duration.seconds(0));
        changerImage.setHideDelay(Duration.seconds(0));
        Tooltip.install(this.profilBox.getCircle(), changerImage);

        Button sauvegarder = new Button("Sauvegarder les modifications");
        sauvegarder.setOnAction(new Sauvegarder(this.main));

        super.getChildren()
                .addAll(new VBox(20, this.profilBox.getCircle(), this.pseudoLabel,
                        new HBox(20, nbPublicationsBox, nbAbonnesBox, nbAbonnementsBox),
                        sauvegarder));
        super.getStylesheets().add(CSS.MON_PROFIL.getChemin());

    }

    public void init() {
        Compte compte = this.main.getMonCompte();
        this.profilBox.changerImage(compte.getPhoto());
        this.pseudoLabel.setText(compte.getPseudo());
        this.nbPublications.setText(String.valueOf(compte.getNbPublications()));
        this.nbAbonnes.setText(String.valueOf(compte.getNbAbonnes()));
        this.nbAbonnements.setText(String.valueOf(compte.getNbAbonnements()));
    }

    public void changerImage(Image image) {
        this.profilBox.changerImage(image);
        this.main.getMonCompte().setPhoto(this.main.imageToByteArray(image));
    }

    public void mettreAJourMonProfilAbonnements(int nbAbonnements) {
        this.nbAbonnements.setText(String.valueOf(nbAbonnements));
    }

    public void mettreAJourMonProfilAbonnes(String pseudo2, int nbAbonnes) {
        if (this.pseudoLabel.getText().equals(pseudo2)) {
            this.nbAbonnes.setText(String.valueOf(nbAbonnes));
        }
    }

    public ProfilBox getProfilBox() {
        return this.profilBox;
    }

}

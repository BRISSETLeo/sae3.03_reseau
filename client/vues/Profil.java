package client.vues;

import caches.Compte;
import client.Main;
import client.controlers.profil.Abonne;
import client.controlers.profil.EcrireNewMessage;
import client.utilitaire.CSS;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Profil extends VBox {

    private final Main main;

    private final ProfilBox profilBox;
    private final Label pseudoLabel;
    private final Label nbPublications;
    private final Label nbAbonnes;
    private final Label nbAbonnements;
    private final Button ecrireMessage;

    private final Button suivre;

    public Profil(Main main) {

        this.main = main;

        this.profilBox = new ProfilBox(null, 70);

        this.pseudoLabel = new Label();
        this.pseudoLabel.getStyleClass().add("pseudo");

        this.nbPublications = new Label();
        this.nbAbonnes = new Label();
        this.nbAbonnements = new Label();

        VBox nbPublicationsBox = new VBox(5, this.nbPublications, new Label("Publication(s)"));
        VBox nbAbonnesBox = new VBox(5, this.nbAbonnes, new Label("Abonné(s)"));
        VBox nbAbonnementsBox = new VBox(5, this.nbAbonnements, new Label("Abonnement(s)"));

        this.suivre = new Button();
        this.ecrireMessage = new Button("Ecrire un message");

        super.getChildren()
                .addAll(new VBox(20, this.profilBox.getCircle(), this.pseudoLabel,
                        new HBox(20, nbPublicationsBox, nbAbonnesBox, nbAbonnementsBox),
                        this.suivre, this.ecrireMessage));
        super.getStylesheets().add(CSS.MON_PROFIL.getChemin());

    }

    public void init(Compte compte) {
        this.profilBox.init(compte);
        this.pseudoLabel.setText(compte.getPseudo());
        this.nbPublications.setText(String.valueOf(compte.getNbPublications()));
        this.nbAbonnes.setText(String.valueOf(compte.getNbAbonnes()));
        this.nbAbonnements.setText(String.valueOf(compte.getNbAbonnements()));
        this.ecrireMessage.setOnAction(new EcrireNewMessage(this.main, this.pseudoLabel.getText()));
        if (compte.estCeQueJeLeSuis()) {
            this.suivre.setText("Ne plus suivre");
        } else {
            this.suivre.setText("Suivre");
        }
        this.suivre.setOnAction(new Abonne(this.main, this, compte.getPseudo()));
    }

    public void mettreAJourLeSuiviProfil(int nbAbonnes, boolean estCeQueJeLeSuis) {

        this.mettreAJourAbonnes(this.pseudoLabel.getText(), nbAbonnes);

        if (estCeQueJeLeSuis) {
            this.suivre.setText("Ne plus suivre");
        } else {
            this.suivre.setText("Suivre");
        }

    }

    public void mettreAJourAbonnes(String pseudo, int nbAbonnes) {
        if (this.pseudoLabel.getText().equals(pseudo)) {
            this.nbAbonnes.setText(String.valueOf(nbAbonnes));
        }
    }

    public void mettreAJourProfilAbonnements(String pseudo, int nbAbonnements) {
        if (this.pseudoLabel.getText().equals(pseudo)) {
            this.nbAbonnements.setText(String.valueOf(nbAbonnements));
        }
    }

    public Button getSuivre() {
        return this.suivre;
    }

}

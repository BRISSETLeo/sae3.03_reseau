package client.vues;

import java.sql.Timestamp;

import caches.Compte;
import caches.Publication;
import client.Main;
import client.controlers.monprofil.AfficherMonProfil;
import client.controlers.profil.AfficherProfil;
import client.controlers.publication.LikePublication;
import client.controlers.publication.SupprimerPublication;
import client.utilitaire.CSS;
import client.utilitaire.IMAGE;
import client.vues.nodes.Button;
import client.vues.nodes.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class PublicationVue extends VBox {

    private final int idPublication;

    private final Accueil accueil;
    private final Main main;

    private final ImageView likeView;
    private final Label nbLikeLabel;
    private final Circle circle;

    private final String pseudo;

    private final Timestamp datePublication;

    public PublicationVue(Accueil accueil, Main main, Publication publication) {

        super(10);

        this.idPublication = publication.getIdPublication();
        this.pseudo = publication.getCompte().getPseudo();
        this.datePublication = publication.getDatePublication();

        this.accueil = accueil;
        this.main = main;
        this.nbLikeLabel = new Label();
        this.nbLikeLabel.setWrapText(true);

        this.likeView = new client.vues.nodes.ImageView(15);

        Compte compte = publication.getCompte();

        this.circle = new Circle();
        this.circle.setRadius(15);
        this.circle.setFill(new ImagePattern(compte.getPhoto()));

        HBox compteBox = new HBox(10, circle, new Label(compte.getPseudo()));
        compteBox.getStyleClass().add("compte-box");
        HBox suppressionBox = new HBox(10, compteBox, Main.createRegion());
        if (compte.getPseudo().equals(main.getMonCompte().getPseudo())) {
            compteBox.setOnMouseClicked(new AfficherMonProfil(this.main));
            Button suppressionButton = new Button(IMAGE.CORBEILLE.getChemin(), 18);
            suppressionBox.getChildren().add(suppressionButton);
            suppressionButton.setOnAction(new SupprimerPublication(this.main, this.idPublication));
        } else {
            compteBox.setOnMouseClicked(new AfficherProfil(this.main, compte.getPseudo()));
        }

        Label contenu = new Label(publication.getIdPublication() + " ) " + publication.getContenu());
        contenu.setWrapText(true);

        Label datePublication = new Label(publication.getDatePublication().toString());
        datePublication.setWrapText(true);

        ImageView imageView = null;

        if (publication.getImageBytes() != null) {
            imageView = new ImageView(publication.getImage(), 150, 100);
        }

        super.getChildren().addAll(suppressionBox, contenu);
        if (imageView != null)
            super.getChildren().add(imageView);
        super.getChildren().addAll(new HBox(datePublication, Main.createRegion(),
                this.nbLikeBox(publication.getNbLike(), publication.estCeQueJaiLike())));
        super.getStylesheets().add(CSS.PUBLICATION_VUE.getChemin());
        super.getStyleClass().add("container");

    }

    private HBox nbLikeBox(int nbLike, boolean hasLike) {
        HBox nbLikeBox = new HBox(5);
        nbLikeBox.setOnMouseClicked(new LikePublication(this, this.accueil, this.main));
        nbLikeBox.getStyleClass().add("nb-like-box");
        this.nbLikeLabel.setText(String.valueOf(nbLike));

        if (hasLike) {
            likeView.setImage(this.accueil.getLikeImg());
        } else {
            likeView.setImage(this.accueil.getUnlikeImg());
        }

        nbLikeBox.getChildren().addAll(this.nbLikeLabel, this.likeView);
        return nbLikeBox;
    }

    public void mettreAJourLeLike(int nbLike, boolean jaiLike, boolean hasLike) {
        this.nbLikeLabel.setText(String.valueOf(nbLike));
        if (jaiLike) {
            if (hasLike) {
                likeView.setImage(this.accueil.getLikeImg());
            } else {
                likeView.setImage(this.accueil.getUnlikeImg());
            }
        }
    }

    public void mettreAJourPhotoProfil(Compte compte) {
        this.circle.setFill(new ImagePattern(compte.getPhoto()));
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public ImageView getLikeView() {
        return this.likeView;
    }

    public int getIdPublication() {
        return this.idPublication;
    }

    public Timestamp getDatePublication() {
        return this.datePublication;
    }

}

package client.vues;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caches.Compte;
import caches.Publication;
import client.Main;
import client.utilitaire.CSS;
import client.utilitaire.IMAGE;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Accueil extends VBox {

    private final Main main;

    private final SplitPane splitPane;
    private final ScrollPane scrollPane;
    private final VBox container;

    private final Recherche pageDeRecherche;

    private final Image likeImg;
    private final Image unlikeImg;

    private final Map<Integer, PublicationVue> publications;

    private int nbDisplay;

    public Accueil(Main main) {

        this.main = main;

        this.likeImg = new Image(IMAGE.LIKE.getChemin());
        this.unlikeImg = new Image(IMAGE.UNLIKE.getChemin());

        this.publications = new HashMap<>();

        this.nbDisplay = 10;

        this.splitPane = new SplitPane();
        this.container = new VBox(15);

        this.pageDeRecherche = new Recherche(this.main);

        this.scrollPane = new ScrollPane(this.container);
        this.scrollPane.setFitToWidth(true);

        this.splitPane.getItems().addAll(this.scrollPane, this.pageDeRecherche);
        this.splitPane.setDividerPositions(0.7);

        super.getChildren().add(this.splitPane);
        VBox.setVgrow(this.splitPane, Priority.ALWAYS);
        super.getStylesheets().add(CSS.PUBLICATION.getChemin());

    }

    public void setDisplayPublications(List<Publication> publications) {

        this.container.getChildren().clear();
        this.publications.clear();

        int ind = 0;

        for (Publication publication : publications) {
            PublicationVue publicationVue = new PublicationVue(this, this.main, publication);
            this.container.getChildren().add(publicationVue);
            this.publications.put(publication.getIdPublication(), publicationVue);
            if (++ind <= this.nbDisplay) {
                publicationVue.setVisible(true);
                publicationVue.setManaged(true);
            } else {
                publicationVue.setVisible(false);
                publicationVue.setManaged(false);
            }
        }

        Button afficherPlus = new Button("Afficher plus de publications");
        afficherPlus.setOnAction(event -> this.displayMorePublications());
        this.container.getChildren().add(afficherPlus);

        if (ind <= this.nbDisplay) {
            afficherPlus.setVisible(false);
            afficherPlus.setManaged(false);
        } else {
            afficherPlus.setVisible(true);
            afficherPlus.setManaged(true);
        }

    }

    public void displayMorePublications() {
        this.nbDisplay += 10;
        int ind = 0;
        for (PublicationVue publicationVue : this.publications.values()) {
            if (!publicationVue.isVisible() && ++ind <= this.nbDisplay) {
                publicationVue.setVisible(true);
                publicationVue.setManaged(true);
            }
        }
        Button afficherPlus = (Button) this.container.getChildren().get(this.container.getChildren().size() - 1);
        if (ind <= this.nbDisplay) {
            afficherPlus.setVisible(false);
            afficherPlus.setManaged(false);
        } else {
            afficherPlus.setVisible(true);
            afficherPlus.setManaged(true);
        }
    }

    public void mettreAJourLeLike(int idPublication, int nbLike, boolean jaiLike, boolean hasLike) {
        this.publications.get(idPublication).mettreAJourLeLike(nbLike, jaiLike, hasLike);
    }

    public void mettreAJourProfilPublication(Compte compte) {
        this.publications.values().forEach(publicationVue -> {
            if (publicationVue.getPseudo().equals(compte.getPseudo())) {
                publicationVue.mettreAJourPhotoProfil(compte);
            }
        });
    }

    public Recherche getPageDeRecherche() {
        return this.pageDeRecherche;
    }

    public Image getLikeImg() {
        return this.likeImg;
    }

    public Image getUnlikeImg() {
        return this.unlikeImg;
    }

    public void ajouterPublications(List<Publication> publications) {
        for (Publication publication : publications) {
            Timestamp publicationDate = publication.getDatePublication();
            int insertIndex = binarySearchInsertIndex(this.container.getChildren(), publicationDate);

            PublicationVue publicationVue = new PublicationVue(this, this.main, publication);
            this.container.getChildren().add(insertIndex, publicationVue);
            this.publications.put(publication.getIdPublication(), publicationVue);
            publicationVue.setVisible(false);
            publicationVue.setManaged(false);
        }
        this.displayLimitedPublication();
    }

    public void removePublication(int idPublication) {
        this.container.getChildren().removeIf(publicationVue -> publicationVue instanceof PublicationVue
                && ((PublicationVue) publicationVue).getIdPublication() == idPublication);
        this.publications.remove(idPublication);
        this.displayLimitedPublication();
    }

    public void removePublications(String pseudo) {
        this.container.getChildren().removeIf(publicationVue -> publicationVue instanceof PublicationVue
                && ((PublicationVue) publicationVue).getPseudo().equals(pseudo));
        this.publications.values().removeIf(publicationVue -> publicationVue.getPseudo().equals(pseudo));
        this.displayLimitedPublication();
    }

    private void displayLimitedPublication() {
        int ind = 0;
        for (Node publicationVue : this.container.getChildren()) {
            if (publicationVue instanceof PublicationVue) {
                if (++ind > this.nbDisplay) {
                    publicationVue.setVisible(false);
                    publicationVue.setManaged(false);
                } else {
                    publicationVue.setVisible(true);
                    publicationVue.setManaged(true);
                }
            }
        }
        Button afficherPlus = (Button) this.container.getChildren().get(this.container.getChildren().size() - 1);
        if (ind <= this.nbDisplay) {
            afficherPlus.setVisible(false);
            afficherPlus.setManaged(false);
        } else {
            afficherPlus.setVisible(true);
            afficherPlus.setManaged(true);
        }
        this.redimenssionerScroll();
    }

    public void redimenssionerScroll() {
        this.scrollPane.applyCss();
        this.scrollPane.layout();
        this.scrollPane.requestLayout();
        double vValue = this.scrollPane.getVvalue();
        Platform.runLater(() -> {
            this.scrollPane.setVvalue(vValue);
        });
    }

    private int binarySearchInsertIndex(ObservableList<Node> nodes, Timestamp targetDate) {
        int low = 0;
        int high = nodes.size() - 2;

        while (low <= high) {
            int mid = (low + high) / 2;
            PublicationVue midPublication = (PublicationVue) nodes.get(mid);
            Timestamp midDate = midPublication.getDatePublication();

            if (midDate.equals(targetDate)) {
                return mid;
            } else if (midDate.after(targetDate)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return low;
    }

}

package client.graphisme;

import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caches.Commentaire;
import caches.Publication;
import client.Main;
import client.controle.LikeButton;
import client.controle.SupprimerPublication;
import client.controle.UnlikeButton;
import client.graphisme.affichage.ButtonG;
import client.graphisme.affichage.ImageViewS;
import client.graphisme.affichage.LabelF;
import enums.CheminCSS;
import enums.CheminIMG;
import enums.FontP;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Accueil extends VBox {

    private Main main;
    private VBox contenant;
    private Map<Integer, VBox> publications;
    private Map<Integer, VBox> commentaires;
    private Map<String, List<CompteBox>> comptesBoxs;
    private List<HBox> vocalBox;

    private Image likePubli;
    private Image unlikePubli;

    public Accueil(Main main) {
        this.main = main;
        this.contenant = new VBox();
        this.publications = new HashMap<>();
        this.commentaires = new HashMap<>();
        this.vocalBox = new ArrayList<>();
        this.likePubli = new ImageViewS(CheminIMG.LIKE.getChemin()).getImage();
        this.unlikePubli = new ImageViewS(CheminIMG.UNLIKE.getChemin()).getImage();
        this.comptesBoxs = new HashMap<>();

        ScrollPane scrollPane = new ScrollPane(this.contenant);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        super.getStylesheets().add(CheminCSS.ACCUEIL.getChemin());
        super.getChildren().add(scrollPane);
    }

    public void ajouterPublication(Publication publication, boolean hasNewPublication) {
        VBox container = new VBox();
        container.getStyleClass().add("container");

        ButtonG likeButton = new ButtonG();

        this.mettreAJourLikeButton(likeButton, publication);

        Label contentLabel = new Label(publication.getContent());
        contentLabel.setFont(FontP.FONT_15.getFont());
        contentLabel.setWrapText(true);

        CompteBox compteBox = new CompteBox(this.main,publication.getCompte());

        if(!this.comptesBoxs.containsKey(publication.getCompte().getPseudo())){
            this.comptesBoxs.put(publication.getCompte().getPseudo(), new ArrayList<>());
        }

        List<CompteBox> compteBoxs = this.comptesBoxs.get(publication.getCompte().getPseudo());
        compteBoxs.add(compteBox);
        this.comptesBoxs.put(publication.getCompte().getPseudo(), compteBoxs);

        container.getChildren().addAll(new HBox(compteBox, Main.createRegion(),
                new LabelF(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(publication.getDate()))), 
                contentLabel);

        this.setupVocal(publication.getVocal(), container);

        HBox likeBox = new HBox(Main.createRegion(), new LabelF(publication.getLikes() + ""), likeButton);
        likeBox.setAlignment(Pos.CENTER_LEFT);

        if (publication.getCompte().getPseudo().equals(this.main.getPseudo())) {
            Button supprimerPublication = new Button("Supprimer la publication");
            supprimerPublication.setOnAction(new SupprimerPublication(this.main, publication.getIdPublication()));
            likeBox.getChildren().add(0, supprimerPublication);
        }

        container.getChildren().add(likeBox);

        if (hasNewPublication) {
            container.setTranslateX(-1000);
            this.contenant.getChildren().add(0, container);
            TranslateTransition transition = new TranslateTransition(Duration.seconds(1), container);
            transition.setToX(0);
            transition.play();
        } else
            this.contenant.getChildren().add(container);

        this.publications.put(publication.getIdPublication(), container);

        publication.getCommentaires().forEach(c -> this.ajouterCommentaire(c));
    }

    public void removePublication(int idPublication) {
        VBox container = this.publications.get(idPublication);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(1), container);
        transition.setToX(-1000);
        transition.setOnFinished(e -> {
            this.contenant.getChildren().remove(container);
            this.retirerCompteBoxDeListeAffichage(idPublication);
            this.publications.remove(idPublication);
        });
        transition.play();
    }

    public void retirerCompteBoxDeListeAffichage(int idPublication){
        CompteBox compteBox = (CompteBox) ((HBox) this.publications.get(idPublication).getChildren().get(0)).getChildren().get(0);
        for (String pseudo : this.comptesBoxs.keySet()) {
            if(this.comptesBoxs.get(pseudo).contains(compteBox)){
                this.comptesBoxs.get(pseudo).remove(compteBox);
            }
        }
    }

    private void setupVocal(Blob vocal, VBox container) {
        if (vocal != null) {
            try {
                int blobLength = (int) vocal.length();
                if (blobLength > 1) {
                    byte[] bytes = vocal.getBytes(1, blobLength);
                    HBox hBox = new HBox(2);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    for (Double amplitude : this.main.playAudio(bytes)) {
                        this.drawBar(hBox, amplitude);
                    }
                    this.vocalBox.add(hBox);
                    container.getChildren().add(hBox);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawBar(HBox hBox, double amplitude) {
        double barHeight = amplitude * 80;
        if (amplitude == 0 || barHeight < 1)
            return;
        double barWidth = 3;
        Rectangle bar = new Rectangle(barWidth, barHeight);
        bar.getStyleClass().add("black-rectangle");
        hBox.getChildren().add(bar);
    }

    private void ajouterCommentaire(Commentaire commentaire) {
        VBox container = new VBox();
        this.commentaires.put(commentaire.getIdCommentaire(), container);
    }

    private void mettreAJourLikeButton(ButtonG likeButton, Publication publication) {
        int idPublication = publication.getIdPublication();
        if (publication.isCallerIsLiker())
            this.mettreLikeButtonALike(likeButton, idPublication);
        else
            this.mettreLikeButtonAUnlike(likeButton, idPublication);
    }

    private void mettreLikeButtonAUnlike(ButtonG likeButton, int idPublication) {
        likeButton.setOnAction(new LikeButton(this.main, idPublication));
        likeButton.setGraphic(this.unlikePubli);
    }

    private void mettreLikeButtonALike(ButtonG likeButton, int idPublication) {
        likeButton.setOnAction(new UnlikeButton(this.main, idPublication));
        likeButton.setGraphic(this.likePubli);
    }

    public void ajouterLike(int idPublication, int like, boolean isMe) {
        ButtonG likeButton = this.mettreAJourLike(this.recupererLikeBox(idPublication), like, idPublication, isMe);
        if(isMe)
            this.mettreLikeButtonALike(likeButton, idPublication);
    }

    public void removeLike(int idPublication, int like, boolean isMe) {
        ButtonG likeButton = this.mettreAJourLike(this.recupererLikeBox(idPublication), like, idPublication, isMe);
        if(isMe)
            this.mettreLikeButtonAUnlike(likeButton, idPublication);
    }

    public void modifierComptePublications(String pseudo, Image image){
        if(this.comptesBoxs.containsKey(pseudo)){
            for (CompteBox compteBox : this.comptesBoxs.get(pseudo)) {
                compteBox.setPhotoProfil(image);   
            }
        }
    }

    private HBox recupererLikeBox(int idPublication) {
        VBox container = this.publications.get(idPublication);
        return (HBox) container.getChildren().get(container.getChildren().size() - 1);
    }

    private ButtonG mettreAJourLike(HBox likeBox, int like, int idPublication, boolean isMe) {
        int deb = 1;
        if (likeBox.getChildren().size() == 4)
            deb = 2;
        LabelF likeLabel = (LabelF) likeBox.getChildren().get(deb);
        likeLabel.setText(like + "");
        return (ButtonG) likeBox.getChildren().get(deb + 1);
    }

    public boolean demanderSupprimerPublication(int idPublication) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(CheminCSS.ALERT.getChemin());
        alert.setTitle("Supprimer la publication");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette publication ?");
        alert.setContentText("Cette action est irréversible.");
        return alert.showAndWait().get().getButtonData().isDefaultButton();
    }

}

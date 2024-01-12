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
import client.controle.JouerSon;
import client.controle.LikeButton;
import client.controle.PauseSon;
import client.controle.StopperSon;
import client.controle.SupprimerPublication;
import client.controle.UnlikeButton;
import client.controle.Unpause;
import client.graphisme.affichage.ButtonG;
import client.graphisme.affichage.ImageViewS;
import client.graphisme.affichage.LabelF;
import enums.CheminCSS;
import enums.CheminIMG;
import enums.FontP;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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

    private Image likePubli;
    private Image unlikePubli;

    private Map<Integer, byte[]> vocaux;
    private Map<byte[], HBox> vocalBoxs;
    private Map<byte[], ButtonG> playPauseButtons;
    private Map<byte[], JouerSon> jouerSons;
    private Map<byte[], Unpause> unpauseSons;
    private Map<byte[], PauseSon> pauseSons;

    private Image playImg;
    private Image pauseImg;

    public Accueil(Main main) {
        this.main = main;
        this.contenant = new VBox();
        this.publications = new HashMap<>();
        this.commentaires = new HashMap<>();
        this.likePubli = new ImageViewS(CheminIMG.LIKE.getChemin()).getImage();
        this.unlikePubli = new ImageViewS(CheminIMG.UNLIKE.getChemin()).getImage();
        this.comptesBoxs = new HashMap<>();
        this.vocalBoxs = new HashMap<>();
        this.playPauseButtons = new HashMap<>();
        this.playImg = new ImageViewS(CheminIMG.PLAY.getChemin()).getImage();
        this.pauseImg = new ImageViewS(CheminIMG.PAUSE.getChemin()).getImage();
        this.vocaux = new HashMap<>();
        this.jouerSons = new HashMap<>();
        this.unpauseSons = new HashMap<>();
        this.pauseSons = new HashMap<>();

        ScrollPane scrollPane = new ScrollPane(this.contenant);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
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

        CompteBox compteBox = new CompteBox(this.main, publication.getCompte());

        if (!this.comptesBoxs.containsKey(publication.getCompte().getPseudo())) {
            this.comptesBoxs.put(publication.getCompte().getPseudo(), new ArrayList<>());
        }

        List<CompteBox> compteBoxs = this.comptesBoxs.get(publication.getCompte().getPseudo());
        compteBoxs.add(compteBox);
        this.comptesBoxs.put(publication.getCompte().getPseudo(), compteBoxs);

        container.getChildren().addAll(new HBox(compteBox, Main.createRegion(),
                new Label(new SimpleDateFormat("dd/MM/YYYY HH:mm:ss").format(publication.getDate()))),
                contentLabel);

        this.setupVocal(publication.getVocal(), container, publication.getIdPublication());

        HBox likeBox = new HBox(Main.createRegion(), new LabelF(publication.getLikes() + ""), likeButton);
        likeBox.setAlignment(Pos.CENTER_LEFT);

        if (publication.getCompte().getPseudo().equals(this.main.getPseudo())) {
            ButtonG supprimerPublication = new ButtonG(new ImageViewS(CheminIMG.CORBEILLE.getChemin()));
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
            if (this.vocaux.containsKey(idPublication)) {
                byte[] vocal = this.vocaux.get(idPublication);
                if (vocal != null) {
                    this.main.arreterSon(vocal);
                    this.vocalBoxs.remove(vocal);
                    this.playPauseButtons.remove(vocal);
                    this.jouerSons.remove(vocal);
                    this.pauseSons.remove(vocal);
                    this.unpauseSons.remove(vocal);
                }
            }
            this.vocaux.remove(idPublication);
            this.publications.remove(idPublication);
        });
        transition.play();
    }

    public void retirerCompteBoxDeListeAffichage(int idPublication) {
        CompteBox compteBox = (CompteBox) ((HBox) this.publications.get(idPublication).getChildren().get(0))
                .getChildren().get(0);
        for (String pseudo : this.comptesBoxs.keySet()) {
            if (this.comptesBoxs.get(pseudo).contains(compteBox)) {
                this.comptesBoxs.get(pseudo).remove(compteBox);
            }
        }
    }

    private void setupVocal(Blob vocal, VBox container, int idPublication) {
        if (vocal != null) {
            try {
                int blobLength = (int) vocal.length();
                if (blobLength > 1) {
                    byte[] bytes = vocal.getBytes(1, blobLength);
                    HBox hBoxContainer = new HBox(5);
                    HBox hBox = new HBox(2);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    for (Double amplitude : this.main.playAudio(bytes)) {
                        this.drawBar(hBox, amplitude);
                    }

                    ButtonG playPauseButton = new ButtonG(this.playImg);
                    ButtonG arretButton = new ButtonG(new ImageViewS(CheminIMG.STOP.getChemin()));
                    arretButton.setOnAction(new StopperSon(this.main, bytes));

                    JouerSon jouerSon = new JouerSon(this.main, bytes);
                    PauseSon pauseSon = new PauseSon(this.main, bytes);
                    Unpause unpause = new Unpause(this.main, bytes);

                    this.vocalBoxs.put(bytes, hBox);
                    this.playPauseButtons.put(bytes, playPauseButton);
                    this.jouerSons.put(bytes, jouerSon);
                    this.pauseSons.put(bytes, pauseSon);
                    this.unpauseSons.put(bytes, unpause);
                    this.vocaux.put(idPublication, bytes);

                    playPauseButton.setOnAction(jouerSon);

                    hBoxContainer.getChildren().addAll(playPauseButton, hBox, arretButton);
                    container.getChildren().add(hBoxContainer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void mettreEnPauseSon(byte[] bytes) {
        ButtonG buttonG = this.playPauseButtons.get(bytes);
        buttonG.setOnAction(this.unpauseSons.get(bytes));
        buttonG.setGraphic(this.playImg);
    }

    public void arreterSon(byte[] bytes) {
        ButtonG buttonG = this.playPauseButtons.get(bytes);
        buttonG.setGraphic(this.playImg);
        buttonG.setOnAction(this.jouerSons.get(bytes));
        for (Node children : this.vocalBoxs.get(bytes).getChildren()) {
            children.getStyleClass().clear();
            children.getStyleClass().add("black-rectangle");
        }
    }

    public void reprendreSon(byte[] bytes) {
        ButtonG buttonG = this.playPauseButtons.get(bytes);
        buttonG.setOnAction(this.pauseSons.get(bytes));
        buttonG.setGraphic(this.pauseImg);
    }

    public void jouerSon(byte[] bytes) {
        byte[] audioJouerActuellement = this.main.getAudioJouerActuellement();
        if (audioJouerActuellement != null) {
            if (audioJouerActuellement == this.main.getVocal())
                this.main.arreterSon(null);
            else
                this.arreterSon(audioJouerActuellement);
        }
        this.reprendreSon(bytes);
        for (Node children : this.vocalBoxs.get(bytes).getChildren()) {
            children.getStyleClass().clear();
            children.getStyleClass().add("grey-rectangle");
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
        if (isMe)
            this.mettreLikeButtonALike(likeButton, idPublication);
    }

    public void removeLike(int idPublication, int like, boolean isMe) {
        ButtonG likeButton = this.mettreAJourLike(this.recupererLikeBox(idPublication), like, idPublication, isMe);
        if (isMe)
            this.mettreLikeButtonAUnlike(likeButton, idPublication);
    }

    public void modifierComptePublications(String pseudo, Image image) {
        if (this.comptesBoxs.containsKey(pseudo)) {
            for (CompteBox compteBox : this.comptesBoxs.get(pseudo)) {
                compteBox.setPhotoProfil(image);
            }
        }
    }

    public void updateSon(byte[] bytes, int nbSecMax, int nbSecActuel) {
        if (this.vocalBoxs.get(bytes) == null)
            return;
        int nbBarMax = this.vocalBoxs.get(bytes).getChildren().size();

        int partColoration = nbBarMax / (nbSecMax + 1);
        partColoration += (partColoration * nbSecActuel);

        for (int i = 0; i < nbBarMax; ++i) {
            if (i >= partColoration && nbSecMax != nbSecActuel)
                break;

            Rectangle rectangle = (Rectangle) this.vocalBoxs.get(bytes).getChildren().get(i);
            rectangle.getStyleClass().clear();
            rectangle.getStyleClass().add("black-rectangle");
        }

        if (nbSecMax == nbSecActuel) {
            this.arreterSon(bytes);
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

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
import client.controle.UnlikeButton;
import enums.CheminCSS;
import enums.CheminFONT;
import enums.CheminIMG;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class Accueil extends VBox {

    private Main main;
    private SplitPane splitPane;
    private VBox contenant;
    private Map<Integer, VBox> publications;
    private Map<Integer, VBox> commentaires;
    private List<HBox> vocalBox;

    public Accueil(Main main) {
        this.main = main;
        this.publications = new HashMap<>();
        this.commentaires = new HashMap<>();
        this.contenant = new VBox();
        this.vocalBox = new ArrayList<>();

        ScrollPane scrollPane = new ScrollPane(this.contenant);
        scrollPane.setFitToWidth(true);

        this.splitPane = new SplitPane();
        this.splitPane.getItems().add(scrollPane);
        this.splitPane.setDividerPositions(1);

        super.getStylesheets().add(CheminCSS.ACCUEIL.getChemin());
        super.getChildren().add(this.splitPane);
    }

    public void ajouterPublication(Publication publication) {
        VBox container = new VBox();
        container.getStyleClass().add("container");

        Blob vocal = publication.getVocal();

        Label pseudoLabel = new Label(publication.getPseudo());
        Label dateLabel = new Label(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(publication.getDate()));
        Label contentLabel = new Label(publication.getContent());
        contentLabel.setWrapText(true);

        Label likeLabel = new Label(publication.getLikes() + "");
        Button likeButton = new Button();
        likeButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        this.createButton(likeButton, publication);

        Font font = Font.loadFont(CheminFONT.THE_SMILE.getChemin(), 20);
        pseudoLabel.setFont(font);
        dateLabel.setFont(font);
        contentLabel.setFont(font);
        likeLabel.setFont(font);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        HBox pseudoDateBox = new HBox(pseudoLabel, region, dateLabel);
        HBox likeBox = new HBox(region2, likeLabel, likeButton);

        if (vocal != null) {
            try {
                int blobLength = (int) vocal.length();
                byte[] bytes = vocal.getBytes(1, blobLength);
                HBox hBox = new HBox(2);
                hBox.setAlignment(Pos.CENTER_LEFT);
                for (Double amplitude : this.main.playAudio(bytes)) {
                    this.drawBar(hBox, amplitude);
                }
                this.vocalBox.add(hBox);
                container.getChildren().addAll(pseudoDateBox, contentLabel, hBox, likeBox);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            container.getChildren().addAll(pseudoDateBox, contentLabel, likeBox);
        }

        this.contenant.getChildren().add(container);

        this.publications.put(publication.getIdPublication(), container);

        publication.getCommentaires().forEach(c -> this.ajouterCommentaire(c));
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

    private void createButton(Button likeButton, Publication publication) {
        if (publication.isCallerIsLiker()) {
            likeButton.setOnAction(new UnlikeButton(this.main, publication.getIdPublication()));
            likeButton.setGraphic(this.createImageView(CheminIMG.LIKE.getChemin()));
        } else {
            likeButton.setOnAction(new LikeButton(this.main, publication.getIdPublication()));
            likeButton.setGraphic(this.createImageView(CheminIMG.UNLIKE.getChemin()));
        }
    }

    public void ajouterLike(int idPublication, int like, boolean isMe) {
        VBox container = this.publications.get(idPublication);
        HBox likeBox = (HBox) container.getChildren().get(2);
        Label likeLabel = (Label) likeBox.getChildren().get(1);
        Button likeButton = (Button) likeBox.getChildren().get(2);
        likeLabel.setText(like + "");
        if (isMe) {
            likeButton.setGraphic(this.createImageView(CheminIMG.LIKE.getChemin()));
            likeButton.setOnAction(new UnlikeButton(this.main, idPublication));
        }
    }

    public void removeLike(int idPublication, int like, boolean isMe) {
        VBox container = this.publications.get(idPublication);
        HBox likeBox = (HBox) container.getChildren().get(2);
        Label likeLabel = (Label) likeBox.getChildren().get(1);
        Button likeButton = (Button) likeBox.getChildren().get(2);
        likeLabel.setText(like + "");
        if (isMe) {
            likeButton.setGraphic(this.createImageView(CheminIMG.UNLIKE.getChemin()));
            likeButton.setOnAction(new LikeButton(this.main, idPublication));
        }
    }

    private ImageView createImageView(String chemin) {
        ImageView imageView = new ImageView(chemin);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        return imageView;
    }

    public void enleverPage() {
        if (this.splitPane.getItems().size() > 1) {
            this.splitPane.getItems().remove(1);
        }
    }

    public void ajouterPage(VBox page) {
        this.enleverPage();
        this.splitPane.getItems().add(page);
        this.splitPane.setDividerPositions(0.7);
    }

}

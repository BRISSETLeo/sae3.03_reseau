package graphique.page.centerPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graphique.controller.AjouterLike;
import graphique.page.Accueil;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Publications extends VBox {

    private static Map<String, Label> likeSave;
    private static Map<String, Label> dateSave;

    public Publications() {
        super(20);
        Publications.likeSave = new HashMap<>();
        Publications.dateSave = new HashMap<>();
    }

    public void updateLikes(String idPublication, int nouveauLike) {
        if (Publications.likeSave.containsKey(idPublication)) {
            Publications.likeSave.get(idPublication).setText(nouveauLike + "");
        }
        /*
         * if (Publications.dateSave.containsKey(idPublication)) {
         * Label date = Publications.dateSave.get(idPublication);
         * date.setText(createDateLabel(date.getText()).getText());
         * }
         */
    }

    public static void ajouterContenue(List<String> contenues) {
        Button nomUser = createButton(contenues.get(1), "nomUser");
        Label contenue = createLabel(contenues.get(2), "contenue");
        contenue.setWrapText(true);
        Label date = createDateLabel(contenues.get(3));

        Label likes = createLabel(contenues.get(4), "likes");
        HBox likesBox = createLikesHBox(likes, contenues.get(0));

        Publications.likeSave.put(contenues.get(0), likes);

        VBox conteneur = new VBox(createHeaderHBox(nomUser, date), contenue, likesBox);
        conteneur.getStyleClass().add("conteneur");

        Accueil.getContenant().getChildren().add(conteneur);
    }

    private static Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    private static Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private static Label createDateLabel(String dateString) {
        Label dateLabel = createLabel("", "date");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String timeElapsed = formatTimeElapsed(Duration.between(
                    sdf.parse(dateString).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    LocalDateTime.now()));
            dateLabel.setText(timeElapsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateLabel;
    }

    private static HBox createLikesHBox(Label likesLabel, String idPublication) {
        ImageView likeImg = new ImageView(new Image("graphique/images/like.png"));
        likeImg.setFitWidth(18);
        likeImg.setFitHeight(18);

        Button buttonLike = new Button();
        buttonLike.setOnAction(new AjouterLike(idPublication));
        buttonLike.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        buttonLike.setGraphic(likeImg);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox likesBox = new HBox(region, likesLabel, buttonLike);
        buttonLike.getStyleClass().add("buttonLike");
        likesBox.getStyleClass().add("likesBox");

        return likesBox;
    }

    private static HBox createHeaderHBox(Button nomUser, Label date) {
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox headerHBox = new HBox(nomUser, region, date);
        return headerHBox;
    }

    private static String formatTimeElapsed(Duration duration) {
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "m ";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + "h ";
        } else if (seconds < 604800) {
            long days = seconds / 86400;
            return days + "j";
        } else if (seconds < 2419200) {
            long weeks = seconds / 604800;
            return weeks + "w";
        } else if (seconds < 29030400) {
            long months = seconds / 2419200;
            return months + "mo";
        } else {
            long years = seconds / 29030400;
            return years + "y";
        }
    }

}

package graphique.page;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Accueil extends BorderPane {

    private static ScrollPane scrollPane;
    private static VBox contenant;

    public Accueil() {

        super.getStylesheets().addAll("graphique/css/Accueil.css", "graphique/css/Navbar.css",
                "graphique/css/Publications.css");

        contenant = new VBox(20);
        contenant.getStyleClass().add("patron");
        scrollPane = new ScrollPane(contenant);

        super.setTop(new TopBar());
        super.setLeft(new Navbar());
        super.setCenter(scrollPane);

    }

    public static ScrollPane getScrollPane() {
        return scrollPane;
    }

    public static VBox getContenant() {
        return contenant;
    }

    public static void ajouterContenue(List<String> contenues) {
        Button nomUser = new Button(contenues.get(1));
        Label contenue = new Label(contenues.get(2));
        contenue.setWrapText(true);
        Label date = new Label(contenues.get(3));
        String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);

        try {
            String timeElapsed = formatTimeElapsed(Duration
                    .between(sdf.parse(contenues.get(3)).toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDateTime(), LocalDateTime.now()));
            date.setText(timeElapsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Label likes = new Label(contenues.get(4));

        HBox enTete = new HBox(nomUser, date);
        ImageView likeImg = new ImageView(new Image("graphique/images/like.png"));
        likeImg.setFitWidth(18);
        likeImg.setFitHeight(18);
        HBox likesBox = new HBox(5, likes, likeImg);
        likesBox.setAlignment(Pos.CENTER_RIGHT);
        VBox conteneur = new VBox(enTete, contenue, likesBox);

        nomUser.getStyleClass().add("nomUser");
        contenue.getStyleClass().add("contenue");

        date.getStyleClass().add("date");
        likesBox.getStyleClass().add("likesBox");
        likes.getStyleClass().add("likes");

        conteneur.getStyleClass().add("conteneur");

        getContenant().getChildren().add(conteneur);
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

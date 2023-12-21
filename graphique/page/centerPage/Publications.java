package graphique.page.centerPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import graphique.Main;
import graphique.controller.AjouterLike;
import graphique.page.Accueil;
import javafx.application.Platform;
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
    private static Map<String, Label> labelSave;
    private static Map<String, String> dateSave;

    public Publications() {
        super(20);
        Publications.likeSave = new HashMap<>();
        Publications.labelSave = new HashMap<>();
        Publications.dateSave = new HashMap<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                while (Main.getInstance().getClient() == null || Main.getInstance().getClient().isAlive()) {
                    for (Entry<String, Label> labelSav : Publications.labelSave.entrySet()) {
                        String id = labelSav.getKey();
                        Label label = labelSav.getValue();
                        Platform.runLater(() -> {
                            try {
                                label.setText(formatTimeElapsed(Duration.between(
                                        sdf.parse(Publications.dateSave.get(id)).toInstant()
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime(),
                                        LocalDateTime.now())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void updateLikes(String idPublication, int nouveauLike) {
        if (Publications.likeSave.containsKey(idPublication)) {
            Publications.likeSave.get(idPublication).setText(nouveauLike + "");
        }
    }

    public static void ajouterContenue(String idPublication, String nomUser, String contenue, String date,
            String likes) {
        Button nomUserB = createButton(nomUser, "nomUser");
        Label contenueL = createLabel(contenue, "contenue");
        contenueL.setWrapText(true);
        Label dateL = createDateLabel(date);

        Label likesL = createLabel(likes, "likes");
        HBox likesBox = createLikesHBox(likesL, idPublication);

        Publications.likeSave.put(idPublication, likesL);
        Publications.labelSave.put(idPublication, dateL);
        Publications.dateSave.put(idPublication, date);

        VBox conteneur = new VBox(createHeaderHBox(nomUserB, dateL), contenueL, likesBox);
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
        } else {
            long minutes = duration.toMinutesPart();
            long hours = duration.toHoursPart();
            long days = duration.toDaysPart();
            long weeks = days / 7;
            long months = days / 30; // Approximation
            long years = days / 365; // Approximation

            StringBuilder result = new StringBuilder();

            if (years > 0) {
                result.append(years).append("y ");
                days -= years * 365;
            }

            if (months > 0) {
                result.append(months).append("mo ");
                days -= months * 30;
            }

            if (weeks > 0) {
                result.append(weeks).append("w ");
                days -= weeks * 7;
            }

            if (days > 0) {
                result.append(days).append("j ");
            }

            if (hours > 0) {
                result.append(hours).append("h ");
            }

            if (minutes > 0) {
                result.append(minutes).append("m ");
            }

            result.append(seconds % 60).append("s");

            return result.toString().trim();
        }
    }

}

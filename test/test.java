import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class test extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Overlay Popup");

        Label popupContent = new Label("Nouveau message !");
        popupContent.setStyle("-fx-background-color: red; -fx-padding: 10px; -fx-text-fill: white;");

        StackPane popupPane = new StackPane(popupContent);
        popupPane.setStyle("-fx-background-color: blue;");
        popupPane.setMouseTransparent(true);

        VBox borderPaneContent = new VBox(new Button("BONJOUR"));
        borderPaneContent.setAlignment(Pos.CENTER);

        Pane overlayPane = new Pane(borderPaneContent, popupPane);

        Scene scene = new Scene(overlayPane, 300, 200);

        primaryStage.setScene(scene);
        primaryStage.show();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), popupPane);
        tt.setToX(0);
        tt.play();
    }
}

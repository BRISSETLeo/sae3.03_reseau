package graphique;

import graphique.page.Connexion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;
    private BorderPane borderPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        this.borderPane = new BorderPane();
        this.borderPane.setCenter(new Connexion());
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("SysX");
        Scene scene = new Scene(this.borderPane, 400, 250);
        stage.setScene(scene);
        scene.getStylesheets().addAll("graphique/css/Connexion.css");
        stage.setResizable(false);
        stage.show();
    }

}

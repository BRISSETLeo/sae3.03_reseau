package graphique;

import client.Client;
import graphique.page.Connexion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;
    private static BorderPane borderPane;
    private static Client client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        borderPane = new BorderPane();
        borderPane.setCenter(new Connexion());

    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("SysX");
        Scene scene = new Scene(borderPane, 400, 250);
        stage.setScene(scene);
        scene.getStylesheets().addAll("graphique/css/Connexion.css");
        stage.setResizable(false);
        stage.show();
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client newClient) {
        client = newClient;
    }

    public Stage getStage() {
        return this.stage;
    }

    public static BorderPane getBorderPane() {
        return borderPane;
    }

}

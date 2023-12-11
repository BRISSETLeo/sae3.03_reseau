package graphique;

import client.Client;
import graphique.page.Connexion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;
    private static Client client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {

    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        stage.setTitle("SysX");
        Scene scene = new Scene(new Connexion(), 400, 250);
        stage.setScene(scene);
        scene.getStylesheets().add("graphique/css/Connexion.css");
        stage.setResizable(false);
        stage.show();
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client newClient) {
        client = newClient;
    }

    public static Stage getStage() {
        return stage;
    }

}

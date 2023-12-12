package graphique;

import client.Client;
import graphique.page.Connexion;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private static Stage stage;
    private static Client client;
    private static boolean windowIsClosed;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        Main.windowIsClosed = false;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        stage.setTitle("SysX");
        Scene scene = new Scene(new Connexion(), 400, 250);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Main.windowIsClosed = true;
            }
        });
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

    public static Stage getStage() {
        return stage;
    }

    public static boolean windowIsClosed() {
        return Main.windowIsClosed;
    }

}

package graphique;

import client.Client;
import graphique.page.Accueil;
import graphique.page.Connexion;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    private Stage stage;
    private Client client;
    private boolean windowIsClosed;

    private static Main INSTANCE;
    public static Main getInstance(){
        return INSTANCE;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        INSTANCE = this;
        this.windowIsClosed = false;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("SysX");
        Scene scene = new Scene(new Connexion(), 400, 250);
        
        this.centrerWindow(stage, 400, 250);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Main.this.windowIsClosed = true;
            }
        });
        stage.setScene(scene);
        scene.getStylesheets().addAll("graphique/css/Connexion.css");
        stage.setResizable(false);
        stage.show();
    }


    public void centrerWindow(Stage stage, int width, int heigh){
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX((bounds.getWidth()/2) - (width/2));
        stage.setY((bounds.getHeight()/2) - (heigh/2));
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client newClient) {
        client = newClient;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean windowIsClosed() {
        return this.windowIsClosed;
    }

    public void changerWindow(Pane node, int width, int height){
        this.stage.setScene(new Scene(new Accueil(), width, height));
        Platform.runLater(() -> this.centrerWindow(this.stage, width, height) );
    }

}

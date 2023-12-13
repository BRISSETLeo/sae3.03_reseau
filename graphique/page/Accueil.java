package graphique.page;

import graphique.Main;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Accueil extends BorderPane {

    private static ScrollPane scrollPane;
    private static VBox contenant;
    private SplitPane splitPane;

    public Accueil() {

        super.getStylesheets().addAll("graphique/css/Accueil.css", "graphique/css/Navbar.css",
                "graphique/css/Publications.css");

        Accueil.contenant = Main.getInstance().getPublications();
        Accueil.contenant.getStyleClass().add("patron");
        Accueil.scrollPane = new ScrollPane(contenant);

        this.splitPane = new SplitPane();
        this.splitPane.getItems().addAll(Accueil.scrollPane, Main.getInstance().getPublication());
        this.splitPane.getItems().get(1).setVisible(false);
        this.splitPane.setDividerPositions(1);

        Accueil.scrollPane.setFitToWidth(true);
        Accueil.scrollPane.setFitToHeight(true);

        super.setTop(new TopBar());
        super.setLeft(new Navbar());
        super.setCenter(this.splitPane);

    }

    public static ScrollPane getScrollPane() {
        return scrollPane;
    }

    public static VBox getContenant() {
        return contenant;
    }

    public void setPageCenter(VBox vBox) {
        this.splitPane.setDividerPositions(1);
        this.splitPane.getItems().get(1).setVisible(false);
        scrollPane.setContent(vBox);
    }

    public void setPageRight(VBox vBox) {
        this.splitPane.getItems().get(1).setVisible(true);
        this.splitPane.setDividerPositions(0.7);
    }

}

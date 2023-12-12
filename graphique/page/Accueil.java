package graphique.page;

import graphique.Main;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Accueil extends BorderPane {

    private static ScrollPane scrollPane;
    private static VBox contenant;

    public Accueil() {

        super.getStylesheets().addAll("graphique/css/Accueil.css", "graphique/css/Navbar.css",
                "graphique/css/Publications.css");

        contenant = Main.getInstance().getPublications();
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

    public void setPageCenter(VBox vBox) {
        scrollPane.setContent(vBox);
    }

}

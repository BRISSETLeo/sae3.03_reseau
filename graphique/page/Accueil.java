package graphique.page;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

public class Accueil extends BorderPane {

    private ScrollPane scrollPane;

    public Accueil() {

        super.getStylesheets().addAll("graphique/css/Accueil.css", "graphique/css/Navbar.css");

        this.scrollPane = new ScrollPane();

        super.setTop(new TopBar());
        super.setLeft(new Navbar());

    }

}

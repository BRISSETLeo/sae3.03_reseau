package graphique.page.centerPage;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class Publication extends VBox {

    private TextArea contenue;

    public Publication() {
        super(20);

        this.contenue = new TextArea();
        this.contenue.setPromptText("Contenue de la publication");

        Button publier = new Button("Publier");

        super.getChildren().addAll(this.contenue, publier);

    }

}

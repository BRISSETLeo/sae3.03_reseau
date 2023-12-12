package graphique.page;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TopBar extends HBox {

    public TopBar() {

        super.getStyleClass().add("topBar");

        ImageView sysX = new ImageView(new Image("graphique/images/SysX.png"));

        sysX.setFitWidth(100);
        sysX.setFitHeight(100);

        super.getChildren().add(sysX);

    }

}

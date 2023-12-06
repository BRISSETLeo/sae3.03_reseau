package graphique.page;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Connexion extends VBox {

    public Connexion() {
        final TextField ip = new TextField("Ip");
        final TextField pseudo = new TextField("Pseudo");
        final Button button = new Button("Se connecter");
        ip.getStyleClass().add("textfield");
        pseudo.getStyleClass().add("textfield");
        button.getStyleClass().add("button");
        super.getChildren().addAll(ip, pseudo, button);
    }

}

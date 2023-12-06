package graphique.page;

import graphique.controller.CliquerConnexion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Connexion extends VBox {

    private final TextField ip;
    private final TextField pseudo;
    private final Button button;

    public Connexion() {
        this.ip = new TextField("Ip");
        this.pseudo = new TextField("Pseudo");
        this.button = new Button("Se connecter");
        this.ip.getStyleClass().add("textfield");
        this.pseudo.getStyleClass().add("textfield");
        this.button.getStyleClass().add("button");
        this.button.setOnAction(new CliquerConnexion(this));
        super.getChildren().addAll(ip, pseudo, button);
    }

    public TextField getIp() {
        return this.ip;
    }

    public TextField getPseudo() {
        return this.pseudo;
    }

    public Button getButton() {
        return this.button;
    }

}

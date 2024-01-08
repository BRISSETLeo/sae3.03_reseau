package client.graphisme;

import client.Main;
import client.controle.NewPublication;
import enums.CheminCSS;
import enums.CheminFONT;
import enums.CheminIMG;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Message extends VBox {

    private Main main;


    public Message(Main main) {
        this.main = main;

        super.getChildren().addAll(new Label("Messages") );


    }


}

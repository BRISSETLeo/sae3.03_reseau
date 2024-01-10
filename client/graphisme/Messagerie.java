package client.graphisme;

import caches.Compte;
import client.Main;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Messagerie extends VBox {

    private Main main;

    public Messagerie(Main main) {
        this.main = main;

        super.getChildren().addAll(new Label("Messages") );
        

    }

    public void ajouterCompte(Compte compte) {
    
    }  
}

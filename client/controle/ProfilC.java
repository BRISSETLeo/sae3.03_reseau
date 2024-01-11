package client.controle;

import client.Main;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class ProfilC implements EventHandler<MouseEvent> {
    
    private Main main;

    public ProfilC(Main main) {
        this.main = main;
    }

    @Override
    public void handle(MouseEvent event) {
        
        this.main.afficherPageProfil();

    }

}

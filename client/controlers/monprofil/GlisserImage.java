package client.controlers.monprofil;

import client.vues.MonProfil;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class GlisserImage implements EventHandler<DragEvent> {

    private final MonProfil monProfil;

    public GlisserImage(MonProfil monProfil) {
        this.monProfil = monProfil;
    }

    @Override
    public void handle(DragEvent event) {

        if (event.getGestureSource() != this.monProfil.getProfilBox().getCircle() && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();

    }

}

package client.controlers.publication;

import client.vues.Publication;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class GlisserImage implements EventHandler<DragEvent> {

    private final Publication publication;

    public GlisserImage(Publication publication) {
        this.publication = publication;
    }

    @Override
    public void handle(DragEvent event) {

        if (event.getGestureSource() != this.publication.getImage() && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();

    }

}

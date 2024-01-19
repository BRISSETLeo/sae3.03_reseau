package client.controlers.publication;

import client.Main;
import client.vues.Publication;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class PublierPublication implements EventHandler<MouseEvent> {

    private final Main main;
    private final Publication publication;

    public PublierPublication(Main main, Publication publication) {
        this.main = main;
        this.publication = publication;
    }

    @Override
    public void handle(MouseEvent event) {

        String contenuPublication = this.publication.getContenuPublication().getText();

        if (contenuPublication.isBlank()) {
            this.publication.setErreur();
        } else {
            this.main.getClient().publierPublication(
                    contenuPublication,
                    this.publication.getInsertionDImage().getImage());
        }

    }

}

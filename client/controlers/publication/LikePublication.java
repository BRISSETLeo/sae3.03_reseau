package client.controlers.publication;

import client.Main;
import client.vues.Accueil;
import client.vues.PublicationVue;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LikePublication implements EventHandler<MouseEvent> {

    private final PublicationVue publicationVue;
    private final Accueil accueil;
    private final Main main;

    public LikePublication(PublicationVue publicationVue, Accueil accueil, Main main) {
        this.publicationVue = publicationVue;
        this.accueil = accueil;
        this.main = main;
    }

    @Override
    public void handle(MouseEvent event) {

        if (this.publicationVue.getLikeView().getImage().equals(this.accueil.getUnlikeImg())) {
            this.main.getClient().likerPublication(this.publicationVue.getIdPublication());
        } else {
            this.main.getClient().dislikerPublication(this.publicationVue.getIdPublication());
        }

    }

}

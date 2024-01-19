package client.controlers.message;

import client.Main;
import client.vues.Message;
import client.vues.nodes.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class EnvoyerMessage implements EventHandler<ActionEvent> {

    private final Main main;
    private final Message message;

    public EnvoyerMessage(Main main, Message message) {
        this.main = main;
        this.message = message;
    }

    @Override
    public void handle(ActionEvent event) {

        String contenu = this.message.getEcrireMessage().getText();

        Image image = null;

        if (this.message.getContainerScrollSearch().getChildren().get(1) instanceof VBox) {
            image = ((ImageView) ((VBox) this.message.getContainerScrollSearch().getChildren().get(1)).getChildren()
                    .get(1)).getImage();
        }

        if (!contenu.isBlank() || image != null) {
            this.main.getClient().envoyerMessage(this.message.getPseudoCorrespondant(), contenu,
                    image == null ? null : this.main.imageToByteArray(image));
        }

    }

}

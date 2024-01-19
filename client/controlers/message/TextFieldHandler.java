package client.controlers.message;

import client.vues.Message;
import client.vues.nodes.Button;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class TextFieldHandler implements EventHandler<KeyEvent> {

    private final Message message;

    public TextFieldHandler(Message message) {
        this.message = message;
    }

    @Override
    public void handle(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {
            this.message.getEnvoyerMessage().fire();
            return;
        }

        Platform.runLater(() -> {
            Button enregistrerVocal = this.message.getEnregistrerVocal();
            Button envoyerMessage = this.message.getEnvoyerMessage();

            boolean mettreVisible = false;

            if (this.message.getEcrireMessage().getText().isBlank()) {
                mettreVisible = true;
            }

            enregistrerVocal.setVisible(mettreVisible);
            enregistrerVocal.setManaged(mettreVisible);

            envoyerMessage.setVisible(!mettreVisible);
            envoyerMessage.setManaged(!mettreVisible);
        });

    }

}

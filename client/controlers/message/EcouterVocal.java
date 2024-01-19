package client.controlers.message;

import client.vues.Message;
import client.vues.PatternVocal;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EcouterVocal implements EventHandler<ActionEvent> {

    private final Message message;
    private final PatternVocal patternVocal;

    public EcouterVocal(Message message, PatternVocal patternVocal) {
        this.message = message;
        this.patternVocal = patternVocal;
    }

    @Override
    public void handle(ActionEvent event) {

        this.message.getGestionSon().jouerSon(this.patternVocal.getVocal(), this.patternVocal);
        patternVocal.switchPlayPause();

    }

}

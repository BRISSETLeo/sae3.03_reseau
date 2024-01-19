package client.controlers.message;

import client.vues.Message;
import client.vues.PatternVocal;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class EnleverEcouteVocal implements EventHandler<ActionEvent> {

    private final Message message;
    private final PatternVocal patternVocal;

    public EnleverEcouteVocal(Message message, PatternVocal patternVocal) {
        this.message = message;
        this.patternVocal = patternVocal;
    }

    @Override
    public void handle(ActionEvent event) {

        this.message.getGestionSon().pauseSon(this.patternVocal.getVocal());

    }

}

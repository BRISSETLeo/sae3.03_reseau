package client.controlers.profil;

import client.Main;
import client.vues.Profil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Abonne implements EventHandler<ActionEvent> {

    private final Main main;
    private final Profil profil;
    private final String pseudo;

    public Abonne(Main main, Profil profil, String pseudo) {
        this.main = main;
        this.profil = profil;
        this.pseudo = pseudo;
    }

    @Override
    public void handle(ActionEvent event) {

        String nomButton = this.profil.getSuivre().getText();

        if (nomButton.equals("Suivre")) {
            this.main.getClient().demanderSuivre(this.pseudo);
        } else {
            this.main.getClient().demanderNePlusSuivre(this.pseudo);
        }

    }

}

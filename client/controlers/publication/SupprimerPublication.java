package client.controlers.publication;

import java.util.Optional;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SupprimerPublication implements EventHandler<ActionEvent> {

    private final Main main;
    private final int idPublication;

    public SupprimerPublication(Main main, int idPublication) {

        this.main = main;
        this.idPublication = idPublication;

    }

    @Override
    public void handle(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer publication");
        alert.setHeaderText("Supprimer publication");
        alert.setContentText("Voulez-vous vraiment supprimer cette publication ?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {

            this.main.getClient().supprimerPublication(this.idPublication);

        }

    }

}

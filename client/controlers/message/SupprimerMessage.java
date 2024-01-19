package client.controlers.message;

import java.util.Optional;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SupprimerMessage implements EventHandler<ActionEvent> {

    private final Main main;
    private final int idMessage;

    public SupprimerMessage(Main main, int idMessage) {
        this.main = main;
        this.idMessage = idMessage;
    }

    @Override
    public void handle(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Supprimer un message");
        alert.setHeaderText("Supprimer un message");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce message ?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.NO)
            return;

        this.main.getClient().supprimerMessage(this.idMessage);
    }

}

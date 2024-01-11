package client.controle;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Unpause implements EventHandler<ActionEvent> {

    private Main main;
    private byte[] bytes;

    public Unpause(Main main, byte[] bytes) {
        this.main = main;
        this.bytes = bytes;
    }

    @Override
    public void handle(ActionEvent event) {

        this.main.reprendreSon(this.bytes);

    }

}

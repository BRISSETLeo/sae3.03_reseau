package client.graphisme.affichage;

import enums.FontP;
import javafx.scene.control.Button;

public class ButtonF extends Button {

    public ButtonF(String text) {
        super(text);
        super.setFont(FontP.FONT_20.getFont());
    }

}

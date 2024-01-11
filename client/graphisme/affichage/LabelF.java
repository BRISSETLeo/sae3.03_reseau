package client.graphisme.affichage;

import enums.FontP;
import javafx.scene.control.Label;

public class LabelF extends Label {

    public LabelF(String text) {
        super(text);
        super.setWrapText(true);
        super.setFont(FontP.FONT_20.getFont());
    }

}

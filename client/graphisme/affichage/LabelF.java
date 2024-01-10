package client.graphisme.affichage;

import enums.FontP;
import javafx.scene.control.Label;

public class LabelF extends Label {

    public LabelF(String text) {
        super(text);
        super.setFont(FontP.FONT_20.getFont());
    }

    public LabelF setWrapText() {
        super.setWrapText(true);
        return this;
    }

}

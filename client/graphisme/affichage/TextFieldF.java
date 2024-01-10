package client.graphisme.affichage;

import enums.FontP;
import javafx.scene.control.TextField;

public class TextFieldF extends TextField {

    public TextFieldF(String promptText) {
        super.setPromptText(promptText);
        super.setFont(FontP.FONT_30.getFont());
    }

}

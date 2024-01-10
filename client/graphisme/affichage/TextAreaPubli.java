package client.graphisme.affichage;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;

public class TextAreaPubli extends TextArea {

    public TextAreaPubli(int maxLength) {
        super();
        super.setPromptText("Contenu de la publication..");
        this.init(maxLength);
    }

    public TextAreaPubli(String text, int maxLength) {
        super(text);
        this.init(maxLength);
    }

    private void init(int maxLength) {
        super.setWrapText(true);
        this.setTextFormatter(maxLength);
    }

    private void setTextFormatter(int maxLength) {
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                if (change.getControlNewText().length() <= maxLength) {
                    return change;
                }
                return null;
            }
            return change;
        });
        super.setTextFormatter(textFormatter);
    }

}

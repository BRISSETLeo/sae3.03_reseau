package enums;

import client.Main;
import javafx.scene.text.Font;

public enum FontP {

    FONT_15(Font.loadFont(Main.fontPath, 15)),
    FONT_20(Font.loadFont(Main.fontPath, 20)),
    FONT_30(Font.loadFont(Main.fontPath, 30)),
    FONT_50(Font.loadFont(Main.fontPath, 50));

    private Font font;

    FontP(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return this.font;
    }

}

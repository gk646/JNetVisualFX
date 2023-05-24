package gk646.jnet.userinterface.graphics;

import javafx.scene.text.Font;

public final class Resources {

    public static final Font cascadiaCode = Font.loadFont(Resources.class.getResourceAsStream("/CascadiaCode.ttf"), 15);
    public static final Font cascadiaCode12 = Font.loadFont(Resources.class.getResourceAsStream("/CascadiaCode.ttf"), 12);
    private Resources() {
    }

    public static Font getFontInSize(int size) {
        return new Font(cascadiaCode.getFamily(), size);
    }
}

package gk646.jnet.userinterface.graphics;

import javafx.scene.text.Font;

public final class Resources {

    private Resources() {
    }

    public static Font cascadiaCode = Font.loadFont(Resources.class.getResourceAsStream("/CascadiaCode.ttf"), 15);


    public static Font getFontInSize(int size) {
        return new Font(cascadiaCode.getFamily(), size);
    }
}

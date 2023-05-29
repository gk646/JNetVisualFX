package gk646.jnet.userinterface.graphics;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public final class Resources {
    public static final Font cascadiaCode = Font.loadFont(Resources.class.getResourceAsStream("/CascadiaCode.ttf"), 15);
    public static final double LETTER_WIDTH_15 = initCharacterWidth(15);
    public static final double LETTER_WIDTH_12 = initCharacterWidth(12);
    public static final Font cascadiaCode12 = Font.loadFont(Resources.class.getResourceAsStream("/CascadiaCode.ttf"), 12);

    private Resources() {
    }

    public static Font getFontInSize(int size) {
        return new Font(cascadiaCode.getFamily(), size);
    }

    private static double initCharacterWidth(int size) {
        Text text = new Text("A");
        text.setFont(Resources.getFontInSize(size));
        return (float) (text.getLayoutBounds().getWidth() + 0.1);
    }
}

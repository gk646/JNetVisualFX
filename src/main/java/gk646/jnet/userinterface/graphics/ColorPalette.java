package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.userinterface.terminal.Terminal;
import javafx.scene.paint.Color;

public enum ColorPalette {

    DEFAULT(Colors.LIGHT_BLACK, Color.rgb(90, 105, 136), Colors.MILK, Colors.MILK),
    BROWN(Color.rgb(203, 153, 126), Color.rgb(221, 190, 169), Color.rgb(255, 232, 214), Color.rgb(183, 183, 164)),
    RAINBOW(Color.rgb(202, 255, 191), Color.rgb(155, 246, 255), Color.rgb(189, 178, 255), Color.rgb(253, 255, 182)),
    NEON(Color.rgb(255, 0, 110), Color.rgb(131, 56, 236), Color.rgb(58, 134, 255), Color.rgb(255, 190, 11));
    final Color terminalBack;
    final Color logBack;

    final Color textDefault;

    final Color mainScreenBack;

    ColorPalette(Color terminalBack, Color logBack, Color textColor, Color mainBack) {
        this.terminalBack = terminalBack;
        this.logBack = logBack;
        this.textDefault = textColor;
        this.mainScreenBack = mainBack;
    }

    public void loadPalette() {
        Terminal.TerminalInfo.setBackGround(this.terminalBack);
        Terminal.TerminalInfo.setTextColor(this.textDefault);
        Log.setTextColor(this.textDefault);
        Log.setBackGround(this.logBack);
    }
}

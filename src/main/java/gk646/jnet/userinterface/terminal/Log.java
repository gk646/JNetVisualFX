package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.NetLogger;
import gk646.jnet.util.StringUtil;
import gk646.jnet.util.datastructures.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * The Logger responsible for logging errors, info and exceptions to both the application log and java-terminal.
 */
public final class Log {
    public static final NetLogger logger = new NetLogger("", null);
    public static final ContainerHelper containerHelper = new ContainerHelper(75, 0, 25, 100);
    public static final int LINE_HEIGHT = 14;
    private static final LimitedQueue<String> logText = new LimitedQueue<>(75);
    private static final Color backGround = Colors.UBUNTU_BLACK;
    public static int scrollOffset = 0;
    static int maxCharsPerLine = 49;
    public final double characterWidth = ContainerHelper.initCharacterWidth(12);

    public Log() {
        logText.setLimit(containerHelper.getHeight() / LINE_HEIGHT);
    }

    public static void addLogText(String text) {
        logText.add(StringUtil.insertNewLines(text, maxCharsPerLine));
    }

    public void draw(GraphicsContext gc) {
        drawBackGround(gc);
        drawLogText(gc);
    }

    private void drawLogText(GraphicsContext gc) {
        gc.setFont(Resources.cascadiaCode12);
        if (!gc.getFill().equals(Terminal.TerminalInfo.text)) {
            gc.setFill(Terminal.TerminalInfo.text);
        }
        int startX = containerHelper.getDrawX() + 3;
        int startY = containerHelper.getDrawY() + containerHelper.getHeight() - LINE_HEIGHT + scrollOffset;
        for (String s : logText) {
            startY -= StringUtil.countNewlines(s) * LINE_HEIGHT;
            if (s.startsWith("[W")) {
                gc.setFill(Colors.CADMIUM_ORANGE);
            } else if (s.startsWith("[S")) {
                gc.setFill(Colors.RED);
            } else {
                if (!gc.getFill().equals(Terminal.TerminalInfo.text)) {
                    gc.setFill(Terminal.TerminalInfo.text);
                }
            }
            gc.fillText(s, startX, startY);
            startY -= LINE_HEIGHT;
        }
    }

    public void drawBackGround(GraphicsContext gc) {
        gc.setFill(backGround);
        gc.fillRoundRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth() + 3, containerHelper.getHeight(), 25, 25);

        gc.setFill(Colors.PHILIPINE_SILVER);
        gc.fillText("[Log]", containerHelper.getDrawX() + 10, containerHelper.getDrawY() + 20);
    }

    public void updateSize() {
        logText.setLimit(containerHelper.getHeight() / LINE_HEIGHT);
        maxCharsPerLine = (int) (containerHelper.getWidth() / characterWidth);
        recalculateLineBreaks();
    }

    public void recalculateLineBreaks() {
        String string;
        for (int i = 0; i < logText.size(); i++) {
            string = logText.directGet(i);
            if (string.length() < maxCharsPerLine) {
                continue;
            }
            string = string.replace("\n", "");
            string = StringUtil.insertNewLines(string, maxCharsPerLine);
            logText.set(string, i);
        }
    }
}

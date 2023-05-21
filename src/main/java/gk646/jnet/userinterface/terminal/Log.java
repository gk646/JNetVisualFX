package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.LogHandler;
import gk646.jnet.util.Logging;
import gk646.jnet.util.datastructures.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * The Logger responsible for logging errors, info and exceptions to both the application log and java-terminal.
 */
public final class Log {
    public static final Logging logger = new Logging("", null);
    /**
     * Controls the visibility of debug messages and logging.
     */
    public static boolean verbose = true;
    private static final LimitedQueue<String> logText = new LimitedQueue<>(50);
    private static final Color backGround = Colors.DARK_GREY;
    public static final ContainerHelper containerHelper = new ContainerHelper(65, 0, 35, 50);
    public static int scrollOffset = 0;
    public final float characterWidth;
    public static final int LINE_HEIGHT = 14;
    final List<String> lines = new ArrayList<>();

    public Log() {
        logger.addHandler(new LogHandler());
        Text text = new Text("A");
        text.setFont(Resources.cascadiaCode11);
        this.characterWidth = (float) (text.getLayoutBounds().getWidth() + 0.1);
    }

    public static void addLogText(String text) {
        logText.add(text);
    }

    public void draw(GraphicsContext gc) {
        drawBackGround(gc);
        drawScrollingText(gc);
    }

    private void drawScrollingText(GraphicsContext gc) {
        gc.save();

        gc.setFill(Color.MINTCREAM);
        gc.setFont(Resources.cascadiaCode11);
        int startX = containerHelper.getDrawX() + 3;
        int startY = containerHelper.getDrawY() + containerHelper.getHeight() - LINE_HEIGHT + scrollOffset;

        int maxCharsPerLine = (int) (containerHelper.getWidth() / characterWidth);
        for (String string : logText) {
            lines.clear();
            int start = 0;

            while (start < string.length()) {
                String substring;
                if (start + maxCharsPerLine > string.length()) {
                    substring = string.substring(start);
                } else {
                    substring = string.substring(start, start + maxCharsPerLine);
                }
                lines.add(substring);
                start += maxCharsPerLine;
            }

            startY -= (lines.size() - 1) * LINE_HEIGHT;

            for (String line : lines) {
                gc.fillText(line, startX, startY);
                startY += LINE_HEIGHT;
            }

            startY -= (lines.size() + 1) * LINE_HEIGHT;
        }

        gc.restore();
    }

    public void drawBackGround(GraphicsContext gc) {
        gc.setFill(Colors.PHILIPINE_SILVER);
        gc.fillRoundRect(containerHelper.getDrawX() - 5, containerHelper.getDrawY(), containerHelper.getWidth() + 5, containerHelper.getHeight(), 25, 25);

        gc.setFill(backGround);
        gc.fillRoundRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth() + 3, containerHelper.getHeight(), 25, 25);
        gc.setFill(Colors.PHILIPINE_SILVER);
        gc.fillText("[Log]", containerHelper.getDrawX() + 10, containerHelper.getDrawY() + 20);
    }


    public void updateSize() {
        logText.setLimit(containerHelper.getHeight() / LINE_HEIGHT);
    }
}

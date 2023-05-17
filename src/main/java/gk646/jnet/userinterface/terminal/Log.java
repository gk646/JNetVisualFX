package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public final class Log extends Handler {

    private static final Queue<String> logText = new ArrayDeque<>();
    private static Color backGround = Colors.DARK_GREY;
    public static ContainerHelper containerHelper;
    public static int scrollOffset = 0;
    public final int CHARACTER_WIDTH;
    public final static int LINE_HEIGHT = 14;

    public Log() {
        Text text = new Text("A");
        text.setFont(Resources.cascadiaCode12);
        this.CHARACTER_WIDTH = (int) text.getLayoutBounds().getWidth();
    }


    public void draw(GraphicsContext gc) {
        drawBackGround(gc);
        drawScrollingText(gc);
    }

    private void drawScrollingText(GraphicsContext gc) {
        gc.setFill(Color.MINTCREAM);
        gc.save();
        gc.setFont(Resources.cascadiaCode12);
        int startX = containerHelper.getDrawX();
        int startY = containerHelper.getDrawY() + containerHelper.getHeight() - LINE_HEIGHT + scrollOffset;

        int maxCharsPerLine = (containerHelper.getWidth() / CHARACTER_WIDTH);
        for (String string : logText) {
            Text text1 = new Text(string);
            text1.setFont(gc.getFont());

            List<String> lines = new ArrayList<>();
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
        gc.fillText("[Log]", containerHelper.getDrawX() + 25, containerHelper.getDrawY() - 25);
    }


    /**
     * Publish a {@code LogRecord}.
     * <p>
     * The logging request was made initially to a {@code Logger} object,
     * which initialized the {@code LogRecord} and forwarded it here.
     * <p>
     * The {@code Handler}  is responsible for formatting the message, when and
     * if necessary.  The formatting should include localization.
     *
     * @param record description of the log event. A null record is
     *               silently ignored and is not published
     */
    @Override
    public void publish(LogRecord record) {
        if (isLoggable(record)) {
            addLogText(getFormatter().format(record));
        }
    }

    /**
     * Flush any buffered output.
     */
    @Override
    public void flush() {

    }

    /**
     * Close the {@code Handler} and free all associated resources.
     * <p>
     * The close method will perform a {@code flush} and then close the
     * {@code Handler}.   After close has been called this {@code Handler}
     * should no longer be used.  Method calls may either be silently
     * ignored or may throw runtime exceptions.
     *
     * @throws SecurityException if a security manager exists and if
     *                           the caller does not have {@code LoggingPermission("control")}.
     */
    @Override
    public void close() throws SecurityException {

    }

    @Override
    public Formatter getFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord logRecord) {
                return
                        " [" + logRecord.getLevel() + "] " +
                                logRecord.getMessage() + "\n";
            }
        };
    }

    @Override
    public Level getLevel() {
        return Level.ALL;
    }

    public static void addLogText(String text) {
        logText.add(text);
    }
}

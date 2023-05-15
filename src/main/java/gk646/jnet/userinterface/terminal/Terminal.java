package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.JNetVisualFX;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public final class Terminal {
    private static byte MAX_LINES = 25;
    private static byte LINE_HEIGHT = 17;
    private static byte FONT_SIZE = 15;
    private static final byte COMMAND_HISTORY_LENGTH = 127;
    private int counter = 0;
    private static String terminalRoot = "> ";
    public static StringBuilder currentText = new StringBuilder();
    private ContainerHelper terminal;
    public static final LimitedQueue<String> commandHistory = new LimitedQueue<>(COMMAND_HISTORY_LENGTH);
    private static final LimitedQueue<String> scrollingText = new LimitedQueue<>(5);
    private static final Parser parser = new Parser();


    public Terminal() {
        commandHistory.add("help");
    }

    public void updateSize() {
        MAX_LINES = (byte) (JNetVisualFX.bounds.y / LINE_HEIGHT);
    }

    public void draw(GraphicsContext gc, ContainerHelper containerHelper) {
        this.terminal = containerHelper;
        drawBorder(gc);
        drawScrollingText(gc);
        drawActiveLine(gc);
    }


    private void drawScrollingText(GraphicsContext gc) {
        gc.setFill(Color.MINTCREAM);
        short startX = terminal.getDrawX();
        short startY = (short) (JNetVisualFX.bounds.y - 25);
        for (String string : scrollingText) {
            gc.fillText(string, startX, startY);
            startY -= LINE_HEIGHT;
        }
    }

    private void drawBorder(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(terminal.getDrawX(), terminal.getDrawY(), terminal.getWidth(), terminal.getHeight());
    }

    private void drawActiveLine(GraphicsContext gc) {
        gc.fillText(terminalRoot + currentText.toString() + (counter % 45 < 20 ? "|" : "")
                , terminal.getDrawX(), JNetVisualFX.bounds.y - 5);
        counter++;
    }

    public static void parseText(String text) {
        if (text.isBlank()) {
            scrollingText.add(terminalRoot);
            return;
        }

        if (!parser.parse(text)) {
            scrollingText.add(text + " :was not found to be a command");
        } else {
            commandHistory.add(text);
        }
    }

    public static void addText(String text) {
        scrollingText.add(text);
    }

    public static void clear() {
        scrollingText.clear();
    }

    public static void changeFontSize(int value) {
        JNetVisualFX.gc.setFont(Resources.getFontInSize(FONT_SIZE += value));
    }
}
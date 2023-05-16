package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.JNetVisualFX;
import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public final class Terminal {
    private static byte MAX_LINES = 25;
    private static byte LINE_HEIGHT = 17;
    private static byte FONT_SIZE = 15;
    public static byte cursorOffsetLeft = 0;
    private static final byte COMMAND_HISTORY_LENGTH = 127;
    private int counter = 0;
    private static String terminalRoot = "> ";
    public static StringBuilder currentText = new StringBuilder();
    public static ContainerHelper containerHelper;
    public static final LimitedQueue<String> commandHistory = new LimitedQueue<>(COMMAND_HISTORY_LENGTH);
    private static final LimitedQueue<String> scrollingText = new LimitedQueue<>(MAX_LINES);
    private static final Parser parser = new Parser();
    private static Color backGround = Colors.BLACK;
    private static Color text = Colors.WHITE_SMOKE;
    public Terminal() {
        scrollingText.add("Welcome to JNetVisualFX! To get started use: \"new NetBuilder((4,4,4),sigmoid)\"");
        commandHistory.add("help");
    }

    public void updateSize() {
        MAX_LINES = (byte) (JNetVisualFX.bounds.y / LINE_HEIGHT);
    }

    public void draw(GraphicsContext gc) {
        drawBorder(gc);
        drawScrollingText(gc);
        drawActiveLine(gc);
    }


    private void drawScrollingText(GraphicsContext gc) {
        gc.setFill(Color.MINTCREAM);
        short startX = containerHelper.getDrawX();
        short startY = (short) (JNetVisualFX.bounds.y - 25);
        for (String string : scrollingText) {
            gc.fillText(string, startX, startY);
            startY -= LINE_HEIGHT;
        }
    }

    private void drawBorder(GraphicsContext gc) {
        gc.setFill(backGround);
        gc.fillRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth(), containerHelper.getHeight());
    }

    private void drawActiveLine(GraphicsContext gc) {
        gc.setFill(text);
        StringBuilder activeLine = new StringBuilder(terminalRoot + currentText.toString());
        if (cursorOffsetLeft > 0) {
            activeLine.insert(activeLine.length() - cursorOffsetLeft, (counter % 45 < 20 ? "|" : " "));
        } else {
            activeLine.append(counter % 45 < 20 ? "|" : "");
        }
        gc.fillText(activeLine.toString(), containerHelper.getDrawX(), JNetVisualFX.bounds.y - 5);
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
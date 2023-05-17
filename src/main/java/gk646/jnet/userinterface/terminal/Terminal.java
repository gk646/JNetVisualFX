package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.JNetVisualFX;
import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public final class Terminal {
    public static float CHARACTER_WIDTH;
    private static byte MAX_LINES = 25;
    private static byte FONT_SIZE = 15;
    public static int LINE_HEIGHT = FONT_SIZE + 4;
    public static byte cursorOffsetLeft = 0;
    private static final byte COMMAND_HISTORY_LENGTH = 127;
    private int counter = 0;
    private static String terminalRoot = "> ";
    public static StringBuilder currentText = new StringBuilder();
    public static ContainerHelper containerHelper;
    public static final LimitedQueue<String> commandHistory = new LimitedQueue<>(COMMAND_HISTORY_LENGTH);
    private static final LimitedQueue<String> scrollingText = new LimitedQueue<>(MAX_LINES);
    private static final Parser parser = new Parser();
    private static Color backGround = Colors.LIGHT_BLACK;
    private static Color text = Colors.WHITE_SMOKE;

    public Terminal() {
        scrollingText.add("Welcome to JNetVisualFX! To get started use: \"new NetBuilder((4,4,4),sigmoid)\"");
        commandHistory.add("help");

       changeFontSize(0);
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
        int startX = containerHelper.getDrawX();
        int startY = containerHelper.getDrawY() + containerHelper.getHeight() - LINE_HEIGHT * 2;
        int maxCharsPerLine = (int) (containerHelper.getWidth() / CHARACTER_WIDTH);

        for (String string : scrollingText) {
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
    }

    private void drawBorder(GraphicsContext gc) {
        gc.setFill(Colors.PHILIPINE_SILVER);
        gc.fillRoundRect(containerHelper.getDrawX() - 5, containerHelper.getDrawY(), containerHelper.getWidth() + 5, containerHelper.getHeight(), 25, 25);

        gc.setFill(backGround);
        gc.fillRoundRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth() + 3, containerHelper.getHeight() + 3, 25, 25);
    }

    private void drawActiveLine(GraphicsContext gc) {
        gc.setFill(text);
        StringBuilder activeLine = new StringBuilder(terminalRoot + currentText.toString());
        if (cursorOffsetLeft > 0) {
            activeLine.insert(activeLine.length() - cursorOffsetLeft, (counter % 45 < 20 ? "|" : " "));
        } else {
            activeLine.append(counter % 45 < 20 ? "|" : "");
        }
        gc.fillText(activeLine.toString(), containerHelper.getDrawX(), containerHelper.getDrawY() + containerHelper.getHeight() - FONT_SIZE);
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
        FONT_SIZE += value;
        Font font = Resources.getFontInSize(FONT_SIZE);
        JNetVisualFX.gc.setFont(font);
        Text text1 = new Text("A");
        text1.setFont(font);
        CHARACTER_WIDTH = (float) (text1.getLayoutBounds().getWidth());
        LINE_HEIGHT = FONT_SIZE + 4;
    }
}
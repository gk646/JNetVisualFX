package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.JNetVisualFX;
import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.userinterface.userinput.InputHandler;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.datastructures.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


public final class Terminal {
    static double characterWidth = ContainerHelper.initCharacterWidth(15);
    private static int fontSize = 15;
    public static int lineHeight = fontSize + 4;
    public static byte cursorOffsetLeft = 0;
    private static final byte COMMAND_HISTORY_LENGTH = 127;
    private int counter = 0;
    public static String terminalRoot = "> ";
    public static StringBuilder currentText = new StringBuilder();
    static final ContainerHelper containerHelper = new ContainerHelper(65, 50, 35, 50);
    public static final LimitedQueue<String> commandHistory = new LimitedQueue<>(COMMAND_HISTORY_LENGTH);
    private static final LimitedQueue<String> terminalText = new LimitedQueue<>(35);
    private static final Parser parser = new Parser();
    private static final Color backGround = Colors.LIGHT_BLACK;
    static final Color text = Colors.WHITE_SMOKE;
    private static final CodeCompletion codeCompletion = new CodeCompletion();
    final List<String> lines = new ArrayList<>();

    public Terminal() {
        terminalText.add("Welcome to JNetVisualFX! To get started use: \"new NetBuilder((4,4,4),sigmoid)\"");
        commandHistory.add("help");
    }

    public static int getFontSize() {
        return fontSize;
    }

    public void updateSize() {
        terminalText.setLimit(containerHelper.getHeight() / lineHeight);
    }

    public void draw(GraphicsContext gc) {
        drawBorder(gc);
        drawScrollingText(gc);
        drawActiveLine(gc);
        codeCompletion.draw(gc);
    }


    private void drawScrollingText(GraphicsContext gc) {
        gc.setFill(Color.MINTCREAM);
        int startX = containerHelper.getDrawX();
        int startY = containerHelper.getDrawY() + containerHelper.getHeight() - lineHeight * 2;
        int maxCharsPerLine = (int) (containerHelper.getWidth() / characterWidth);

        for (String string : terminalText) {

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

            startY -= (lines.size() - 1) * lineHeight;

            for (String line : lines) {
                gc.fillText(line, startX, startY);
                startY += lineHeight;
            }

            startY -= (lines.size() + 1) * lineHeight;
        }
    }

    private void drawBorder(GraphicsContext gc) {
        gc.setFill(Colors.PHILIPINE_SILVER);
        gc.fillRoundRect(containerHelper.getDrawX() - 5, containerHelper.getDrawY(), containerHelper.getWidth() + 5, containerHelper.getHeight(), 25, 25);

        gc.setFill(backGround);
        gc.fillRoundRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth() + 3, containerHelper.getHeight() + 3, 25, 25);

        gc.setFill(Colors.PHILIPINE_SILVER);
        gc.fillText("[Terminal]", containerHelper.getDrawX() + 10, containerHelper.getDrawY() + 20);
    }

    private void drawActiveLine(GraphicsContext gc) {
        gc.setFill(text);
        String activeLine = terminalRoot + currentText.toString();
        int activeLineLength = activeLine.length();
        int insertionPoint = activeLineLength - cursorOffsetLeft;

        String cursorDisplay = (counter++ % 45 < 20) ? "|" : " ";

        if (cursorOffsetLeft > 0 && insertionPoint <= activeLineLength) {
            activeLine = new StringBuilder(activeLine).insert(insertionPoint, cursorDisplay).toString();
        } else {
            activeLine += cursorDisplay;
        }
        gc.fillText(activeLine, containerHelper.getDrawX(), containerHelper.getDrawY() + containerHelper.getHeight() - fontSize);
    }

    public static void parseText(String text) {
        if (text.isBlank()) {
            terminalText.add(terminalRoot);
            return;
        }

        if (parser.parse(text) || Parser.numberParser.parse(text)) {
            commandHistory.add(text);
        } else {
            terminalText.add(text + " :was not found to be a command");
        }
    }

    public static void addText(String text) {
        terminalText.add(text);
    }

    public static void clear() {
        terminalText.clear();
    }

    public static void changeFontSize(int value) {
        fontSize += value;
        Font font = Resources.getFontInSize(fontSize);
        JNetVisualFX.gc.setFont(font);
        Text text1 = new Text("A");
        text1.setFont(font);
        characterWidth = (float) (text1.getLayoutBounds().getWidth());
        lineHeight = fontSize + 4;
    }

    public static void scrollCommandHistory() {
        Terminal.cursorOffsetLeft = 0;
        Terminal.currentText = new StringBuilder(Terminal.commandHistory.get(InputHandler.commandHistoryOffset));
    }


    public String testCommand(String command){
        Terminal.parseText(command);
        return terminalText.get(0);
    }
}
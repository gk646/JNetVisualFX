package gk646.jnet.userinterface.terminal;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.userinterface.userinput.InputHandler;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.StringUtil;
import gk646.jnet.util.datastructures.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public final class Terminal {
    public static final LimitedQueue<String> commandHistory = new LimitedQueue<>(20);
    static final ContainerHelper containerHelper = new ContainerHelper(0.3f, 74, 75, 26);
    private static final LimitedQueue<String> terminalText = new LimitedQueue<>(35);
    private static final Parser parser = new Parser();
    private static final CodeCompletion codeCompletion = new CodeCompletion();
    public static String terminalRoot = "> ";
    public static StringBuilder currentText = new StringBuilder();
    private int counter = 0;

    public Terminal() {
        terminalText.add("Welcome to JNetVisualFX! To get started use: \"new NetBuilder([4,4,4],sigmoid)\"");
        commandHistory.add("help");
    }

    public static void parseText(String text) {
        if (text.isBlank()) {
            terminalText.add(terminalRoot);
            return;
        }

        if (parser.parse(text) || Parser.numberParser.parse(text)) {
            commandHistory.add(text);
            UserStatistics.updateStat(UserStatistics.Stat.totalCommandsUsed, 1);
        } else {
            terminalText.add(text + " :was not found to be a command");
        }
    }

    public static void addText(String text) {
        terminalText.add(StringUtil.insertNewLines(text, TerminalInfo.maxCharsPerLine));
    }

    public static void clear() {
        terminalText.clear();
    }

    public static void scrollCommandHistory() {
        TerminalInfo.cursorOffsetLeft = 0;
        Terminal.currentText = new StringBuilder(Terminal.commandHistory.get(InputHandler.commandHistoryOffset));
    }

    public void updateSize() {
        terminalText.setLimit((containerHelper.getHeight() / TerminalInfo.lineHeight) - 1);
        TerminalInfo.maxCharsPerLine = (int) (containerHelper.getWidth() / TerminalInfo.characterWidth);
        recalculateLineBreaks();
    }

    public void draw(GraphicsContext gc) {
        drawBorder(gc);
        drawScrollingText(gc);
        drawActiveLine(gc);
        codeCompletion.draw(gc);
    }

    private void drawScrollingText(GraphicsContext gc) {
        if (!gc.getFill().equals(TerminalInfo.textColor)) {
            gc.setFill(TerminalInfo.textColor);
        }
        int startX = containerHelper.getDrawX();
        int startY = containerHelper.getDrawY() + containerHelper.getHeight() - TerminalInfo.lineHeight * 2;
        for (String s : terminalText) {
            startY -= StringUtil.countNewlines(s) * TerminalInfo.lineHeight;
            gc.fillText(s, startX, startY);
            startY -= TerminalInfo.lineHeight;
        }
    }

    private void drawBorder(GraphicsContext gc) {
        gc.setFont(TerminalInfo.activeFont);
        gc.setFill(TerminalInfo.backGround);
        gc.fillRoundRect(containerHelper.getDrawX() - 5, containerHelper.getDrawY(), containerHelper.getWidth() + 3, containerHelper.getHeight() + 3, 25, 25);

        gc.setFill(Colors.PHILIPINE_SILVER);
        gc.fillText("[Terminal]", containerHelper.getDrawX() + containerHelper.getWidth() - 100, containerHelper.getDrawY() + 20);
    }

    private void drawActiveLine(GraphicsContext gc) {
        String activeLine = terminalRoot + currentText.toString();
        int activeLineLength = activeLine.length();
        int insertionPoint = activeLineLength - TerminalInfo.cursorOffsetLeft;

        String cursorDisplay = (counter++ % 55 < 25) ? "|" : " ";

        if (TerminalInfo.cursorOffsetLeft > 0 && insertionPoint <= activeLineLength) {
            activeLine = new StringBuilder(activeLine).insert(insertionPoint, cursorDisplay).toString();
        } else {
            activeLine += cursorDisplay;
        }
        gc.fillText(activeLine, containerHelper.getDrawX(), containerHelper.getDrawY() + containerHelper.getHeight() - TerminalInfo.fontSize);
    }

    public void recalculateLineBreaks() {
        String string;
        for (int i = 0; i < terminalText.size(); i++) {
            string = terminalText.directGet(i);
            string = string.replace("\n", "");
            string = StringUtil.insertNewLines(string, TerminalInfo.maxCharsPerLine);
            terminalText.set(string, i);
        }
    }

    public String testCommand(String command) {
        Terminal.parseText(command);
        return terminalText.get(0);
    }

    public static class TerminalInfo {
        static  Color backGround = Colors.LIGHT_BLACK;
        public static int cursorOffsetLeft = 0;
        static Color textColor = Colors.MILK;
        static int maxCharsPerLine = 100;
        static double characterWidth = ContainerHelper.initCharacterWidth(15);
        private static int fontSize = 15;
        static int lineHeight = fontSize + 4;
        static Font activeFont = Resources.getFontInSize(fontSize);

        private TerminalInfo() {
        }

        public static int getFontSize() {
            return TerminalInfo.fontSize;
        }

        public static void changeFontSize(int value) {
            TerminalInfo.fontSize += value;
            TerminalInfo.activeFont = Resources.getFontInSize(TerminalInfo.fontSize);
            Text text1 = new Text("A");
            text1.setFont(TerminalInfo.activeFont);
            TerminalInfo.characterWidth = (float) (text1.getLayoutBounds().getWidth());
            TerminalInfo.lineHeight = TerminalInfo.fontSize + 4;
        }

        public static void setBackGround(Color backGround) {
            TerminalInfo.backGround = backGround;
        }

        public static void setTextColor(Color textColor) {
            TerminalInfo.textColor = textColor;
        }
    }
}
package gk646.jnet.userinterface.terminal;

import gk646.jnet.userinterface.JNetVisualFX;
import gk646.jnet.userinterface.userinput.InputHandler;
import gk646.jnet.util.ContainerHelper;
import gk646.jnet.util.LimitedQueue;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public final class Terminal {
    private static final byte MAX_LINES = 40;
    private static final byte LINE_HEIGHT = 17;
    private static String terminalRoot = "> ";
    private ContainerHelper terminal;
    private static final LimitedQueue<String> scrollingText = new LimitedQueue<>(MAX_LINES);
    private static final Parser parser = new Parser();


    public Terminal() {

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
        gc.setLineWidth(3);
        gc.fillRect(terminal.getDrawX(), terminal.getDrawY(), terminal.getWidth(), terminal.getHeight());
        gc.setLineWidth(1);
    }

    private void drawActiveLine(GraphicsContext gc) {
        gc.fillText(terminalRoot + InputHandler.currentText.toString(), terminal.getDrawX(), JNetVisualFX.bounds.y - 5);
    }

    public static void addText(String text) {
        if (!parser.parse(text)) {
            scrollingText.add(text + " :was not found to be a command");
        } else {
            scrollingText.add(text);
        }
    }

    public static void clear() {
        scrollingText.clear();
    }
}

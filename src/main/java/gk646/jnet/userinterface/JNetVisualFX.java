package gk646.jnet.userinterface;

import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.userinterface.terminal.Terminal;
import gk646.jnet.userinterface.userinput.InputHandler;
import gk646.jnet.util.ContainerHelper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

import java.awt.Point;

/**
 * The central class responsible for producing the visual output of JNetVisualFX. Composed of a {@link #terminal}, a
 * {@link #networkVisualizer} and a {@link #log}.
 */
public final class JNetVisualFX {
    final ContainerHelper terminalPos = new ContainerHelper(70, 0, 30, 100);
    final ContainerHelper networkPos = new ContainerHelper(0, 0, 70, 100);
    public static Point bounds;
    final Scene sceneRoot;
    final GraphicsContext gc;
    final Terminal terminal = new Terminal();
    final NetworkVisualizer networkVisualizer = new NetworkVisualizer();
    final Log log = new Log();
    final InputHandler inputHandler;

    final static Playground playground = new Playground();

    JNetVisualFX(Canvas canvas, InputHandler inputHandler, Scene scene) {
        bounds = new Point((int) canvas.getWidth(), (int) canvas.getHeight());
        this.gc = canvas.getGraphicsContext2D();
        this.inputHandler = inputHandler;
        this.sceneRoot = scene;

        gc.setFont(Resources.cascadiaCode);
    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> draw()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Thread updateThread = new Thread(() -> {
            while (true) {
                update();
                try {
                    Thread.sleep(8);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.start();
    }


    private void draw() {
        gc.clearRect(0, 0, bounds.x, bounds.y);

        networkVisualizer.draw(gc, networkPos);
        terminal.draw(gc, terminalPos);
    }


    private void update() {
        inputHandler.update();
        int width = (int) sceneRoot.getWidth();
        int height = (int) sceneRoot.getHeight();
        if (width != bounds.x) {
            bounds.x = width;
        }
        if (height != bounds.y) {
            bounds.y = height;
        }
    }
}

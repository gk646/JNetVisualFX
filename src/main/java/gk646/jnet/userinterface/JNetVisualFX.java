package gk646.jnet.userinterface;

import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.userinterface.terminal.Log;
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
    static {
        Terminal.containerHelper = new ContainerHelper(65, 50, 35, 50);
        Log.containerHelper = new ContainerHelper(65, 0, 35, 50);
        NetworkVisualizer.containerHelper = new ContainerHelper(0, 0, 60, 100);
    }

    final Canvas canvas;
    public static Point bounds;
    final Scene sceneRoot;
    public static GraphicsContext gc;
    final Terminal terminal;
    final NetworkVisualizer networkVisualizer;
    public static Log log = new Log();
    final InputHandler inputHandler;

    JNetVisualFX(Canvas canvas, InputHandler inputHandler, Scene scene) {
        bounds = new Point((int) scene.getWidth(), (int) scene.getHeight());
        gc = canvas.getGraphicsContext2D();
        this.inputHandler = inputHandler;
        this.canvas = canvas;
        this.sceneRoot = scene;

        terminal = new Terminal();
        networkVisualizer = new NetworkVisualizer();


        gc.setFont(Resources.cascadiaCode);
    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> draw()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        sceneRoot.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            bounds.x = newSceneWidth.intValue();
            canvas.setWidth(newSceneWidth.intValue());

            gc = canvas.getGraphicsContext2D();
            terminal.updateSize();
        });

        sceneRoot.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            bounds.y = newSceneHeight.intValue();
            canvas.setHeight(bounds.y);

            gc = canvas.getGraphicsContext2D();

            terminal.updateSize();
        });
    }


    private void draw() {
        gc.clearRect(0, 0, bounds.x, bounds.y);

        networkVisualizer.draw(gc);
        terminal.draw(gc);
        log.draw(gc);
    }
}

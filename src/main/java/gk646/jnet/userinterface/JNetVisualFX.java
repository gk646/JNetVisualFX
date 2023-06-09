package gk646.jnet.userinterface;

import gk646.jnet.userinterface.graphics.ColorPalette;
import gk646.jnet.userinterface.graphics.NetworkVisualizer;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.userinterface.terminal.Log;
import gk646.jnet.userinterface.terminal.Terminal;
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
    public static final Point bounds = new Point();
    static ColorPalette colorPalette;
    final Canvas canvas;
    final Scene sceneRoot;
    final Terminal terminal;
    final NetworkVisualizer networkVisualizer;
    final Log log;
    private GraphicsContext gc;

    JNetVisualFX(Canvas canvas, Scene scene) {
        bounds.x = (int) scene.getWidth();
        bounds.y = (int) scene.getHeight();

        gc = canvas.getGraphicsContext2D();
        this.canvas = canvas;
        this.sceneRoot = scene;

        terminal = new Terminal();
        log = new Log();
        networkVisualizer = new NetworkVisualizer();

        gc.setLineWidth(2);
        gc.setFont(Resources.cascadiaCode);

        ColorPalette.DEFAULT.loadPalette();

    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> draw()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        sceneRoot.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            bounds.x = newSceneWidth.intValue();
            canvas.setWidth(newSceneWidth.intValue());

            gc = canvas.getGraphicsContext2D();
            log.updateSize();
            terminal.updateSize();
        });

        sceneRoot.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            bounds.y = newSceneHeight.intValue();
            canvas.setHeight(bounds.y);

            gc = canvas.getGraphicsContext2D();

            log.updateSize();
            terminal.updateSize();
        });

        terminal.updateSize();
        log.updateSize();
    }

    private void draw() {

        networkVisualizer.draw(gc);
        terminal.draw(gc);
        log.draw(gc);
    }
}

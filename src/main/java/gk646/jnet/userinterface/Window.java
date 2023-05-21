package gk646.jnet.userinterface;

import gk646.jnet.userinterface.userinput.InputHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.Point;
import java.util.Objects;


public class Window extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     */
    @Override
    public void start(Stage stage) {
        //stage.initStyle(StageStyle.UNIFIED);    // can cause window to go white ?
        Point bounds = setupScreen();
        stage.setWidth(bounds.x);
        stage.setHeight(bounds.y);

        stage.setTitle("JNetVisualFX - Playground");
        stage.setResizable(true);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/JNetIcon.png"))));

        Canvas canvas = new Canvas(stage.getWidth(), stage.getHeight());
        Group root = new Group();
        root.getChildren().add(canvas);

        final Scene sceneRoot = new Scene(root);
        stage.setScene(sceneRoot);
        stage.show();

        final var inputHandler = new InputHandler();
        new JNetVisualFX(canvas, sceneRoot).run();

        sceneRoot.setOnKeyTyped(inputHandler::handleKeyType);
        sceneRoot.setOnKeyPressed(inputHandler::handleSpecialKeyType);
        sceneRoot.setOnKeyReleased(inputHandler::handleSpecialKeyLift);
    }

    public static void exit() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Platform.exit();
        }).start();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    private Point setupScreen() {
        Point temp = new Point();
        Screen screen = Screen.getPrimary();
        temp.x = screen.getBounds().getMaxX() > 1280 ? 1280 : (int) screen.getBounds().getMaxX();
        temp.y = screen.getBounds().getMaxY() > 960 ? 960 : (int) screen.getBounds().getMaxY();

        return temp;
    }
}

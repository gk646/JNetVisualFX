package gk646.jnet.userinterface;

import gk646.jnet.userinterface.userinput.InputHandler;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.Point;
import java.io.IOException;


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
    public void start(Stage stage) throws IOException {
        Point bounds = setupScreen();
        stage.setWidth(bounds.x);
        stage.setHeight(bounds.y);

        stage.setTitle("JNetVisualFX - Playground");

        //VBox root = FXMLLoader.load(Window.class.getResource("/layout.fxml"));

        Canvas canvas = new Canvas(bounds.x, bounds.y);

        Group root = new Group();
        root.getChildren().add(canvas);

        final Scene sceneRoot = new Scene(root);
        stage.setScene(sceneRoot);

        stage.show();

        final var inputHandler = new InputHandler();

        new JNetVisualFX(canvas, inputHandler, sceneRoot).run();


        sceneRoot.setOnKeyTyped(inputHandler::handleKeyType);
        sceneRoot.setOnKeyPressed(inputHandler::handleSpecialKeyType);
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

package gk646.jnet.userinterface.exercise;

import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ExerciseWindow {
    public static final int EXERCISE_WIDTH = 400;
    public static final int EXERCISE_HEIGHT = 660;
    static ExerciseWindow instance;
    static Exercise activeExercise;

    ExerciseWindow(Exercise exercise) {
        activeExercise = exercise;
        activeExercise.resetHints();
    }


    public static void create(Exercise exercise) {
        if (instance != null) return;

        instance = new ExerciseWindow(exercise);
        Stage secondaryStage = new Stage(StageStyle.UNIFIED);

        secondaryStage.setTitle("Exercise " + exercise + " Info");
        secondaryStage.setWidth(EXERCISE_WIDTH);
        secondaryStage.setHeight(EXERCISE_HEIGHT);
        secondaryStage.setResizable(false);
        Group group = new Group();
        Canvas canvas = new Canvas(EXERCISE_WIDTH, EXERCISE_HEIGHT);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Resources.cascadiaCode12);
        gc.setFill(Colors.MILK);
        gc.fillRect(0, 0, EXERCISE_WIDTH, EXERCISE_HEIGHT);
        gc.setFill(Colors.LIGHT_BLACK);
        gc.fillText(exercise.info, 2, 10);
        group.getChildren().add(canvas);

        Scene scene = new Scene(group);
        secondaryStage.setScene(scene);
        secondaryStage.show();


        secondaryStage.setOnCloseRequest(event -> instance.close());
    }

    public static boolean isOpen() {
        return instance != null;
    }

    public static void test() {
        activeExercise.test();
    }

    public static void getHint() {
        activeExercise.getHint();
    }

    private void close() {
        instance = null;
    }
}

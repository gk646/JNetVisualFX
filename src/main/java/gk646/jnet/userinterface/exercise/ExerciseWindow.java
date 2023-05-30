package gk646.jnet.userinterface.exercise;

import gk646.jnet.localdata.files.UserStatistics;
import gk646.jnet.userinterface.graphics.Colors;
import gk646.jnet.userinterface.graphics.Resources;
import gk646.jnet.userinterface.terminal.Terminal;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ExerciseWindow {
    public static final int EXERCISE_WIDTH = 400;
    public static final int EXERCISE_HEIGHT = 670;
    static Exercise activeExercise;
    static Stage activeStage;

    private ExerciseWindow() {

    }


    public static boolean create(Exercise exercise) {
        if (activeStage != null) {
            if (!activeExercise.equals(exercise)) {
                activeStage.close();
                activeStage = null;
                create(exercise);
                return false;
            }
            return false;
        }

        activeExercise = exercise;
        activeStage = new Stage(StageStyle.UNIFIED);

        activeStage.setTitle("Exercise " + exercise + " Info");
        activeStage.setWidth(EXERCISE_WIDTH);
        activeStage.setHeight(EXERCISE_HEIGHT);
        activeStage.setResizable(false);
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
        activeStage.setScene(scene);
        activeStage.show();

        activeStage.setOnCloseRequest(event -> activeStage = null);

        return true;
    }

    public static boolean isOpen() {
        return activeStage != null;
    }

    public static void test() {
        double error = activeExercise.test();
        if (Math.abs(error) < 0.01) {
            Terminal.addText("Tests passed! You successfully trained a network to solve equality!");
            UserStatistics.updateStat(UserStatistics.Stat.exercisesFinished, 1);
        } else if (Math.abs(error) < 0.2) {
            Terminal.addText("Tests not passed! Your models accuracy was too low, but close!");
        } else {
            Terminal.addText("Tests not passed! Your models accuracy was too low!");
        }
    }

    public static void getHint() {
        activeExercise.getHint();
    }

    public static void close() {
        if (activeStage != null) activeStage.close();
    }
}

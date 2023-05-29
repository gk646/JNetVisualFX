package gk646.jnet.userinterface.exercise;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ExerciseWindow {
    public static ExerciseWindow instance;
    static Exercise activeExercise;

    ExerciseWindow(Exercise exercise) {
        activeExercise = exercise;
    }


    public static void create(Exercise exercise) {
        if (instance != null) return;
        instance = new ExerciseWindow(exercise);
        Stage secondaryStage = new Stage(StageStyle.UNIFIED);

        secondaryStage.setTitle("Exercise " + exercise + " Info");
        secondaryStage.setWidth(250);
        secondaryStage.setHeight(350);


        secondaryStage.show();


        secondaryStage.setOnCloseRequest(event->{
            instance.close();
        });
    }

    void close(){
        instance = null;
    }



}

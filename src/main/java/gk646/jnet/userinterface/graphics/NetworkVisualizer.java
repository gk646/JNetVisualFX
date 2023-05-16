package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class NetworkVisualizer {
    private ContainerHelper containerHelper;

    private static byte circleDiameter = 15;
    private static short offsetX = 200;
    private static short offsetY = 200;
    private static short verticalSpacing = 50;
    private static short horizontalSpacing = 150;


    public NetworkVisualizer() {

    }


    public void draw(GraphicsContext gc, ContainerHelper containerHelper) {
        this.containerHelper = containerHelper;
    }


    private void drawNetwork(GraphicsContext gc) {
        if (Playground.neuralNetwork == null) return;

        gc.setFill(Color.ROSYBROWN);
        short[] netDimensions = Playground.neuralNetwork.getNetwork().layerInfo;

        int startX = containerHelper.getDrawX() - offsetX;
        int startY = containerHelper.getDrawY() - offsetY;

        for (byte i = 0; i < netDimensions.length; i++) {
            for (byte j = 0; j < netDimensions[i]; j++) {
                gc.fillOval(startX, startY, circleDiameter, circleDiameter);
                startY += verticalSpacing;
            }
            startX += horizontalSpacing;
        }
    }
}

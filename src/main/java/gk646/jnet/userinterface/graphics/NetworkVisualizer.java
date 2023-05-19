package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public final class NetworkVisualizer {
    public static ContainerHelper containerHelper;
    public static float MAX_CIRCLE_DIAMETER = 30;
    public static float circleDiameter = 15;
    public static float MIN_CIRCLE_DIAMETER = 7;
    public static short offsetX = 0;
    public static int offsetY = 0;
    public static short verticalSpacing = 50;
    public static short horizontalSpacing = 75;
    private static int drawStartX = 500;
    private static int drawStartY = 500;
    private Color neuronColor = Colors.ICE_BERG;

    private Color backGround = Colors.MILK;

    public NetworkVisualizer() {
    }


    public void draw(GraphicsContext gc) {
        drawBackGround(gc);
        drawNetwork(gc);
    }


    private void calculateCircleDiameter() {
        circleDiameter = Math.max(MIN_CIRCLE_DIAMETER, Math.min(MAX_CIRCLE_DIAMETER, containerHelper.getWidth() / 30));
    }

    private void calculateSpacing() {
        int maxNeurons = Arrays.stream(Playground.neuralNetwork.getNetwork().layerInfo).max().orElse(0);

        verticalSpacing = (short) ((containerHelper.getHeight() - circleDiameter) / (maxNeurons + 1));
        horizontalSpacing = (short) ((containerHelper.getWidth() - circleDiameter) / (Playground.neuralNetwork.getNetwork().layerInfo.length + 1));
    }

    private int getLayerStartY(int neuronCount) {
        if (neuronCount == 1) return (int) (containerHelper.getHeight() / 2 - circleDiameter / 2 + offsetY);
        int totalSpaceNeededY = (neuronCount - 1) * verticalSpacing;
        return (int) ((containerHelper.getHeight() - totalSpaceNeededY) / 2 - circleDiameter / 2 + offsetY);
    }

    private int getLayerStartX(int layerIndex) {
        return (int) ((layerIndex + 1) * horizontalSpacing - circleDiameter / 2 + offsetX);
    }

    private void drawNetwork(GraphicsContext gc) {
        if (Playground.neuralNetwork == null) return;

        calculateCircleDiameter();
        calculateSpacing();

        gc.setFill(neuronColor);
        int[] netDimensions = Playground.neuralNetwork.getNetwork().layerInfo;

        for (int i = 0; i < netDimensions.length; i++) {
            int neuronCount = netDimensions[i];
            int startY = getLayerStartY(neuronCount);
            int startX = getLayerStartX(i);

            for (int j = 0; j < neuronCount; j++) {
                gc.fillOval(startX, startY + j * verticalSpacing, circleDiameter, circleDiameter);
            }
        }
    }

    private void drawBackGround(GraphicsContext gc) {
        gc.setFill(backGround);
        gc.fillRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth(), containerHelper.getHeight());
    }
}

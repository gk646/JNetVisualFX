package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public final class NetworkVisualizer {
    public static final ContainerHelper containerHelper = new ContainerHelper(0, 0, 60, 100);
    public static float MAX_CIRCLE_DIAMETER = 30;
    float circleDiameter = 15;
    final float MIN_CIRCLE_DIAMETER = 7;
    static short offsetX = 0;
    static int offsetY = 0;
    static short verticalSpacing = 50;
    static short horizontalSpacing = 75;
    int drawStartX = 500;
    int drawStartY = 500;
    static final Color neuronColor = Colors.ICE_BERG;
    static final Color backGround = Colors.MILK;

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

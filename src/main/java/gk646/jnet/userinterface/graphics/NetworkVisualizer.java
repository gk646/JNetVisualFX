package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class NetworkVisualizer {
    public static final ContainerHelper containerHelper = new ContainerHelper(0, 0, 76, 75);
    static final float MIN_CIRCLE_DIAMETER = 10;
    static final Color neuronColor = Colors.ICE_BERG;
    static final Color backGround = Colors.WHITE_SMOKE;
    public static float maxCircleDiameter = 35;
    static short offsetX = 0;
    static int offsetY = 0;
    static short verticalSpacing = 50;
    static short horizontalSpacing = 75;
    static int maxNeuronCountInLayers = 3;
    float circleDiameter = 15;

    public NetworkVisualizer() {
    }

    public static void setMaxNeurons(int val) {
        maxNeuronCountInLayers = val;
    }

    public void draw(GraphicsContext gc) {
        drawBackGround(gc);
        drawNetwork(gc);
    }

    private void calculateCircleDiameter() {
        circleDiameter = Math.max(MIN_CIRCLE_DIAMETER, Math.min(maxCircleDiameter, containerHelper.getWidth() / 30));
    }

    private void calculateSpacing() {
        verticalSpacing = (short) ((containerHelper.getHeight() - circleDiameter) / (maxNeuronCountInLayers + 1));
        horizontalSpacing = (short) ((containerHelper.getWidth() - circleDiameter) / (Playground.neuralNetwork.getBounds().length + 1));
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
        gc.setStroke(Colors.PASTEL_GREY);
        gc.setFill(neuronColor);
        int[] netDimensions = Playground.neuralNetwork.getBounds();

        int[] startXs = new int[netDimensions.length];
        int[][] startYs = new int[netDimensions.length][];

        for (int i = 0; i < netDimensions.length; i++) {
            int neuronCount = netDimensions[i];
            startXs[i] = getLayerStartX(i);
            startYs[i] = new int[neuronCount];

            for (int j = 0; j < neuronCount; j++) {
                startYs[i][j] = getLayerStartY(neuronCount) + j * verticalSpacing;

                if (i > 0) {
                    for (int k = 0; k < netDimensions[i - 1]; k++) {
                        gc.strokeLine(startXs[i] + circleDiameter / 2, startYs[i][j] + circleDiameter / 2,
                                startXs[i - 1] + circleDiameter / 2, startYs[i - 1][k] + circleDiameter / 2);
                    }
                }
            }
        }

        for (int i = 0; i < netDimensions.length; i++) {
            for (int j = 0; j < netDimensions[i]; j++) {
                gc.fillOval(startXs[i], startYs[i][j], circleDiameter, circleDiameter);
            }
        }
    }

    private void drawBackGround(GraphicsContext gc) {
        gc.setFill(backGround);
        gc.fillRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth(), containerHelper.getHeight());
    }
}

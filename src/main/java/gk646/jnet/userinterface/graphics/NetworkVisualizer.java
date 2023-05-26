package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Arrays;

public final class NetworkVisualizer {
    public static final ContainerHelper containerHelper = new ContainerHelper(0, 0, 76, 75);
    static final float MIN_CIRCLE_DIAMETER = 10;
    static final Color neuronColor = Colors.ICE_BERG;
    static final Color backGround = Colors.WHITE_SMOKE;
    public static float maxCircleDiameter = 35;
    public static short[][][] activeConnection;
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

    public static void updateConnectionMatrix(int[] layerInfo) {
        activeConnection = new short[layerInfo.length - 1][][];
        for (int i = 1; i < layerInfo.length; i++) {
            activeConnection[i - 1] = new short[layerInfo[i]][layerInfo[i - 1]];
        }
    }

    public void draw(GraphicsContext gc) {
        drawBackGround(gc);
        drawInfo(gc);
        drawNetworkLines(gc);
        drawNetworkCircles(gc);
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

    private void drawNetworkLines(GraphicsContext gc) {
        if (Playground.neuralNetwork == null) return;

        calculateCircleDiameter();
        calculateSpacing();
        gc.setStroke(Colors.PASTEL_GREY);
        int[] netDimensions = Playground.neuralNetwork.getBounds();

        int startY;
        int startX = (int) (getLayerStartX(1) + circleDiameter / 2);
        int previousX;
        int previousY;
        int neuronCount;
        for (int i = 1; i < netDimensions.length; i++) {
            neuronCount = netDimensions[i];
            startY = (int) (getLayerStartY(neuronCount) + circleDiameter / 2);
            for (int j = 0; j < neuronCount; j++) {
                previousY = (int) (getLayerStartY(netDimensions[i - 1]) + circleDiameter / 2);
                previousX = (int) (getLayerStartX(i - 1) + circleDiameter / 2);
                for (int k = 0; k < netDimensions[i - 1]; k++) {
                    if (activeConnection[i - 1][j][k] > 0) {
                        gc.setStroke(Colors.RED);
                        gc.strokeLine(startX, startY, previousX, previousY);
                        gc.setStroke(Colors.PASTEL_GREY);
                        activeConnection[i - 1][j][k]--;
                    } else if (activeConnection[i - 1][j][k] < 0) {
                        gc.setStroke(Colors.ICE_BERG);
                        gc.strokeLine(startX, startY, previousX, previousY);
                        gc.setStroke(Colors.PASTEL_GREY);
                        activeConnection[i - 1][j][k]++;
                    } else {
                        gc.strokeLine(startX, startY, previousX, previousY);
                    }
                    previousY += verticalSpacing;
                }
                startY += verticalSpacing;
            }
            startX += horizontalSpacing;
        }
    }

    private void drawNetworkCircles(GraphicsContext gc) {
        if (Playground.neuralNetwork == null) return;

        gc.setFill(neuronColor);
        int[] netDimensions = Playground.neuralNetwork.getBounds();
        int startX = getLayerStartX(0);
        int startY;
        for (final int netDimension : netDimensions) {
            startY = getLayerStartY(netDimension);
            for (int j = 0; j < netDimension; j++) {
                gc.fillOval(startX, startY, circleDiameter, circleDiameter);
                startY += verticalSpacing;
            }
            startX += horizontalSpacing;
        }
    }

    private void drawBackGround(GraphicsContext gc) {
        gc.setFill(backGround);
        gc.fillRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth(), containerHelper.getHeight());
    }

    private void drawInfo(GraphicsContext gc) {
        gc.setFill(Colors.INTELLIJ_GREY);
        if (Playground.networkBuilder == null) {
            gc.fillText("NetBuilder: null", containerHelper.getDrawX() + containerHelper.getWidth() - 150, containerHelper.getDrawY() + 25);
        } else {
            gc.fillText(Playground.networkBuilder.toString(),containerHelper.getDrawX()+25,containerHelper.getDrawY() + 25);
        }
    }
}

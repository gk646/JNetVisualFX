package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.JNetVisualFX;
import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class NetworkVisualizer {
    public static ContainerHelper containerHelper;
    public static byte circleRadius = 20;
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


    private void drawNetwork(GraphicsContext gc) {
        if (Playground.neuralNetwork == null) return;

        gc.setFill(neuronColor);
        short[] netDimensions = Playground.neuralNetwork.getNetwork().layerInfo;

        int startX = getLayerCenterX(netDimensions.length);

        for (short neuronCount : netDimensions) {
            int startY = getLayerCenterY(neuronCount);
            for (int j = 0; j < neuronCount; j++) {
                gc.fillOval(startX, startY, circleRadius, circleRadius);
                startY += verticalSpacing;
            }
            startX += horizontalSpacing;
        }
    }

    private void drawBackGround(GraphicsContext gc) {
        gc.setFill(backGround);
        gc.fillRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth(),containerHelper.getHeight());
    }

    private static int getLayerCenterY(short neuronCount) {
        if (neuronCount == 1) return JNetVisualFX.bounds.y / 2 + circleRadius + offsetY;

        int totalSpaceNeededY = (neuronCount * circleRadius * 2) + ((neuronCount - 1) * verticalSpacing);
        int startY = JNetVisualFX.bounds.y / 2 - totalSpaceNeededY / 2;

        return startY - circleRadius + offsetY;
    }

    private int getLayerCenterX(int layerCount) {
        if (layerCount == 1) return containerHelper.getWidth() / 2 - circleRadius + offsetX;

        int totalSpaceNeededX = (layerCount * circleRadius * 2) + ((layerCount - 1) * horizontalSpacing);
        int startX = containerHelper.getWidth() / 2 - totalSpaceNeededX / 2;

        return startX - circleRadius + offsetX;
    }

    public static void updateSize() {
        if (Playground.neuralNetwork == null) return;
    }
}

package gk646.jnet.userinterface.graphics;

import gk646.jnet.userinterface.terminal.Playground;
import gk646.jnet.util.ContainerHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class NetworkVisualizer {
    public static ContainerHelper containerHelper;
    public static float circleDiameter = 20;
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
                gc.fillOval(startX, startY, circleDiameter, circleDiameter);
                startY += verticalSpacing;
            }
            startX += horizontalSpacing;
        }
        containerHelper.drawDebugLines(gc);
    }

    private void drawBackGround(GraphicsContext gc) {
        gc.setFill(backGround);
        gc.fillRect(containerHelper.getDrawX(), containerHelper.getDrawY(), containerHelper.getWidth(), containerHelper.getHeight());
    }

    private static int getLayerCenterY(short neuronCount) {
        if (neuronCount == 1) return (int) (containerHelper.getHeight() / 2 - circleDiameter / 2 + offsetY);

        int totalSpaceNeededY = ((neuronCount - 1) * verticalSpacing);
        while (totalSpaceNeededY >= containerHelper.getHeight() - 50) {
            if (circleDiameter > 7) {
                circleDiameter -= 0.2f;
            }
            verticalSpacing -= 1;
            totalSpaceNeededY = ((neuronCount - 1) * verticalSpacing);
        }

        while (totalSpaceNeededY < containerHelper.getHeight() - 75) {
            if (circleDiameter < 20) {
                circleDiameter += 0.2f;
            }
            verticalSpacing += 1;
            totalSpaceNeededY = ((neuronCount - 1) * verticalSpacing);
        }

        int startY = containerHelper.getHeight() / 2 - totalSpaceNeededY / 2;

        return (int) (startY - circleDiameter / 2 + offsetY);
    }

    private int getLayerCenterX(int layerCount) {
        if (layerCount == 1) return (int) (containerHelper.getWidth() / 2 - circleDiameter / 2 + offsetX);

        int totalSpaceNeededX = ((layerCount - 1) * horizontalSpacing);

        while (totalSpaceNeededX > containerHelper.getWidth() - 100) {
            if (circleDiameter > 7) {
                circleDiameter -= 0.05f;
            }
            horizontalSpacing -= 2;
            totalSpaceNeededX = ((layerCount - 1) * horizontalSpacing);
        }
        while (totalSpaceNeededX < containerHelper.getWidth() - 120) {
            if (circleDiameter < 20) {
                circleDiameter += 0.1f;
            }
            if (horizontalSpacing <= 250) {
                horizontalSpacing += 1;
            } else {
                break;
            }
            totalSpaceNeededX = ((layerCount - 1) * horizontalSpacing);
        }
        int startX = containerHelper.getWidth() / 2 - totalSpaceNeededX / 2;

        return (int) (startX - circleDiameter / 2 + offsetX);
    }

    public static void updateSize() {
        if (Playground.neuralNetwork == null) return;
    }
}

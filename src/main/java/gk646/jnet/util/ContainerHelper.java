package gk646.jnet.util;

import gk646.jnet.userinterface.JNetVisualFX;
import javafx.scene.canvas.GraphicsContext;


/**
 * Helper class to scale containers to the windows size according to the global {@link JNetVisualFX#bounds}.
 */
public final class ContainerHelper {
    private int percentX;
    private int percentY;
    private int width;
    private int height;


    /**
     * Takes percent values relative to  {@link JNetVisualFX#bounds}.
     *
     * @param beginPositionPercentX the topLeft x coordinate in percent of the bounds
     * @param beginPositionPercentY the topLeft y coordinate in percent of the bounds
     * @param widthPercent          the width in percent of the bounds
     * @param heightPercent         the height in percent of the bounds
     */
    public ContainerHelper(int beginPositionPercentX, int beginPositionPercentY, int widthPercent, int heightPercent) {
        this.percentX = beginPositionPercentX;
        this.percentY = beginPositionPercentY;
        this.width = widthPercent;
        this.height = heightPercent;
    }


    public int getDrawX() {
        return (int) (JNetVisualFX.bounds.x / 100f * percentX);
    }

    public int getDrawY() {
        return (int) (JNetVisualFX.bounds.y / 100f * percentY);
    }

    public int getWidth() {
        return (int) (JNetVisualFX.bounds.x / 100f * width);
    }

    public int getHeight() {
        return (int) (JNetVisualFX.bounds.y / 100f * height);
    }

    public void drawDebugLines(GraphicsContext gc) {
        gc.setLineWidth(4);
        gc.strokeLine(this.getWidth() / 2, this.getDrawY(), this.getWidth() / 2, this.getHeight());

        gc.strokeLine(this.getDrawX(), this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
        gc.setLineWidth(1);
    }
}

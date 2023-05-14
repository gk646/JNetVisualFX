package gk646.jnet.util;

import gk646.jnet.userinterface.JNetVisualFX;

import java.awt.Point;

import static gk646.jnet.userinterface.JNetVisualFX.bounds;

/**
 * Helper class to scale containers to the windows size according to the global {@link JNetVisualFX#bounds}.
 */
public final class ContainerHelper {
    public short percentX;
    public short percentY;
    public short width;
    public short height;


    /**
     * Takes percent values relative to  {@link JNetVisualFX#bounds}.
     *
     * @param beginPositionPercentX the topLeft x coordinate in percent of the bounds
     * @param beginPositionPercentY the topLeft y coordinate in percent of the bounds
     * @param widthPercent          the width in percent of the bounds
     * @param heightPercent         the height in percent of the bounds
     */
    public ContainerHelper(int beginPositionPercentX, int beginPositionPercentY, int widthPercent, int heightPercent) {
        this.percentX = (short) beginPositionPercentX;
        this.percentY = (short) beginPositionPercentY;
        this.width = (short) widthPercent;
        this.height = (short) heightPercent;
    }


    public short getDrawX() {
        return (short) (bounds.x / 100f * percentX);
    }

    public short getDrawY() {
        return (short) (bounds.y / 100f * percentY);
    }
    public short getWidth() {
        return (short) (bounds.x / 100f * width);
    }

    public short getHeight() {
        return (short) (bounds.y / 100f * height);
    }

}

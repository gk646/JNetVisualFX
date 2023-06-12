package gk646.jnet.util;

import gk646.jnet.Main;

public final class NumberUtil {
    private NumberUtil() {
    }
    public static int[] arrayToInt(double[] d) {
        int[] temp = new int[d.length];
        for (int i = 0; i < d.length; i++) {
            temp[i] = (int) d[i];
        }
        return temp;
    }

    public static int getNewTotalTime(Number totalSeconds) {
        long difference = System.nanoTime() - Main.STARTUP_TIME;
        long diffSeconds = difference / 1_000_000_000;
        return (int) (diffSeconds + totalSeconds.intValue());
    }
}

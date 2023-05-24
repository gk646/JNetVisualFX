package gk646.jnet.util;

import gk646.jnet.Main;

public class NumberUtil {
    private NumberUtil(){}
    public static int toInt(double d) {
        return (int) d;
    }

    public static int[] arrayToInt(double[] d) {
        int[] temp = new int[d.length];
        for (int i = 0; i < d.length; i++) {
            temp[i] = (int) d[i];
        }
        return temp;
    }

    public static int getNewTotalTime(Number totalSeconds) {
        long difference = System.nanoTime() - Main.startUpTime;
        long diffSeconds = difference / 1_000_000_000;
        return (int) (diffSeconds + totalSeconds.intValue());
    }
}

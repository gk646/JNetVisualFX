package gk646.jnet.util;

public class NumberUtil {


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
}

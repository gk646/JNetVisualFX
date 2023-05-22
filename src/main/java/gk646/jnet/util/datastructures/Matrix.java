package gk646.jnet.util.datastructures;

import java.util.Arrays;

public class Matrix {
    double[][] arr;

    private Matrix(double[][] arr) {
        this.arr = new double[arr.length][arr[0].length];
        System.arraycopy(arr, 0, this.arr, 0, arr.length);
    }


    public static Matrix fromArray(double[][] arr) {
        return new Matrix(arr);
    }

    @Override
    public String toString() {
        var res = new StringBuilder();
        for (double[] arr : arr) {
            res.append(Arrays.toString(arr));
            res.append("\n");
        }
        return res.toString();
    }
}

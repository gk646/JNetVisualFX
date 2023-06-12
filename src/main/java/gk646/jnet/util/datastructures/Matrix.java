package gk646.jnet.util.datastructures;

import gk646.jnet.userinterface.terminal.Log;

import java.util.Arrays;

public final class Matrix {
    private static final double EPSILON = 1e-5;
    private final int rows;
    private final int cols;
    private final double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matrix(double[][] arr) {
        this.rows = arr.length;
        this.cols = arr[0].length;
        this.data = arr;
    }

    public Matrix(double[] arr) {
        this.rows = 1;
        this.cols = arr.length;
        this.data = new double[1][this.cols];
        System.arraycopy(arr, 0, data[0], 0, arr.length);
    }

    public void set(int row, int col, double value) {
        data[row][col] = value;
    }

    public double get(int row, int col) {
        return data[row][col];
    }

    public double[] getRow(int row) {
        if (row > rows) {
            Log.logger.logException(IllegalArgumentException.class, "row index out of bounds!");
        }
        return data[row];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double[][] getRawData() {
        return data;
    }

    public Matrix multiply(Matrix o) {
        double[][] otherData = o.data;

        if (this.data[0].length != otherData.length) {
            throw new IllegalArgumentException("Matrices cannot be multiplied: number of columns in the first matrix doesn't match the number of rows in the second.");
        }

        double[][] result = new double[this.data.length][otherData[0].length];

        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < otherData[0].length; j++) {
                for (int k = 0; k < this.data[0].length; k++) {
                    result[i][j] += this.data[i][k] * otherData[k][j];
                }
            }
        }

        return new Matrix(result);
    }

    public Matrix transpose() {
        double[][] result = new double[this.data[0].length][this.data.length];
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                result[j][i] = this.data[i][j];
            }
        }

        return new Matrix(result);
    }
    public static Matrix elementWiseMult(Matrix m, Matrix o) {
        double[][] mData = m.data;
        double[][] oData = o.data;

        if (mData.length != oData.length || mData[0].length != oData[0].length) {
            throw new IllegalArgumentException("Matrices should have the same dimensions for element-wise multiplication");
        }

        double[][] result = new double[mData.length][mData[0].length];

        for (int i = 0; i < mData.length; i++) {
            for (int j = 0; j < mData[0].length; j++) {
                result[i][j] = mData[i][j] * oData[i][j];
            }
        }

        return new Matrix(result);
    }

    public boolean isOneDimensional() {
        return this.rows == 1;
    }

    public boolean isEmpty() {
        return rows == 0 && cols == 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (rows > 1) {
            builder.append("[");
            for (double[] doubles : data) {
                builder.append("[");
                for (double value : doubles) {
                    if (Math.abs((int) value - value) < EPSILON) {
                        builder.append((int) value).append(",");
                    } else {
                        builder.append(value).append(",");
                    }
                }
                builder.setLength(builder.length() - 1);
                builder.append("],");
            }
            builder.setLength(builder.length() - 1);
            builder.append("]");
        } else if (rows == 1) {
            builder.append("[");
            for (double value : data[0]) {
                if (Math.abs((int) value - value) < EPSILON) {
                    builder.append((int) value).append(",");
                } else {
                    builder.append(value).append(",");
                }
            }
            builder.setLength(builder.length() - 1);
            builder.append("]");
        } else {
            builder.append("[]");
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (double[] arr : data) {
            for (double num : arr) {
                hash += num;
            }
            hash -= arr.length;
        }
        hash /= cols;
        hash *= rows;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof final Matrix other)) {
            return false;
        }
        return (Arrays.deepEquals(other.data, this.data));
    }
}

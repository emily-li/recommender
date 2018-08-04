package com.liemily.math;

import java.util.Arrays;

public class Matrix {
    private final double[][] data;

    public Matrix(final double[][] data) {
        this.data = data;
    }

    public double get(final int row, final int col) {
        return data[row][col];
    }

    public int width() {
        return data.length;
    }

    public int height() {
        return data[0].length;
    }

    public double[] getCol(int i) {
        return Arrays.stream(data).mapToDouble(line -> line[i]).toArray();
    }

    public void set(final int colIdx, final int rowIdx, final double val) {
        data[rowIdx][colIdx] = val;
    }

    public void set(final int i, final double[] row) {
        data[i] = row;
    }
}

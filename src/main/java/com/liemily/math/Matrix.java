package com.liemily.math;

import org.ejml.simple.SimpleMatrix;

import java.util.Iterator;

public class Matrix {
    private final SimpleMatrix matrix;

    public Matrix(final double[][] matrix) {
        this.matrix = new SimpleMatrix(matrix);
    }

    public Matrix(final SimpleMatrix matrix) {
        this.matrix = matrix;
    }

    public double get(final int row, final int col) {
        return matrix.get(row, col);
    }

    public int width() {
        return matrix.numCols();
    }

    public int height() {
        return matrix.numRows();
    }

    public double[] getCol(int colIdx) {
        final SimpleMatrix simpleCol = matrix.cols(colIdx, colIdx + 1);
        final Iterator<Double> colIterator = simpleCol.iterator(true, 0, 0, simpleCol.numRows() - 1, 0);

        final double[] colArray = new double[simpleCol.numRows()];
        for (int i = 0; colIterator.hasNext(); i++) {
            colArray[i] = colIterator.next();
        }
        return colArray;
    }

    public void set(final int colIdx, final int rowIdx, final double val) {
        matrix.set(rowIdx, colIdx, val);
    }

    public void set(final int i, final double[] row) {
        matrix.setRow(i, 0, row);
    }

    public SimpleMatrix getMatrix() {
        return matrix;
    }
}

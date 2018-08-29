package com.liemily.math;

import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;

public class Matrix {
    private BasicMatrix matrix;

    public Matrix(final double[][] matrix) {
        this.matrix = PrimitiveMatrix.FACTORY.rows(matrix);
    }

    public Matrix(final BasicMatrix matrix) {
        this.matrix = matrix;
    }

    public double get(final int row, final int col) {
        return matrix.get(row, col).doubleValue();
    }

    public double[] getCol(int colIdx) {
        final BasicMatrix col = matrix.selectColumns(colIdx);
        return col.toRawCopy1D();
    }

    public double[] getRow(int colIdx) {
        final BasicMatrix row = matrix.selectRows(colIdx);
        return row.toRawCopy1D();
    }

    public void set(final int rowIdx, final int colIdx, final double val) {
        final double[][] raw = matrix.toRawCopy2D();
        raw[rowIdx][colIdx] = val;
        matrix = PrimitiveMatrix.FACTORY.rows(raw);
    }

    public BasicMatrix getMatrix() {
        return matrix;
    }
}

package com.liemily.math;

import org.ojalgo.matrix.BasicMatrix;

public class MatrixCalculator {
    public Matrix multiply(final Matrix matrix1, final Matrix matrix2) {
        final BasicMatrix m1 = matrix1.getMatrix();
        final BasicMatrix m2 = matrix2.getMatrix();
        final BasicMatrix multiplied = m1.multiplyElements(m2);
        return new Matrix(multiplied);
    }
}

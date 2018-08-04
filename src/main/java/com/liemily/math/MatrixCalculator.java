package com.liemily.math;

import org.ejml.simple.SimpleMatrix;

public class MatrixCalculator {
    public Matrix multiply(final Matrix matrix1, final Matrix matrix2) {
        final SimpleMatrix m1 = matrix1.getMatrix();
        final SimpleMatrix m2 = matrix2.getMatrix();

        return new Matrix(m1.mult(m2));
    }
}

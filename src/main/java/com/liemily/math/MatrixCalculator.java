package com.liemily.math;

import java.util.Arrays;

public class MatrixCalculator {
    public Matrix multiply(final Matrix matrix1, final Matrix matrix2) {
        double[][] multiplied = new double[matrix1.width()][matrix2.height()];
        Arrays.stream(multiplied).forEach(row -> Arrays.fill(row, 0.0));

        for (int i = 0; i < matrix1.width(); i++) {
            for (int j = 0; j < matrix2.height(); j++) {
                for (int k = 0; k < matrix1.height(); k++) {
                    multiplied[i][j] += matrix1.get(i, k) * matrix2.get(k, j);
                }
            }
        }
        return new Matrix(multiplied);
    }
}

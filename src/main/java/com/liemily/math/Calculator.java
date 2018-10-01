package com.liemily.math;

import org.apache.commons.math3.stat.inference.TTest;
import org.ojalgo.matrix.BasicMatrix;

import java.util.Arrays;

public class Calculator {
    public double sigmoid(final double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double reverseSigmoid(final double x) {
        return Math.log(x / (1 - x));
    }

    public double cosineSimilarity(final double[] v1, final double[] v2) {
        return 1 - cosineDistance(v1, v2);
    }

    double cosineDistance(final double[] u, final double[] v) {
        final double uv = Arrays.stream(multiply(u, v)).average().orElse(0);
        final double uu = Arrays.stream(multiply(u, u)).average().orElse(0);
        final double vv = Arrays.stream(multiply(v, v)).average().orElse(0);
        return 1 - uv / Math.sqrt(uu * vv);
    }

    double[] multiply(final double[] v1, final double[] v2) {
        final double[] product = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            product[i] = v1[i] * v2[i];
        }
        return product;
    }

    public Matrix multiply(final Matrix matrix1, final Matrix matrix2) {
        final BasicMatrix m1 = matrix1.getMatrix();
        final BasicMatrix m2 = matrix2.getMatrix();
        final BasicMatrix product = m1.multiplyElements(m2);
        return new Matrix(product);
    }

    public Matrix add(final Matrix matrix1, final Matrix matrix2) {
        final BasicMatrix m1 = matrix1.getMatrix();
        final BasicMatrix m2 = matrix2.getMatrix();
        final BasicMatrix sum = m1.add(m2);
        return new Matrix(sum);
    }

    public double tTest(final double[] x, final double[] y) {
        return Math.abs(new TTest().homoscedasticTTest(x, y));
    }
}

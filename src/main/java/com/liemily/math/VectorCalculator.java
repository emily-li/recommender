package com.liemily.math;

import java.util.Arrays;

public class VectorCalculator {
    public double cosineSimilarity(final double[] v1, final double[] v2) {
        return 1 - cosineDistance(v1, v2);
    }

    double cosineDistance(final double[] u, final double[] v) {
        final double umu = Arrays.stream(u).average().orElse(0);
        final double vmu = Arrays.stream(v).average().orElse(0);

        final double[] nu = Arrays.stream(u).map(d -> d - umu).toArray();
        final double[] nv = Arrays.stream(v).map(d -> d - vmu).toArray();

        final double uv = Arrays.stream(multiply(nu, nv)).average().orElse(0);
        final double uu = Arrays.stream(multiply(nu, nu)).average().orElse(0);
        final double vv = Arrays.stream(multiply(nv, nv)).average().orElse(0);
        return 1 - uv / Math.sqrt(uu * vv);
    }

    double[] multiply(final double[] v1, final double[] v2) {
        final double[] product = new double[v1.length];
        for (int i = 0; i < v1.length; i++) {
            product[i] = v1[i] * v2[i];
        }
        return product;
    }
}

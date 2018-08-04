package com.liemily.math;

import org.ojalgo.function.aggregator.Aggregator;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;

public class VectorCalculator {
    public double cosineSimilarity(final double[] v1, final double[] v2) {
        return 1 - cosineDistance(v1, v2);
    }

    double cosineDistance(final double[] u, final double[] v) {
        final double uv = multiply(u, v).aggregateAll(Aggregator.AVERAGE).doubleValue();
        final double uu = multiply(u, u).aggregateAll(Aggregator.AVERAGE).doubleValue();
        final double vv = multiply(v, v).aggregateAll(Aggregator.AVERAGE).doubleValue();
        return 1 - uv / Math.sqrt(uu * vv);
    }

    BasicMatrix multiply(final double[] v1, final double[] v2) {
        return PrimitiveMatrix.FACTORY.rows(v1).multiplyElements(PrimitiveMatrix.FACTORY.rows(v2));
    }
/*    public double cosineSimilarity(final double[] v1, final double[] v2) {
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
    }*/
}

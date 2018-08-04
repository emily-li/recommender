package com.liemily.math;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ojalgo.matrix.BasicMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;

public class VectorCalculatorTest {
    private static VectorCalculator calculator;

    @BeforeClass
    public static void setupBeforeClass() {
        calculator = new VectorCalculator();
    }

    @Test
    public void testSameVectorCosineSimilarity() {
        final double sim = calculator.cosineSimilarity(new double[]{0, 0, 1}, new double[]{0, 0, 1});
        Assert.assertEquals(1, sim, 0.0);
    }

    @Test
    public void testDiffVectorCosineDistance() {
        final double sim = calculator.cosineDistance(new double[]{1, 0, 1}, new double[]{0, 0, 1});
        Assert.assertEquals(0.292893, sim, 0.00001);
    }

    @Test
    public void testSameVectorCosineDistance() {
        final double sim = calculator.cosineDistance(new double[]{0, 0, 1}, new double[]{0, 0, 1});
        Assert.assertEquals(0, sim, 0.0);
    }

    @Test
    public void testSameVectorMultiply() {
        final BasicMatrix product = calculator.multiply(new double[]{0, 0, 1}, new double[]{0, 0, 1});
        final BasicMatrix expected = PrimitiveMatrix.FACTORY.rows(new double[]{0, 0, 1});
        assert expected.equals(product);
    }

    @Test
    public void testDiffVectorMultiply() {
        final BasicMatrix product = calculator.multiply(new double[]{1, 0, 1}, new double[]{0, 0, 1});
        final BasicMatrix expected = PrimitiveMatrix.FACTORY.rows(new double[]{0, 0, 1});
        assert expected.equals(product);
    }
}

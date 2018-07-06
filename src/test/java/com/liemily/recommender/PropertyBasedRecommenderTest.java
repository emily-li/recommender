package com.liemily.recommender;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PropertyBasedRecommenderTest {
    private static PropertyBasedRecommender recommender;
    private static DataSet dataSet;

    @BeforeClass
    public static void setupBeforeClass() {
        dataSet = new DataSet(
                new String[]{"item1", "item2", "item3"},
                new double[][]{
                        new double[]{1, 2, 3},
                        new double[]{4, 5, 6},
                        new double[]{7, 8, 9}
                }
        );
        recommender = new PropertyBasedRecommender(dataSet);
    }

    @Test
    public void testGetLikelihood() throws Exception {
        final double actual = recommender.getLikelihood(dataSet.getHeader()[1], dataSet.getHeader()[2]);
        Assert.assertEquals(5, actual, 0.0001);
    }
}

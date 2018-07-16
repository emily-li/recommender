package com.liemily.recommender;

import com.liemily.data.DataSet;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ItemBasedRecommenderTest {
    private static ItemBasedRecommender recommender;
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
        recommender = new ItemBasedRecommender(dataSet);
    }

    @Test
    public void testGetRecommendation() throws Exception {
        final String actual = recommender.getRecommendation(dataSet.getHeader()[2]);
        Assert.assertEquals("item3", actual);
    }
}

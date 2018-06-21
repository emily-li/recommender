package com.liemily.recommender;

import org.junit.BeforeClass;
import org.junit.Test;

public class ItemMatrixTest {
    private static SuccessiveBasedRecommender recommender;
    private static String[] header;

    @BeforeClass
    public static void setupBeforeClass() {
        header = new String[]{"fooItem", "barItem", "someItem", "otherItem"};
        final double[][] data = new double[header.length][header.length];
        final DataSet dataSet = new DataSet(header, data);
        recommender = new SuccessiveBasedRecommender(dataSet);
    }

    @Test
    public void testSuccessivePurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood = recommender.getLikelihood(header[1], header[0]);
        recommender.registerSuccessiveItem(header[1], header[0]);

        assert recommender.getLikelihood(header[1], header[0]) > previousLikelihood;
    }

    @Test
    public void testNotSuccessivePurchaseLikelihoodDecreases() throws Exception {
        recommender.registerSuccessiveItem(header[1], header[0]);
        final double previousLikelihood = recommender.getLikelihood(header[1], header[0]);
        recommender.registerSuccessiveItem(header[1], header[2]);

        assert recommender.getLikelihood(header[1], header[0]) < previousLikelihood;
    }

    @Test
    public void testRecentPurchaseLikelihoodIncreases() throws Exception {
        final double previousLikelihood0 = recommender.getLikelihood(header[2], header[0]);
        final double previousLikelihood1 = recommender.getLikelihood(header[2], header[0]);

        recommender.registerSuccessiveItem(header[2], header[1], header[0]);

        assert recommender.getLikelihood(header[2], header[0]) > previousLikelihood0;
        assert recommender.getLikelihood(header[2], header[1]) > previousLikelihood1;
    }
}

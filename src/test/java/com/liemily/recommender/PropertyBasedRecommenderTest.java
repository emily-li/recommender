package com.liemily.recommender;

import com.liemily.data.Item;
import org.junit.BeforeClass;
import org.junit.Test;

public class PropertyBasedRecommenderTest {
    private static PropertyBasedRecommender recommender;

    @BeforeClass
    public static void setupBeforeClass() {
        recommender = new PropertyBasedRecommender(null);
    }

    @Test
    public void testSimilarity() throws Exception {
        final int[] props1 = new int[]{1, 0};
        final int[] props2 = new int[]{0, 1};

        final Item similarItem1 = new Item("1", props1);
        final Item similarItem2 = new Item("2", props1);
        final Item dissimilarItem = new Item("3", props2);

        final double similarLikelihood = recommender.getLikelihood(similarItem1, similarItem2);
        final double dissimilarLikelihood = recommender.getLikelihood(similarItem1, dissimilarItem);

        assert similarLikelihood > dissimilarLikelihood;
    }
}

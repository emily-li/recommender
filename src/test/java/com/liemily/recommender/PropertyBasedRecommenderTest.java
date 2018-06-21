package com.liemily.recommender;

import com.liemily.data.Item;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.TreeMap;

public class PropertyBasedRecommenderTest {
    private static PropertyBasedRecommender recommender;

    @BeforeClass
    public static void setupBeforeClass() {
        recommender = new PropertyBasedRecommender(null);
    }

    @Test
    public void testSimilarity() {
        final TreeMap<String, String> props1 = new TreeMap<>(Collections.singletonMap("prop", "1"));
        final TreeMap<String, String> props2 = new TreeMap<>(Collections.singletonMap("prop", "2"));

        final Item similarItem1 = new Item("1", props1);
        final Item similarItem2 = new Item("2", props1);
        final Item dissimilarItem = new Item("3", props2);

        final double similarLikelihood = recommender.getLikelihood(similarItem1, similarItem2);
        final double dissimilarLikelihood = recommender.getLikelihood(similarItem1, dissimilarItem);

        assert similarLikelihood > dissimilarLikelihood;
    }
}

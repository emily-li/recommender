package com.liemily.recommender;

import com.liemily.data.Item;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by Emily Li on 02/07/2018.
 */
public class HybridRecommenderTest {
    private HybridRecommender hybridRecommender;

    @Test
    public void test() {
        final ItemBasedRecommender recommender1 = mock(ItemBasedRecommender.class);
        final ItemBasedRecommender recommender2 = mock(ItemBasedRecommender.class);
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{
                recommender1, recommender2
        };

        hybridRecommender = spy(new HybridRecommender(recommenders));
        hybridRecommender.getRecommendation(new Item(""));

    }
}

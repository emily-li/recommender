package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.Matrix;
import org.junit.Test;

public class UntrainedRecommenderProviderTest {
    @Test
    public void testRecommenderGivesRandomValues() throws Exception {
        final Inventory inventory = new Inventory(new Item("a"), new Item("b"), new Item("c"));

        final Matrix m1 = new UntrainedRecommenderProvider(inventory).getRecommender(0.0001, 0.0002).getDataSet().getData();
        final Matrix m2 = new UntrainedRecommenderProvider(inventory).getRecommender(0.0001, 0.0002).getDataSet().getData();

        assert !m1.getMatrix().equals(m2.getMatrix());
    }
}
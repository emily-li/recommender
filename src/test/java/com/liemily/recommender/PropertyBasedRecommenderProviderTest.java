package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;
import com.liemily.math.VectorCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyBasedRecommenderProviderTest {
    private static PropertyBasedRecommenderProvider provider;

    @BeforeClass
    public static void setupBeforeClass() {
        provider = new PropertyBasedRecommenderProvider(new VectorCalculator());
    }

    @Test
    public void testRecommenderGivenInventoryProperties() throws Exception {
        Inventory inventory = new Inventory(
                new Item("item1", new double[]{1, 1, 0}),
                new Item("item2", new double[]{1, 0, 0}),
                new Item("item3", new double[]{0, 0, 1})
        );
        PropertyBasedRecommender recommender = provider.getRecommender(inventory);

        DataSet actual = recommender.getDataSet();
        double[][] expected = new double[][]{
                new double[]{0, 0.333, 0},
                new double[]{0.333, 0, 0},
                new double[]{0, 0, 0}
        };

        for (int i = 0; i < inventory.getIds().length; i++) {
            for (int j = 0; j < inventory.getIds().length; j++) {
                assertEquals(expected[i][j], actual.get(inventory.getIds()[i], inventory.getIds()[j]), 0.0001);
            }
        }
    }
}

package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.math.VectorCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PropertyBasedRecommenderProviderTest {
    private static Inventory inventory;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new Inventory(
                new Item("item1", new double[]{1, 1, 0}),
                new Item("item2", new double[]{1, 0, 0}),
                new Item("item3", new double[]{0, 0, 1})
        );
    }

    @Test
    public void testWeightsAlterPropertySimilarities() throws Exception {
        final double minWeight = 0.1;
        final double maxWeight = 0.2;
        final PropertyBasedRecommenderProvider provider = new PropertyBasedRecommenderProvider(new VectorCalculator(), inventory, minWeight, maxWeight);
        final ItemBasedRecommender recommender = provider.getRecommender();

        final DataSet actualDataSet = recommender.getDataSet();
        final double[][] nonWeightedExpectedDataSet = new double[][]{
                new double[]{0, 0.7071, 0},
                new double[]{0.7071, 0, 0},
                new double[]{0, 0, 0}
        };

        for (int i = 0; i < inventory.getIds().length; i++) {
            for (int j = 0; j < inventory.getIds().length; j++) {
                final double nonWeightedExpected = nonWeightedExpectedDataSet[i][j];
                final double actual = actualDataSet.get(inventory.getIds()[i], inventory.getIds()[j]);

                if (nonWeightedExpected != 0) {
                    final double min = nonWeightedExpected * minWeight;
                    final double max = nonWeightedExpected * maxWeight;

                    assertNotEquals(nonWeightedExpected, actual, 0.00001);
                    assert actual > min && actual < max;
                } else {
                    assertEquals(nonWeightedExpected, actual, 0.00001);
                }
            }
        }

        assertNotEquals(actualDataSet.getData().get(0, 1), actualDataSet.getData().get(1, 0));
    }
}

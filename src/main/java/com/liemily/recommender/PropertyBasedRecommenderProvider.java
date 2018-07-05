/*
package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;
import com.liemily.math.VectorCalculator;

import java.util.Arrays;

public class PropertyBasedRecommenderProvider {
    private VectorCalculator vectorCalculator;

    public PropertyBasedRecommenderProvider(VectorCalculator vectorCalculator) {
        this.vectorCalculator = vectorCalculator;
    }

    public PropertyBasedRecommender getRecommender(Inventory inventory) {
        final Item[] items = inventory.getInventory();

        final double[][] similarities = new double[items.length][items.length];
        for (final Item item : items) {
            final int[] props = item.getPropArray();
            vectorCalculator.
        }

        */
/*Item[] items = inventory.getInventory();
        for (int i = 0; i < items.length; i++) {
            items[i].
        }
        int[] likelihoodVector = new int[item.getProps().size()];

        int idx = 0;
        for (Map.Entry<String, String> prop : item.getProps().entrySet()) {
            String comparatorProp = comparatorItem.getProps().get(prop.getKey());
            likelihoodVector[idx] = prop.getValue().equalsIgnoreCase(comparatorProp) ? 1 : 0;
            idx++;
        }
        return Arrays.stream(likelihoodVector).average().orElse(0);*//*



        final DataSet dataSet = new DataSet(inventory.getIds(), similarities);
        return new PropertyBasedRecommender(dataSet);
    }
}
*/

package com.liemily.recommender;

import com.liemily.data.Item;

import java.util.Arrays;
import java.util.Map;

public class PropertyBasedRecommender extends ItemBasedRecommender {
    public PropertyBasedRecommender(DataSet dataSet) {
        super(dataSet);
    }

    @Override
    public double getLikelihood(Item item, Item comparatorItem) {
        int[] likelihoodVector = new int[item.getProps().size()];

        int idx = 0;
        for (Map.Entry<String, String> prop : item.getProps().entrySet()) {
            String comparatorProp = comparatorItem.getProps().get(prop.getKey());
            likelihoodVector[idx] = prop.getValue().equalsIgnoreCase(comparatorProp) ? 1 : 0;
            idx++;
        }
        return Arrays.stream(likelihoodVector).average().orElse(0);
    }
}

package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;

public class PropertyBasedRecommender extends ItemBasedRecommender {
    public PropertyBasedRecommender(DataSet dataSet) {
        super(dataSet);
    }

    @Override
    public double getLikelihood(Item item, Item comparatorItem) throws NoSuchFieldException {
        return getDataSet().get(item.getId())[getDataSet().getIndex(comparatorItem.getId())];
    }

    private double blah(Inventory inventory) {
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
        return Arrays.stream(likelihoodVector).average().orElse(0);*/
        return 0;
    }
}

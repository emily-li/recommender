package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;

/**
 * Created by Emily Li on 02/07/2018.
 */
public class HybridRecommender {
    private ItemBasedRecommender[] recommenders;
    private Inventory inventory;

    public HybridRecommender(ItemBasedRecommender[] recommenders, Inventory inventory) {
        this.recommenders = recommenders;
        this.inventory = inventory;
    }

    public Item getRecommendation(Item item) throws NoSuchFieldException {
        final double[] probabilities = new double[inventory.getInventory().length];

        for (int i = 0; i < recommenders.length; i++) {
            final double[] recommenderProbabilities = recommenders[i].getDataSet().get(item.getId());

            for (int j = 0; j < probabilities.length; j++) {
                probabilities[j] = i == 0 ? recommenderProbabilities[i] : probabilities[j] * recommenderProbabilities[j];
            }
        }

        double max = -1;
        int maxIdx = -1;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > max) {
                max = probabilities[i];
                maxIdx = i;
            }
        }
        return inventory.get(maxIdx);
    }
}

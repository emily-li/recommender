package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;

import java.util.Arrays;

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
        double[] probabilities = new double[inventory.size()];
        for (int i = 0; i < recommenders.length; i++) {
            final double[] recommenderProbabilities = recommenders[i].getDataSet().get(item.getId());
            probabilities[i] == null ? recommenderProbabilities : probabilities[i] * probabilities;
        }
        probabilities.
    }
}

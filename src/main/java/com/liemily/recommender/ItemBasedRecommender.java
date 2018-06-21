package com.liemily.recommender;

import com.liemily.data.Item;

public interface ItemBasedRecommender {
    double getLikelihood(Item item, Item comparatorItem) throws NoSuchFieldException;
}

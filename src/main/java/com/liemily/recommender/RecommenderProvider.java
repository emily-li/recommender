package com.liemily.recommender;

import com.liemily.exception.RecommenderException;

public interface RecommenderProvider {
    ItemBasedRecommender getRecommender() throws RecommenderException;
}

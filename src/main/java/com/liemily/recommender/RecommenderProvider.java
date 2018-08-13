package com.liemily.recommender;

import com.liemily.exception.RecommenderException;

public interface RecommenderProvider {
    ItemBasedRecommender getRecommender(final double weightMin, final double weightMax) throws RecommenderException;
}

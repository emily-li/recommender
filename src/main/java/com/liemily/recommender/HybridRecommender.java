package com.liemily.recommender;

/**
 * Main recommender used by the project.
 * As the SuccessiveCollaborativeRecommender is the main intent of the recommender, this is required.
 * Other recommenders are auxiliary to supplement the SuccessiveCollaborativeRecommender
 */
public class HybridRecommender extends ItemBasedRecommender {
    private final SuccessiveCollaborativeRecommender successiveCollaborativeRecommender;
    private final ItemBasedRecommender[] auxiliaryRecommenders;

    public HybridRecommender(final DataSet dataSet,
                             final SuccessiveCollaborativeRecommender successiveCollaborativeRecommender,
                             final ItemBasedRecommender[] auxiliaryRecommenders) {
        super(dataSet);
        this.successiveCollaborativeRecommender = successiveCollaborativeRecommender;
        this.auxiliaryRecommenders = auxiliaryRecommenders;
    }

    public ItemBasedRecommender[] getAuxiliaryRecommenders() {
        return auxiliaryRecommenders;
    }

    public SuccessiveCollaborativeRecommender getSuccessiveCollaborativeRecommender() {
        return successiveCollaborativeRecommender;
    }
}

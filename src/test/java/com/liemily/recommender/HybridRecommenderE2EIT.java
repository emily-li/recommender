package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.generator.EntityGenerator;
import com.liemily.math.MatrixCalculator;
import com.liemily.math.VectorCalculator;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Emily Li on 03/08/2018.
 */
@Ignore
public class HybridRecommenderE2EIT {
    private static Inventory inventory;
    private static ItemBasedRecommender untrainedRecommender;
    private static ItemBasedRecommender hybridRecommender;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        inventory = new EntityGenerator(new Random()).generateInventory(10000, 20);

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(new VectorCalculator(), inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());

        final ItemBasedRecommender propertyRecommender = propertyBasedRecommenderProvider.getRecommender();
        final ItemBasedRecommender successiveRecommender = successiveCollaborativeRecommenderProvider.getRecommender();
        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, new ItemBasedRecommender[]{propertyRecommender, successiveRecommender}, new MatrixCalculator());
        hybridRecommender = hybridRecommenderProvider.getRecommender();

        untrainedRecommender = new UntrainedRecommenderProvider(inventory).getRecommender();
    }

    @Test
    public void testHybridRecommenderPrecisionHigherThanRandom() throws Exception {
        // precision = #true_positives / (#true_positives + #false_positives)
        // independent samples t-test over 100 tests

        final String item = inventory.get(0).getId();
        untrainedRecommender.getRecommendation(item);
        hybridRecommender.getRecommendation(item);
    }
}

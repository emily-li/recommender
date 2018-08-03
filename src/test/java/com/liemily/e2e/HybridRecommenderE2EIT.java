package com.liemily.e2e;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.generator.EntityGenerator;
import com.liemily.math.MatrixCalculator;
import com.liemily.math.VectorCalculator;
import com.liemily.recommender.HybridRecommenderProvider;
import com.liemily.recommender.ItemBasedRecommender;
import com.liemily.recommender.PropertyBasedRecommenderProvider;
import com.liemily.recommender.SuccessiveCollaborativeRecommenderProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Emily Li on 03/08/2018.
 */
public class HybridRecommenderE2EIT {
    private static Inventory inventory;
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
    }

    @Test
    public void testHybridRecommenderPrecisionHigherThanRandom() {
        // precision = #true_positives / (#true_positives + #false_positives)
        // independent samples t-test over 100 tests
    }
}

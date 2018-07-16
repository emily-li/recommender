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

public class HybridRecommenderE2ETest {
    private static EntityGenerator entityGenerator;
    private static HybridRecommenderProvider hybridRecommenderProvider;

    @BeforeClass
    public static void setupBeforeClass() throws Exception {
        entityGenerator = new EntityGenerator(new Random());
        final Inventory inventory = entityGenerator.generateInventory(10000, 1000);

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(new VectorCalculator(), inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{propertyBasedRecommenderProvider.getRecommender(), successiveCollaborativeRecommenderProvider.getRecommender()};

        hybridRecommenderProvider = new HybridRecommenderProvider(inventory, recommenders, new MatrixCalculator());
    }

    @Test
    public void test() {

    }
}

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

public class ItemBasedRecommenderPerformanceIT {
    private static Inventory inventory;

    private static PropertyBasedRecommenderProvider propertyBasedRecommenderProvider;
    private static SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider;

    @BeforeClass
    public static void setupBeforeClass() {
        inventory = new EntityGenerator(new Random()).generateInventory(10000, 20);

        propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(new VectorCalculator(), inventory);
        successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());
    }

    @Test
    public void testGetPropertyBasedRecommenderTime() {
        long start = System.currentTimeMillis();
        propertyBasedRecommenderProvider.getRecommender();
        long end = System.currentTimeMillis();
        assert start - end < 60000;
    }

    @Test
    public void testGetSuccessiveCollaborativeRecommenderTime() throws Exception {
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());

        long start = System.currentTimeMillis();
        successiveCollaborativeRecommenderProvider.getRecommender();
        long end = System.currentTimeMillis();
        assert start - end < 60000;
    }

    @Test
    public void testGetHybridRecommenderProviderTime() throws Exception {
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{propertyBasedRecommenderProvider.getRecommender(), successiveCollaborativeRecommenderProvider.getRecommender()};

        long start = System.currentTimeMillis();
        new HybridRecommenderProvider(inventory, recommenders, new MatrixCalculator());
        long end = System.currentTimeMillis();
        assert start - end < 60000;
    }

    @Test
    public void testGetHybridRecommenderTime() throws Exception {
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{propertyBasedRecommenderProvider.getRecommender(), successiveCollaborativeRecommenderProvider.getRecommender()};
        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, recommenders, new MatrixCalculator());

        long start = System.currentTimeMillis();
        hybridRecommenderProvider.getRecommender();
        long end = System.currentTimeMillis();
        assert start - end < 60000;
    }
}

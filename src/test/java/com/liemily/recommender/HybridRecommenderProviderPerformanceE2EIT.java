package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.generator.EntityGenerator;
import com.liemily.math.Calculator;
import com.liemily.math.Matrix;
import org.junit.BeforeClass;
import org.junit.Test;

public class HybridRecommenderProviderPerformanceE2EIT {
    private static int timeout;
    private static Inventory inventory;
    private static Calculator calculator;

    private static PropertyBasedRecommenderProvider propertyBasedRecommenderProvider;
    private static SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider;

    @BeforeClass
    public static void setupBeforeClass() {
        timeout = 60000;
        inventory = new EntityGenerator().generateInventory(10000, 20);
        calculator = new Calculator();

        propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(new Calculator(), inventory);
        successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());
    }

    @Test(timeout = 120000)
    public void testGetPropertyBasedRecommenderTime() {
        long start = System.currentTimeMillis();
        propertyBasedRecommenderProvider.getRecommender(0.0001, 0.0002);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 120000)
    public void testGetSuccessiveCollaborativeRecommenderTime() throws Exception {
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());

        long start = System.currentTimeMillis();
        successiveCollaborativeRecommenderProvider.getRecommender(0.0001, 0.0002);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 240000)
    public void testGetHybridRecommenderTime() throws Exception {
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{propertyBasedRecommenderProvider.getRecommender(0.0001, 0.0002), successiveCollaborativeRecommenderProvider.getRecommender(0.0001, 0.0002)};
        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, recommenders, calculator);

        long start = System.currentTimeMillis();
        hybridRecommenderProvider.getRecommender(0.0001, 0.0002);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 180000)
    public void testMatrixMultiplication() throws Exception {
        final Matrix m = successiveCollaborativeRecommenderProvider.getRecommender(0.0001, 0.0002).getDataSet().getData();

        long start = System.currentTimeMillis();
        calculator.multiply(m, m);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }
}

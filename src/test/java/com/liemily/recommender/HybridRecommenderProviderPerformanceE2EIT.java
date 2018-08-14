package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.generator.EntityGenerator;
import com.liemily.math.Matrix;
import com.liemily.math.MatrixCalculator;
import com.liemily.math.VectorCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

public class HybridRecommenderProviderPerformanceE2EIT {
    private static int timeout;
    private static Inventory inventory;
    private static MatrixCalculator matrixCalculator;

    private static PropertyBasedRecommenderProvider propertyBasedRecommenderProvider;
    private static SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider;

    @BeforeClass
    public static void setupBeforeClass() {
        timeout = 60000;
        inventory = new EntityGenerator().generateInventory(10000, 20);
        matrixCalculator = new MatrixCalculator();

        propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(new VectorCalculator(), inventory);
        successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());
    }

    @Test(timeout = 180000)
    public void testGetPropertyBasedRecommenderTime() {
        long start = System.currentTimeMillis();
        propertyBasedRecommenderProvider.getRecommender(0.0001, 0.0002);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 180000)
    public void testGetSuccessiveCollaborativeRecommenderTime() throws Exception {
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());

        long start = System.currentTimeMillis();
        successiveCollaborativeRecommenderProvider.getRecommender(0.0001, 0.0002);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 180000)
    public void testGetHybridRecommenderTime() throws Exception {
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{propertyBasedRecommenderProvider.getRecommender(0.0001, 0.0002), successiveCollaborativeRecommenderProvider.getRecommender(0.0001, 0.0002)};
        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, recommenders, matrixCalculator);

        long start = System.currentTimeMillis();
        hybridRecommenderProvider.getRecommender(0.0001, 0.0002);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 180000)
    public void testMatrixMultiplication() {
        final Matrix m = propertyBasedRecommenderProvider.getRecommender(0.0001, 0.0002).getDataSet().getData();

        long start = System.currentTimeMillis();
        matrixCalculator.multiply(m, m);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }
}

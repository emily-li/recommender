package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.generator.EntityGenerator;
import com.liemily.math.Matrix;
import com.liemily.math.MatrixCalculator;
import com.liemily.math.VectorCalculator;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

public class HybridRecommenderProviderPerformanceE2EIT {
    private static int timeout;
    private static Inventory inventory;
    private static MatrixCalculator matrixCalculator;

    private static PropertyBasedRecommenderProvider propertyBasedRecommenderProvider;
    private static SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider;

    @BeforeClass
    public static void setupBeforeClass() {
        timeout = 60000;
        inventory = new EntityGenerator(new Random()).generateInventory(10000, 100);
        matrixCalculator = new MatrixCalculator();

        propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(new VectorCalculator(), inventory);
        successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());
    }

    @Test(timeout = 180000)
    public void testGetPropertyBasedRecommenderTime() {
        long start = System.currentTimeMillis();
        propertyBasedRecommenderProvider.getRecommender();
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 180000)
    public void testGetSuccessiveCollaborativeRecommenderTime() throws Exception {
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, new UserHistory());

        long start = System.currentTimeMillis();
        successiveCollaborativeRecommenderProvider.getRecommender();
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 180000)
    public void testGetHybridRecommenderTime() throws Exception {
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{propertyBasedRecommenderProvider.getRecommender(), successiveCollaborativeRecommenderProvider.getRecommender()};
        final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, recommenders, matrixCalculator);

        long start = System.currentTimeMillis();
        hybridRecommenderProvider.getRecommender();
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }

    @Test(timeout = 180000)
    public void testMatrixMultiplication() {
        final Matrix m = propertyBasedRecommenderProvider.getRecommender().getDataSet().getData();

        long start = System.currentTimeMillis();
        matrixCalculator.multiply(m, m);
        long end = System.currentTimeMillis();
        assert start - end < timeout;
    }
}

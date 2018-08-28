package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import com.liemily.generator.EntityGenerator;
import com.liemily.math.Calculator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class HybridRecommenderProviderIT {
    @Test
    public void testHybridRecommenderPrecisionTestData() throws Exception {
        final Calculator calculator = new Calculator();
        final EntityGenerator entityGenerator = new EntityGenerator();
        final Inventory inventory = entityGenerator.generateInventory(100, 20);
        final UserHistory[] userHistories = entityGenerator.generateUserHistories(1000, 5, inventory);

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(calculator, inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistories);

        final ItemBasedRecommender hybridRecommender = new HybridRecommenderProvider(inventory,
                calculator,
                successiveCollaborativeRecommenderProvider.getRecommender(1.0001, 1.0002),
                propertyBasedRecommenderProvider.getRecommender(1.0001, 1.0002))
                .getRecommender(1.00001, 1.0002);

        final Collection<UserHistory> userHistoriesWithMultOrders = new ArrayList<>();
        for (final UserHistory userHistory : userHistories) {
            if (userHistory.getOrderHistory().length > 1) {
                userHistoriesWithMultOrders.add(userHistory);
            }
        }

        int truePositives = 0;
        for (final UserHistory userHistory : userHistoriesWithMultOrders) {
            final String[][] orders = userHistory.getOrderHistory();
            final int randOrderIdx = ThreadLocalRandom.current().nextInt(1, orders.length);
            final String[] randOrder = orders[randOrderIdx];

            final String recommendation = hybridRecommender.getRecommendation(randOrder[ThreadLocalRandom.current().nextInt(0, randOrder.length)]);
            for (int i = 0; i < randOrderIdx; i++) {
                if (Arrays.asList(orders[i]).contains(recommendation)) {
                    truePositives++;
                }
            }
        }
        assert truePositives > 0;
    }
}

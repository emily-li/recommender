package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.UserHistory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecommenderTrainerTest {
    private static RecommenderTrainer trainer;

    @BeforeClass
    public static void setUpBeforeClass() {
        trainer = new RecommenderTrainer(new Inventory()) {
            @Override
            public ItemBasedRecommender getTrainedRecommender(Map<String, Collection<String>> validRecommendations, UserHistory[] userHistories, double significance, int retries) {
                return null;
            }
        };
    }

    @Test
    public void testGetValidRecommendations() {
        final UserHistory userHistory = new UserHistory(new String[]{"item0"}, new String[]{"item1"}, new String[]{"item2"});
        final Map<String, Collection<String>> recommendations = trainer.getValidRecommendations(userHistory);
        assertEquals(new HashSet<>(Arrays.asList("item2", "item1")), recommendations.get("item0"));
        assertEquals(new HashSet<>(Collections.singletonList("item2")), recommendations.get("item1"));
        assertNull(recommendations.get("item2"));
    }
}
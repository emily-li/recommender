package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;


/**
 * Created by Emily Li on 02/07/2018.
 */


public class HybridRecommenderTest {
    private HybridRecommender hybridRecommender;
    private ItemBasedRecommender[] recommenders;

    @Before
    public void setup() throws Exception {
        recommenders = new ItemBasedRecommender[]{
                mock(PropertyBasedRecommender.class),
                mock(SuccessiveCollaborativeRecommender.class)
        };

        DataSet dataSet = mock(DataSet.class);
        when(dataSet.get(Mockito.anyString())).thenReturn(new double[]{0.0});

        for (ItemBasedRecommender recommender : recommenders) {
            when(recommender.getDataSet()).thenReturn(dataSet);
        }
        hybridRecommender = new HybridRecommender(recommenders, new Inventory(new Item("")));
    }

    @Test
    public void testHybridRecommenderUsesSubRecommenders() throws Exception {
        hybridRecommender.getRecommendation(new Item("item1"));

        for (ItemBasedRecommender recommender : recommenders) {
            verify(recommender, times(1)).getLikelihood(null, null);
        }
    }
}

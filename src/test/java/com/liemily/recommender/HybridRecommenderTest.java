/*

package com.liemily.recommender;

import com.liemily.data.Inventory;
import com.liemily.data.Item;
import org.junit.Test;

import java.util.Arrays;

*/
/**
 * Created by Emily Li on 02/07/2018.
 *//*


public class HybridRecommenderTest {
    private HybridRecommender hybridRecommender;

    @Test
    public void test() throws Exception {
        final String[] header = new String[]{"item1", "item2"};
        final ItemBasedRecommender recommender1 = new PropertyBasedRecommender();
        final ItemBasedRecommender recommender2 = new SuccessiveCollaborativeRecommender(
                new DataSet(header, new double[][]{
                    new double[]{0.1, 0.5}, // probability of item1 -> item1 is 0.1, item1 -> item 2 is 0.5
                    new double[]{0.2, 0.1}
        }));
        final ItemBasedRecommender[] recommenders = new ItemBasedRecommender[]{
                recommender1, recommender2
        };

        hybridRecommender = new HybridRecommender(recommenders, new Inventory(Arrays.stream(header).map(Item::new).toArray(Item[]::new)));
        hybridRecommender.getRecommendation(new Item(""));
    }
}
*/

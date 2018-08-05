package com.liemily.recommender;

import org.junit.Test;

/**
 * Created by Emily Li on 05/08/2018.
 */
public class DataSetTest {
    @Test(expected = NoSuchFieldException.class)
    public void testNoSuchFieldExceptionThrownForNonInventoryItem() throws Exception {
        final DataSet dataSet = new DataSet(new String[]{"a", "b", "c"}, null);
        dataSet.getIndex("d");
    }
}

package com.liemily.recommender;

import org.junit.Test;

public class DataSetTest {
    @Test(expected = NoSuchFieldException.class)
    public void testNoSuchFieldExceptionThrownForNonInventoryItem() throws Exception {
        final DataSet dataSet = new DataSet(new String[]{"a", "b", "c"}, null);
        dataSet.getIndex("d");
    }
}

package com.liemily.recommender;

import com.liemily.data.DataReader;
import com.liemily.data.Inventory;
import com.liemily.data.InventoryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InventoryServiceTest {
    private InventoryService inventoryService;

    @Before
    public void setup() {
        inventoryService = new InventoryService(new DataReader());
    }

    @Test
    public void testPopulateInventory() throws Exception {
        Inventory inventory = inventoryService.getInventory("inventoryTest.txt");
        Assert.assertArrayEquals(new String[]{"a", "b", "c", "e", "d", "foo"}, inventory.getInventory());
    }
}

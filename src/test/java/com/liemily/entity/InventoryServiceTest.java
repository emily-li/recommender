package com.liemily.entity;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class InventoryServiceTest {
    private static InventoryService inventoryService;

    @BeforeClass
    public static void setupBeforeClass() {
        inventoryService = new InventoryService();
    }

    @Test
    public void testPopulateInventory() throws Exception {
        Inventory inventory = inventoryService.getInventory("inventoryTest.txt");
        Assert.assertArrayEquals(new String[]{"a", "b", "c", "e", "d", "foo"}, inventory.getIds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicatesThrown() throws Exception {
        inventoryService.getInventory("duplicatesInventoryTest.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumPropertiesThrown() throws Exception {
        inventoryService.getInventory("invalidNumFields.txt");
    }
}

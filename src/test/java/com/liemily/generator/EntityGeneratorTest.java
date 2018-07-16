package com.liemily.generator;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import com.liemily.entity.UserHistory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.function.DoublePredicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class EntityGeneratorTest {
    private static EntityGenerator entityGenerator;

    private static int numItems;
    private static int numUsers;

    private static Inventory inventory;
    private static UserHistory[] userHistories;

    @BeforeClass
    public static void setupBeforeClass() {
        entityGenerator = new EntityGenerator(new Random());
        numItems = 100;
        numUsers = 100;
        inventory = entityGenerator.generateInventory(numItems, 1000);
        userHistories = entityGenerator.generateUserHistories(numUsers, 100, inventory);
    }

    @Test
    public void testGeneratesItems() {
        final Set<String> items = Arrays.stream(inventory.getIds()).collect(Collectors.toSet());
        assertEquals(items.size(), numItems);
    }

    @Test
    public void testMultiplePropertiesPopulatedSometimes() {
        assertSomeInventoryProperties(inventory, d -> d > 0);
    }

    @Test
    public void testMultiplePropertiesEmptySometimes() {
        assertSomeInventoryProperties(inventory, d -> d == 0);
    }

    @Test
    public void testSomePropertiesSharedSometimes() {

        boolean shared = false;
        Set<Integer> propIds = new HashSet<>();
        for (Item item : inventory.getInventory()) {
            for (int i = 0; i < item.getPropArray().length; i++) {
                if (item.getPropArray()[i] != 0) {
                    shared = !propIds.add(i);

                    if (shared) {
                        break;
                    }
                }
            }
        }
        assertTrue(shared);
    }

    @Test
    public void testGeneratesUserHistories() {
        Arrays.stream(userHistories).forEach(uh -> assertNotNull(uh.getPurchaseHistory()));
        assertEquals(numUsers, userHistories.length);
    }

    @Test
    public void testGeneratedUserHistoriesHaveDifferentSizes() {
        final Set<Integer> sizes = new HashSet<>();
        sizes.add(userHistories[0].getPurchaseHistory().size());

        boolean diffSizes = false;
        for (int i = 1; i < userHistories.length; i++) {
            if (sizes.add(userHistories[i].getPurchaseHistory().size())) {
                diffSizes = true;
                break;
            }
        }
        assertTrue(diffSizes);
    }

    @Test
    public void testGeneratedUserHistoriesHaveInventoryPurchases() {
        Collection<String> inventoryItems = Arrays.stream(inventory.getIds()).collect(Collectors.toList());
        for (final UserHistory userHistory : userHistories) {
            List<String> purchaseHistory = userHistory.getPurchaseHistory();
            for (String item : purchaseHistory) {
                assertTrue(inventoryItems.contains(item));
            }
        }
    }

    private void assertSomeInventoryProperties(Inventory inventory, DoublePredicate predicate) {
        boolean assertedProps = false;
        for (Item item : inventory.getInventory()) {
            if (Arrays.stream(item.getPropArray()).filter(predicate).count() > 1) {
                assertedProps = true;
                break;
            }
        }
        assertTrue(assertedProps);
    }
}

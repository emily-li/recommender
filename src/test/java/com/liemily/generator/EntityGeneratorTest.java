package com.liemily.generator;

import com.liemily.entity.Inventory;
import com.liemily.entity.Item;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.DoublePredicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EntityGeneratorTest {
    private static EntityGenerator entityGenerator;
    private static int numItems;
    private static int numProps;

    @BeforeClass
    public static void setupBeforeClass() {
        entityGenerator = new EntityGenerator();
        numItems = 10000;
        numProps = 100;
    }

    @Test
    public void testGeneratesItems() {
        Inventory inventory = entityGenerator.generate(numItems, numProps);
        Set<String> items = Arrays.stream(inventory.getIds()).collect(Collectors.toSet());
        assertEquals(items.size(), numItems);
    }

    @Test
    public void testMultiplePropertiesPopulatedSometimes() {
        Inventory inventory = entityGenerator.generate(numItems, numProps);
        assertSomeProperties(inventory, d -> d > 0);
    }

    @Test
    public void testMultiplePropertiesEmptySometimes() {
        Inventory inventory = entityGenerator.generate(numItems, numProps);
        assertSomeProperties(inventory, d -> d == 0);
    }

    @Test
    public void testSomePropertiesSharedSometimes() {
        Inventory inventory = entityGenerator.generate(numItems, numProps);

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

    private void assertSomeProperties(Inventory inventory, DoublePredicate predicate) {
        boolean assertedProps = false;
        for (Item item : inventory.getInventory()) {
            assertedProps = Arrays.stream(item.getPropArray()).filter(predicate).count() > 1;
            if (assertedProps) {
                break;
            }
        }
        assertTrue(assertedProps);
    }
}

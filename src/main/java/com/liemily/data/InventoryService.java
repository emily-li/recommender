package com.liemily.data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InventoryService {
    private DataReader dataReader;

    public InventoryService(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public Inventory getInventory(final String file) throws IOException, URISyntaxException, IllegalArgumentException {
        String[] inventory = dataReader.readFile(file);
        Set<String> uniq = new HashSet<>();
        for (String item : inventory) {
            boolean added = uniq.add(item);
            if (!added) {
                throw new IllegalArgumentException("Duplicate item: " + item);
            }
        }

        Item[] items = Arrays.stream(inventory).map(Item::new).toArray(Item[]::new);
        return new Inventory(items);
    }

}

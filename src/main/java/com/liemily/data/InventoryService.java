package com.liemily.data;

import java.io.IOException;
import java.net.URISyntaxException;

public class InventoryService {
    private DataReader dataReader;

    public InventoryService(DataReader dataReader) {
        this.dataReader = dataReader;
    }

    public Inventory getInventory(final String file) throws IOException, URISyntaxException {
        String[] inventory = dataReader.readFile(file);
        return new Inventory(inventory);
    }

}

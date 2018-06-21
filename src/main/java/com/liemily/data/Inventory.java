package com.liemily.data;

import java.util.Arrays;

public class Inventory {
    private final Item[] inventory;
    private final String[] ids;

    public Inventory(final Item... inventory) {
        this.inventory = inventory;
        this.ids = Arrays.stream(inventory).map(Item::getId).toArray(String[]::new);
    }

    public String[] getIds() {
        return ids;
    }

    public Item get(int i) {
        return inventory[i];
    }

    public int size() {
        return inventory.length;
    }
}

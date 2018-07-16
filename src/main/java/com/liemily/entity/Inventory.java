package com.liemily.entity;

import java.util.Arrays;

public class Inventory {
    private final Item[] inventory;
    private final String[] ids;

    public Inventory(final Item... inventory) {
        this.inventory = inventory;
        this.ids = Arrays.stream(inventory).map(Item::getId).toArray(String[]::new);
    }

    public Item[] getInventory() {
        return inventory;
    }

    public String[] getIds() {
        return ids;
    }

    public Item get(int i) {
        return inventory[i];
    }
}

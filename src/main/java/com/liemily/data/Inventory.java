package com.liemily.data;

public class Inventory {
    private final String[] inventory;

    public Inventory(final String... inventory) {
        this.inventory = inventory;
    }

    public String[] getInventory() {
        return inventory;
    }
}

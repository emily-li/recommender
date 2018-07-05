package com.liemily.data;

public class Item {
    private final String id;
    private final int[] propArray;

    public Item(final String name) {
        this(name, new int[0]);
    }

    public Item(final String name, final int[] propArray) {
        this.id = name;
        this.propArray = propArray;
    }

    public String getId() {
        return id;
    }

    public int[] getPropArray() {
        return propArray;
    }
}

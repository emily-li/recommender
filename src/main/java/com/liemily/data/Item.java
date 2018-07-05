package com.liemily.data;

public class Item {
    private final String id;
    private final boolean[] propArray;

    public Item(final String name, final boolean[] propArray) {
        this.id = name;
        this.propArray = propArray;
    }

    public String getId() {
        return id;
    }

    public boolean[] getPropArray() {
        return propArray;
    }
}

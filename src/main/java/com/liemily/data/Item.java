package com.liemily.data;

public class Item {
    private final String id;
    private final double[] propArray;

    public Item(final String name) {
        this(name, new double[0]);
    }

    public Item(final String name, final double[] propArray) {
        this.id = name;
        this.propArray = propArray;
    }

    public String getId() {
        return id;
    }

    public double[] getPropArray() {
        return propArray;
    }
}

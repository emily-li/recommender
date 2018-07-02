package com.liemily.data;

import java.util.Map;
import java.util.TreeMap;

public class Item {
    private final String id;
    private final TreeMap<String, String> props;

    public Item(String name) {
        this(name, new TreeMap<>());
    }

    public Item(String name, TreeMap<String, String> props) {
        this.id = name;
        this.props = props;
    }

    public String getId() {
        return id;
    }

    public TreeMap<String, String> getProps() {
        return props;
    }
}

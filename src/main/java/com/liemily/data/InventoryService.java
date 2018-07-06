package com.liemily.data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class InventoryService {
    public Inventory getInventory(final String file) throws IOException, URISyntaxException {
        return new Inventory(readFile(file));
    }

    private Item[] readFile(final String file) throws IOException, URISyntaxException {
        final Path path = Paths.get(ClassLoader.getSystemResource(file).toURI());

        final Set<String> uniqIds = new HashSet<>();
        try (final Stream<String> dataStream = Files.lines(path)) {
            final String[] lines = dataStream.toArray(String[]::new);
            final String[] header = lines[0].split(",");

            final Item[] items = new Item[lines.length - 1];

            for (int i = 1; i < lines.length; i++) {
                items[i - 1] = getItemFromLine(lines[i], header, uniqIds);
            }
            return items;
        }
    }

    private Item getItemFromLine(String line, String[] header, Set<String> uniqIds) {
        final String[] fields = line.split(",", -1);
        final String id = fields[0];

        validateUniqueItem(uniqIds, id);
        validateItemLine(header, fields);

        final double[] props = new double[fields.length - 1];
        for (int i = 0; i < props.length; i++) {
            props[i] = fields[i].isEmpty() ? 0 : 1;
        }
        return new Item(id, props);
    }

    private void validateUniqueItem(Set<String> items, String newItem) {
        final boolean added = items.add(newItem);
        if (!added) {
            throw new IllegalArgumentException("Duplicate item: " + newItem);
        }
    }

    private void validateItemLine(String[] header, String[] line) {
        if (header.length != line.length) {
            throw new IllegalArgumentException(String.format("Invalid number of fields on line '%s'. Number of fields should match header: '%s'", Arrays.toString(line), Arrays.toString(header)));
        }
    }
}

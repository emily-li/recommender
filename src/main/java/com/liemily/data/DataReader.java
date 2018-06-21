package com.liemily.data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataReader {
    public String[] readFile(final String file) throws URISyntaxException, IOException {
        final Path path = Paths.get(ClassLoader.getSystemResource(file).toURI());
        try (final Stream<String> dataStream = Files.lines(path)) {
            return dataStream.toArray(String[]::new);
        }
    }
}

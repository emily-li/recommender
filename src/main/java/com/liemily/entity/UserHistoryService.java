package com.liemily.entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Emily Li on 05/08/2018.
 */
public class UserHistoryService {
    public UserHistory[] getUserHistories(final String file) throws IOException {
        return readFile(file);
    }

    private UserHistory[] readFile(final String file) throws IOException {
        final Path path = Paths.get(file);
        try (final Stream<String> dataStream = Files.lines(path)) {
            final String[] lines = dataStream.toArray(String[]::new);

            final Map<String, Map<String, Order>> userHistoryDetails = new HashMap<>();
            for (int i = 1; i < lines.length; i++) {
                final String[] purchaseDetails = lines[i].split(",");
                final String userId = purchaseDetails[0];
                final String orderId = purchaseDetails[1];
                final String item = purchaseDetails[2];

                userHistoryDetails.computeIfAbsent(userId, details -> new TreeMap<>());
                userHistoryDetails.get(userId).computeIfAbsent(orderId, order -> new Order(new ArrayList<>()));
                userHistoryDetails.get(userId).get(orderId).items.add(item);
            }

            return userHistoryDetails.entrySet().parallelStream()
                    .map(userOrders -> userOrders.getValue().values().parallelStream()
                            .map(order -> order.items.parallelStream().toArray(String[]::new)).toArray(String[][]::new))
                    .map(UserHistory::new).toArray(UserHistory[]::new);
        }
    }

    public class Order {
        private Collection<String> items;

        public Order(Collection<String> items) {
            this.items = items;
        }
    }
}

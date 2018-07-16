package com.liemily.entity;

import java.util.Arrays;
import java.util.List;

public class UserHistory {
    private final List<String> purchaseHistory;

    /**
     * Purchase history, where the first item is the first purchase
     *
     * @param purchaseHistory List of item names that should match to an inventory
     */
    public UserHistory(final String... purchaseHistory) {
        this.purchaseHistory = Arrays.asList(purchaseHistory);
    }

    public List<String> getPurchaseHistory() {
        return purchaseHistory;
    }
}

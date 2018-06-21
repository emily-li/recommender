package com.liemily.data;

import java.util.Arrays;
import java.util.List;

public class UserHistory {
    private final List<String> purchaseHistory;

    public UserHistory(final String... purchaseHistory) {
        this.purchaseHistory = Arrays.asList(purchaseHistory);
    }

    public List<String> getPurchaseHistory() {
        return purchaseHistory;
    }
}

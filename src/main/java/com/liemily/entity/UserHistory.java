package com.liemily.entity;

import java.util.Arrays;

public class UserHistory {
    private final String[][] purchaseHistory;

    /**
     * Purchase history, where the first item is the first purchase
     *
     * @param purchaseHistory List of item names that should match to an inventory
     */
    public UserHistory(final String[]... purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public String[][] getOrderHistory() {
        return purchaseHistory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHistory that = (UserHistory) o;
        return Arrays.deepEquals(purchaseHistory, that.purchaseHistory);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(purchaseHistory);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(purchaseHistory);
    }
}

package com.liemily.entity;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Emily Li on 05/08/2018.
 */
public class UserHistoryServiceTest {
    private static UserHistoryService userHistoryService;

    @BeforeClass
    public static void setupBeforeClass() {
        userHistoryService = new UserHistoryService();
    }

    @Test
    public void testCanReadUserHistoryFromFile() throws Exception {
        final UserHistory[] expectedUserHistories = new UserHistory[]{
                new UserHistory(new String[]{"item1", "item2"}),
                new UserHistory(new String[]{"item3", "item4"}, new String[]{"item1"}),
                new UserHistory(new String[]{"item2"})
        };
        final UserHistory[] userHistories = userHistoryService.getUserHistories("src/test/resources/userHistoryTest.txt");
        Assert.assertArrayEquals(expectedUserHistories, userHistories);
    }
}
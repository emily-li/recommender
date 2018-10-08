package com.liemily.recommender;

import com.liemily.entity.Inventory;
import com.liemily.entity.InventoryService;
import com.liemily.entity.UserHistory;
import com.liemily.entity.UserHistoryService;
import com.liemily.math.Calculator;

import java.util.Collection;
import java.util.Map;
import java.util.Scanner;

/**
 * The HybridRecommender is the main recommender required for the app.
 *
 * The main method instantiates all classes required for the HybridRecommender, including
 * inventory, user histories, auxiliary recommenders to the successive recommender, the successive recommender,
 * and finally the HybridRecommender.
 *
 * It then starts the HybridRecommender in a loop to constantly provide recommendations
 * as the recommender stores new purchases in its history.
 */
public class RecommenderApp {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new IllegalArgumentException("Invalid number of arguments. Arguments are '<inventoryFileLocation> <userHistoriesFileLocation>'");
        }
        final String inventoryFile = args[0];
        final String userHistoriesFile = args[1];

        final Calculator calculator = new Calculator();
        final InventoryService inventoryService = new InventoryService();
        final Inventory inventory = inventoryService.getInventory(inventoryFile);
        final UserHistoryService userHistoryService = new UserHistoryService();
        final UserHistory[] userHistories = userHistoryService.getUserHistories(userHistoriesFile);

        final PropertyBasedRecommenderProvider propertyBasedRecommenderProvider = new PropertyBasedRecommenderProvider(calculator, inventory);
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, calculator, userHistories);
        final HybridRecommenderTrainer hybridRecommenderTrainer = new HybridRecommenderTrainer(calculator, inventory, propertyBasedRecommenderProvider, successiveCollaborativeRecommenderProvider);

        final Map<String, Collection<String>> validRecommendations = hybridRecommenderTrainer.getValidRecommendations(userHistories);
        HybridRecommender hybridRecommender = hybridRecommenderTrainer.getTrainedRecommender(validRecommendations, userHistories, 0.05, 10);

        try (final Scanner scannedInput = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.println("Awaiting input of newly registered inventory item");

                    final String input = scannedInput.nextLine();
                    final String[] inputArgs = input.split(" ");

                    final String item = String.join(" ", inputArgs);
                    if (inputArgs.length == 0 || item.trim().isEmpty()) {
                        throw new IllegalArgumentException("No item provided. Please provide an inventory item as an argument.");
                    }

                    hybridRecommender.getSuccessiveCollaborativeRecommender().registerSuccessiveItem(item);
                    final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, calculator, hybridRecommender.getSuccessiveCollaborativeRecommender(), hybridRecommender.getAuxiliaryRecommenders());
                    hybridRecommender = hybridRecommenderProvider.getRecommender(0, 0);
                    final String recommendation = hybridRecommender.getRecommendation(item);

                    System.out.println("Next recommended item is: " + recommendation);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(100);
                    System.out.println("-----------------------");
                }
            }
        }
    }
}

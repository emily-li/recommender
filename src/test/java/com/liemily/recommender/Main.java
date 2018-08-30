package com.liemily.recommender;

public class Main {
    public static void main(String[] args) {
        /*if (args.length != 2) {
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
        final SuccessiveCollaborativeRecommenderProvider successiveCollaborativeRecommenderProvider = new SuccessiveCollaborativeRecommenderProvider(inventory, userHistories);
        final HybridRecommenderTrainer hybridRecommenderTrainer = new HybridRecommenderTrainer(calculator, inventory, propertyBasedRecommenderProvider, successiveCollaborativeRecommenderProvider);

        final Map<String, Collection<String>> validRecommendations = hybridRecommenderTrainer.getValidRecommendations(userHistories);
        HybridRecommender hybridRecommender = hybridRecommenderTrainer.getTrainedRecommender(validRecommendations, 0.05, 10);

        try (final Scanner scannedInput = new Scanner(System.in)) {
            while (true) {
                try {
                    System.out.println("Awaiting input of newly registered inventory item");

                    final String input = scannedInput.nextLine();
                    final String[] inputArgs = input.split(" ");

                    if (inputArgs.length != 1) {
                        throw new IllegalArgumentException("Invalid number of arguments. Arguments are '<newly registered inventory item>");
                    }
                    final String item = inputArgs[0];

                    hybridRecommender.getSuccessiveCollaborativeRecommender().registerSuccessiveItem(item);
                    final HybridRecommenderProvider hybridRecommenderProvider = new HybridRecommenderProvider(inventory, calculator, hybridRecommender.getSuccessiveCollaborativeRecommender(), hybridRecommender.getAuxiliaryRecommenders());
                    hybridRecommender = hybridRecommenderProvider.getRecommender(0, 0);
                    final String recommendation = hybridRecommender.getRecommendation(item);

                    System.out.println("Next recommended item is: " + recommendation);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }*/
    }
}

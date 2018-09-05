# recommender
Recommender system project for hybrid context aware suggestions for computer part purchases

## Data files
See test resources for examples
### Inventory data
```
itemId,property1,property2,property3
item1,value1,value2,
item2,,,
item3,,,value3
```
* The first line should be the header
* The first column should be the item ID (String)
* Each 'property' field can be named, e.g. 'colour' instead of 'property1'.
* If a property is null for an item, there should be an empty field for the property column
    
### User history data
```
user_id,order_id,item
userId1,orderId1,itemId1
userId2,orderId2,itemId2
userId2,orderId2,itemId3
userId3,orderId3,itemId4
```
* The first line should be the header
* An order can have multiple items
* The item values should match the IDs in the providedinventory file 
    
## Usage
1 . Package
```
mvn clean package
```
2 . Run
```
java -jar <recommenderJar> <inventoryFileLocation> <userHistoriesFileLocation>
``` 
e.g.
```
java -jar target/recommender-1.0.0.jar src/test/resources/hybridE2EInventory.txt src/test/resources/hybridE2EUserHistories.txt
```
3. Input
The input should be a valid item from the inventory file provided.

## Tests
1 . Unit tests
```
mvn test
```
2 . Integration and E2E tests
```
mvn integration-test
```
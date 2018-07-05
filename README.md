# recommender
Recommender system project for hybrid context aware suggestions for computer part purchases

## Data files
Data files should be of the format
```
itemId,property1,property2,property3
item1,value1,value2,
item2,,,
item3,,,value3
```
* The first line should be the header
* The first column should be the item ID (String)
* All item properties must be described in the header
    * If a property is null for an item, there should be an empty field for the property column
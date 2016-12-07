package worldofzuul; // HUSK

import java.util.ArrayList; //Imports the utility for the Arraylist
import java.util.UUID;

/**
 * Handles inventory and handles items. Contains the limits on how big an
 * inventory is.
 *
 * @author emil
 */
public class Inventory { // Initializing the class Inventory

    /**
     * Next is the declaration of the ArrayList witch will be the structure for
     * the inventory. (Adding/removing items etc)
     */
    private final ArrayList<UUID> inventoryList;

    /**
     * Following are the two parameters for the inventory that can be set when
     * constructed. But if the class is constructed without them being set, it
     * will fall back to these predefined values:
     *
     * maxAllowedItems with default 3 maxAllowedWeight with default 12
     *
     */
    private int maxAllowedItems;
    private int maxAllowedWeight;

    /**
     * The inventory will have some values about how many items there are in it,
     * the cummulated weight, a unique ID number for each item, (maybe more to
     * come). These attributes will be declared here:
     */
    private int sumItems;
    private int sumWeight;
    private UUID uuid;

    /**
     * Inventory constructs a inventory with the possibility to set maximum
     * weight the player is allowed to carry and the maximum amount of items
     * allowed to carry. If no values are given, the defaults will be used.
     *
     * @param maxItems set max allowed amount of items in integers default is 3.
     * @param maxWeight set max allowed weight in integers, default is 12.
     *
     */
    public Inventory(int maxItems, int maxWeight) {

        this.maxAllowedItems = maxItems;
        this.maxAllowedWeight = maxWeight;
        uuid = UUID.randomUUID();
        this.inventoryList = new ArrayList<>();
    }

    /**
     * Constructor without setting limits on the inventory, which means it uses
     * the default.
     */
    public Inventory() {
        this.maxAllowedItems = 3;
        this.maxAllowedWeight = 12;
        uuid = UUID.randomUUID();
        this.inventoryList = new ArrayList<>();
    }

    /**
     * getMaxItems returns the maximum amount of items allowed to carry.
     *
     * @return maximum amount of items allowed in integers.
     */
    public int getMaxItems() {
        return maxAllowedItems;
    }

    /**
     * setMaxItems sets the max amount of items allowed to carry.
     *
     * @param x int for setting maxAllowedItems
     */
    public void setMaxItems(int x) {
        this.maxAllowedItems = x;
    }

    /**
     * getMaxWeight returns the maximum cumulated weight allowed to carry.
     *
     * @return maximum cumulated weight allowed in integers.
     */
    public int getMaxWeight() {
        return maxAllowedWeight;
    }

    /**
     * setMaxWeight set the max cumulated weight allowed to carry.
     *
     * @param x int for setting maxAllowedWeight
     */
    public void setMaxWeight(int x) {
        this.maxAllowedWeight = x;
    }

    /**
     * addItem adds a item, of type Item, to the inventory.
     *
     * TODO: There needs a destination too.. The uniqID could be implemented
     * better in regards of how to remove items again.
     *
     * @param uuid
     * @param weight Set the weight of the item (int).
     * @return returns the UUID number for the added item.
     */
    public boolean addItem(UUID uuid, int weight) {
        this.sumWeight += weight;
        this.sumItems++;

        if (this.sumItems < this.maxAllowedItems && this.sumWeight <= this.maxAllowedWeight) {
            this.inventoryList.add(uuid);
            return true;
        }

        this.sumWeight -= weight;
        this.sumItems--;
        return false;
    }

    /**
     * remItem removes a given item from the inventory, based upon the unique ID
     * returned from the addItem method.
     *
     * TODO: better implementation in regards to uniqID and idendifying items.
     *
     * @param uuid UUID of item to be deleted.
     * @param weight
     *
     */
    public void remItem(UUID uuid, int weight) {
        if (this.inventoryList.contains(uuid)) {
            this.inventoryList.remove(uuid);
            this.sumWeight -= weight;
            this.sumItems--; //Decreases by one, to keep keeping track of the amount of items.
        }

    }

    public UUID[] getInventoryUuids() {
        UUID[] returnArray = new UUID[this.inventoryList.size()];
        int count = 0;
        for (UUID uuid : this.inventoryList) {
            returnArray[count] = uuid;
            count++;
        }
        return returnArray;
    }

    public boolean hasSpaceFor(int weight) {
        if (maxAllowedItems > sumItems && maxAllowedWeight > (sumWeight + weight)) {
            return true;
        }
        return false;
    }
}

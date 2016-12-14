package worldofzuul;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Handles inventory and handles items. Contains the limits on how big an
 * inventory is.
 */
public class Inventory {

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
     * maxAllowedItems with default 3, maxAllowedWeight with default 12
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
        this.inventoryList = new ArrayList<>();
    }

    /**
     * Constructor without setting limits on the inventory, which means it uses
     * the default.
     */
    public Inventory() {
        this.maxAllowedItems = 3;
        this.maxAllowedWeight = 100;
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
     * addItem adds an item's UUID to the inventory.
     *
     * @param uuid the UUID of the item
     * @param weight the weight of the item
     * @return returns the UUID number for the added item.
     */
    public boolean addItem(UUID uuid, int weight) {
        this.sumWeight += weight;
        this.sumItems++;

        if (this.sumItems < this.maxAllowedItems && this.sumWeight <= this.maxAllowedWeight) {
            this.inventoryList.add(uuid);
            return true;
        }

        //If it makes it this far, it means that there were not enough space for the item
        this.sumWeight -= weight;
        this.sumItems--;
        return false;
    }

    /**
     * remItem removes a given item from the inventory, based on the item's UUID
     *
     * @param uuid UUID of item to be deleted.
     * @param weight the weight of the item has to be removed again
     */
    public void remItem(UUID uuid, int weight) {
        if (this.inventoryList.contains(uuid)) {
            this.inventoryList.remove(uuid);
            this.sumWeight -= weight;
            this.sumItems--; //Decreases by one, to keep keeping track of the amount of items.
        }

    }

    /**
     * Returns the UUIDs held by this inventory object
     *
     * @return an array of the UUIDs
     */
    public UUID[] getInventoryUuids() {
        UUID[] returnArray = new UUID[this.inventoryList.size()];
        int count = 0;
        for (UUID uuid : this.inventoryList) {
            returnArray[count] = uuid;
            count++;
        }
        return returnArray;
    }

    /**
     * Checks whether there is space for the item
     *
     * @param weight the weight of the item that is being tested
     * @return whether or not there is space for it
     */
    public boolean hasSpaceFor(int weight) {
        if (maxAllowedItems > sumItems && maxAllowedWeight > (sumWeight + weight)) {
            return true;
        }
        return false;
    }
}

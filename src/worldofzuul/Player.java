package worldofzuul;

import java.util.UUID;

/**
 * Holds all of the information regarding the player. Contains methods for
 * accessing the players inventory.
 */
public class Player {

    //Defines variables
    private UUID currentPlanetId; //The planet the player is currently at
    private Inventory inventory; //The players inventory
    private int maxFuel; //How much fuel the player can hold
    private int fuel; //How much fuel the player currently has
    private int reputation; //How much reputation the player has
    private boolean ITD;
    private int warpFuel;
    private boolean canWarp;
    private String name;

    /**
     * Constructor.
     *
     * @param name the players name that will be associated with the highscore
     * @param maxFuel the maximum amount of fuel
     * @param startingReputation the starting reputation amount
     */
    public Player(String name, int maxFuel, int startingReputation) {
        this.name = name;
        this.maxFuel = maxFuel;
        this.fuel = maxFuel;
        this.warpFuel = 50;
        this.reputation = startingReputation;
        this.canWarp = false;

        this.inventory = new Inventory();
    }

    // ***** SETTERS *****
    public void setCurrentPlanet(UUID planetId) {
        this.currentPlanetId = planetId;
        this.setFuel(this.maxFuel);
    }

    public void setFuel(int fuel) {
        if (fuel < this.maxFuel) {
            this.fuel = fuel;
        } else {
            this.fuel = this.maxFuel;
        }
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public void setITD(boolean itd) {
        this.ITD = itd;
    }

    public void setCanWarp(boolean canWarp) {
        this.canWarp = canWarp;
    }

    /**
     * Sets the value of warpfuel. (Use addWarpfuel instead for adding fuel!)
     *
     * @param fuel int for new value og warpFuel
     */
    public void setWarpfuel(int fuel) {
        this.warpFuel = fuel;
    }
    // ***** SETTERS END *****

    // ***** GETTERS *****
    public String getName() {
        return this.name;
    }

    public UUID getPlanetId() {
        return this.currentPlanetId;
    }

    public int getMaxFuel() {
        return this.maxFuel;
    }

    public int getFuel() {
        return this.fuel;
    }

    public int getReputation() {
        return this.reputation;
    }

    public boolean getITD() {
        return ITD;
    }

    public boolean canWarp() {
        return this.canWarp;
    }

    public int getWarpfuel() {
        return warpFuel;
    }
    // ***** GETTERS END *****

    // ***** HANDLING INVENTORY *****
    public UUID[] getInventoryUuids() {
        return this.inventory.getInventoryUuids();
    }

    /**
     * Adds an item to the player's inventory
     *
     * @param uuid of the item being added
     * @param weight of the item being added
     * @return whether or not there was space for it
     */
    public boolean addItem(UUID uuid, int weight) {
        return this.inventory.addItem(uuid, weight);
    }

    /**
     * Removes an item based on the UUID of that item
     *
     * @param itemId the UUID if the item
     * @param weight the weight of the item being removed
     */
    public void removeItem(UUID itemId, int weight) {
        this.inventory.remItem(itemId, weight);
    }

    public boolean hasInventorySpaceFor(int weight) {
        return this.inventory.hasSpaceFor(weight);
    }
    // ***** HANDLING INVENTORY END *****

    /**
     * Adds new warpfuel to the existing value of warpFuel
     *
     * @param newFuel int for warpfuel to be added
     */
    public void addWarpfuel(int newFuel) {
        warpFuel += newFuel;
    }
}

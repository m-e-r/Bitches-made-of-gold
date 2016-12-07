/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul;

import java.util.UUID;

/**
 * Holds all of the information regarding NPCs. It has methods for handling its
 * inventory, both deleting and adding items
 *
 * @author emildaniel
 */
public class NPC implements Comparable<NPC>, PrintAble, PicturizeAble {

    //The number used by the user to reference the NPC during runtime
    public static int referenceCounter = 0;

    private String name;
    private String description;
    private String imagePath;
    private UUID id;
    private int referenceNumber; //The number used by the user to reference the NPC during runtime
    private int rid; //Identifies which Item the NPC has to receive by the start of the game
    private int pid; //Identifies where the NPC should be placed at the start of the game
    private int iid;
    private UUID packageId; //Which Item UUID the NPC has to receive
    private UUID planetId; //Which Planet/Moon UUID the NPC is placed at
    private int chanceToMove;
    private int conversationId;
    private int nextConversationId;
    Inventory inventory;

    /**
     * Constructor
     *
     * @param name of the NPC
     * @param description of the NPC
     * @param rid used to identify which items this NPC has to receive by the
     * start of the game
     * @param pid used to tell where the NPC should be placed (on a moon or
     * planet) at the start of the game
     * @param conversationId the first conversation id (which conversation file)
     * the NPC should use at the start of the game
     * @param chanceToMove whether or not the NPC can move. 0 means completely
     * no movement, 10 means certain of moving, in between means x/10 chance to
     * move
     */
    public NPC(String name, String description, int rid, int pid, int iid, int conversationId, int chanceToMove) {
        this.name = name;
        this.description = description;
        this.rid = rid;
        this.pid = pid;
        this.iid = iid;
        this.chanceToMove = chanceToMove;
        this.conversationId = conversationId;
        this.nextConversationId = -1;
        this.id = UUID.randomUUID();
        this.inventory = new Inventory();

        this.referenceNumber = NPC.referenceCounter;
        NPC.referenceCounter++;
    }

    public NPC() {
        this.nextConversationId = -1;
        this.id = UUID.randomUUID();
        this.inventory = new Inventory();

        this.referenceNumber = NPC.referenceCounter;
        NPC.referenceCounter++;
    }

    // ***** GETTERS *****
    public UUID getId() {
        return this.id;
    }

    public int getReferenceNumber() {
        return this.referenceNumber;
    }

    public UUID getPlanetId() {
        return this.planetId;
    }

    public int getChanceToMove() {
        return this.chanceToMove;
    }

    public int getRid() {
        return this.rid;
    }

    public int getPid() {
        return this.pid;
    }

    public int getIid() {
        return this.iid;
    }

    public UUID getPackageId() {
        return this.packageId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
    
    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    public int getConversationId() {
        return this.conversationId;
    }

    public int getNextConversationId() {
        return this.nextConversationId;
    }
    // ***** GETTERS END *****

    // ***** SETTERS *****
    public void setReceiverRid(int rid) {
        this.rid = rid;
    }

    public void setPlanetId(UUID planetId) {
        this.planetId = planetId;
    }

    public void setConversationId(int id) {
        this.conversationId = id;
    }

    public void setNextConversationId(int id) {
        this.nextConversationId = id;
    }

    public void setPackageId(UUID uuid) {
        this.packageId = uuid;
    }
    // ***** SETTERS END *****

    public boolean hasNextConversationId() {
        return this.nextConversationId != -1;
    }

    // ***** GETTERS REGARDING INVENTORY *****
    public UUID[] getInventoryUuids() {
        return this.inventory.getInventoryUuids();
    }
    // ***** GETTERS REGARDING INVENTORY END *****

    /**
     * Creates an item using the method in inventory
     *
     * @param uuid
     * @param weight
     * @return the UUID of the newly created item
     */
    public boolean addItem(UUID uuid, int weight) {
        return this.inventory.addItem(uuid, weight);
    }

    /**
     * Removes an item based on the UUID of that item
     *
     * @param itemId the UUID if the item
     * @param weight
     */
    public void removeItem(UUID itemId, int weight) {
        this.inventory.remItem(itemId, weight);
    }

    @Override
    public int compareTo(NPC t) {
        return (this.referenceNumber - t.referenceNumber);
    }

}

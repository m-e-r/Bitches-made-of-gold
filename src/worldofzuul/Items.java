package worldofzuul; // HUSK

import java.util.UUID;

/**
 * The class Items is based on that there is several different items that the
 * player can get. These Items must be delivered from one NPC to another through
 * a receiver id (RID). Each item has a unique id and description & the item has
 * a weight. There is a limit of how much weight and how many items the player
 * can have. The paperwork on each item can have an effect of the difficulty to
 * deliver the pakage.
 */
public class Items implements Comparable<Items>, PrintAble, PicturizeAble {

    //The number which the user references this item during runtime
    public static int referenceCounter = 0;

    //Initializing variables
    private UUID id; //Every item have an ID
    private int weight; // The weight of the item
    private int reputationWorth;
    private String description; // The description of the item
    private String imagePath;
    private int rid;  // The RID for the destination
    private int iid;
    private UUID npcId;
    private int referenceNumber;
    private int deliverytime; // Each item have a delivery time
    private boolean papers; // Does the user have papers on the item

    /**
     * The constructor
     *
     * @param weight of the item, used to limit how many items can be carried
     * @param reputationWorth decides how much this item is worth in reputation
     * @param desciption of the item
     * @param rid identifies which NPC has to recieve the item at the start of
     * the game
     * @param pid identifies where the item should "spawn" at the start of the
     * game
     */
    public Items(int weight, int reputationWorth, String desciption, int rid, int iid) {
        this.id = UUID.randomUUID();
        this.weight = weight;
        this.reputationWorth = reputationWorth;
        this.description = desciption;
        this.rid = rid;
        this.iid = iid;
        this.referenceNumber = Items.referenceCounter;
        Items.referenceCounter++;
        this.deliverytime = 0;
        this.papers = false;

    }

    /**
     * Constructor, this is needed to create the json files?
     */
    public Items() {
        this.id = UUID.randomUUID();
        this.referenceNumber = Items.referenceCounter;
        Items.referenceCounter++;
        this.deliverytime = 0;
    }

    // ***** GETTERS *****
    @Override
    public String getName() {
        return this.description;
    }
    
    @Override
    public String getDescription() {        //Method for getting the description of the item.
        return this.description;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    public int getRid() {        //Method for returning  destination RID
        return this.rid;
    }

    public int getIid() {        //Method for returning  destination RID
        return this.iid;
    }

    public UUID getNpcId() {
        return this.npcId;
    }

    public int getWeight() {         //Method for getting the weight of the item.
        return this.weight;
    }

    public int getReputationWorth() {
        return this.reputationWorth;
    }

    public UUID getId() {        // //Method for getting the ID of the item.
        return this.id;
    }

    public int getReferenceNumber() {       //Method for getting the Referencenumber
        return this.referenceNumber;
    }

    public int getDeliveryTime() {          // Method for getting the deliverty time

        return this.deliverytime;
    }

    public boolean getPapers() {            // Method for getting the papers
        return this.papers;
    }
    // ***** GETTERS END *****

    // ***** SETTERS *****
    public void setNpcId(UUID npcId) {          // Method for setting the npc id
        this.npcId = npcId;
    }

    public void setDeliveryTime(int deliverytime) {         // Method for setting the delivery time.
        this.deliverytime = deliverytime;
    }

    public boolean setPapersFalse() {           //method for setting the papers to false
        return this.papers = false;
    }

    public boolean setPapersTrue() {            //method for setting the papers to true
        return this.papers = true;
    }
    // ***** SETTERS END *****

    @Override
    public int compareTo(Items t) {
        return (this.referenceNumber - t.referenceNumber);
    }
}

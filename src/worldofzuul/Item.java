package worldofzuul;

import java.util.UUID;

/**
 * The class Item is based on that there is several different items that the
 * player can get. These Item must be delivered from one NPC to another through
 * a receiver id (RID). Each item has a unique id and description & the item has
 * a weight. There is a limit of how much weight and how many items the player
 * can have. The paperwork on each item can have an effect of the difficulty to
 * deliver the pakage.
 */
public class Item implements PrintAble, PicturizeAble {

    //Initializing variables
    private UUID id; //Every item have an ID
    private int weight; // The weight of the item
    private int reputationWorth;
    private String description; // The description of the item
    private String imagePath;
    private int rid;  // The RID for the destination
    private int iid;
    private UUID npcId; // The UUID for the npc it has to be delivered to
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
     * @param iid identifies where the item should "spawn" at the start of the
     * game
     */
    public Item(int weight, int reputationWorth, String desciption, int rid, int iid) {
        this.id = UUID.randomUUID();
        this.weight = weight;
        this.reputationWorth = reputationWorth;
        this.description = desciption;
        this.rid = rid;
        this.iid = iid;
        this.deliverytime = 0;
        this.papers = false;
    }

    /**
     * Constructor, this is needed to create the json files?
     */
    public Item() {
        this.id = UUID.randomUUID();
        this.deliverytime = 0;
    }

    // ***** GETTERS *****
    @Override
    public String getName() {
        return this.description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    public int getRid() {
        return this.rid;
    }

    public int getIid() {
        return this.iid;
    }

    public UUID getNpcId() {
        return this.npcId;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getReputationWorth() {
        return this.reputationWorth;
    }

    public UUID getId() {
        return this.id;
    }

    public int getDeliveryTime() {
        return this.deliverytime;
    }

    public boolean getPapers() {
        return this.papers;
    }
    // ***** GETTERS END *****

    // ***** SETTERS *****
    public void setNpcId(UUID npcId) {
        this.npcId = npcId;
    }

    public void setDeliveryTime(int deliverytime) {
        this.deliverytime = deliverytime;
    }

    public boolean setPapersFalse() {
        return this.papers = false;
    }

    public boolean setPapersTrue() {
        return this.papers = true;
    }
    // ***** SETTERS END *****
}

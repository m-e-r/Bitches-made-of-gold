package worldofzuul;

import java.util.UUID;

/**
 * Holds all of the information regarding planets, except from NPC handling,
 * which is handled by the super class NPCHolder
 */
public class Planet extends NPCHolder {

    //Initializing variables
    private UUID moonUuid;
    private int x;          //x-coordinate of the planet
    private int y;          //y-coordinate of the planet

    /**
     * Constructor
     *
     * @param name of the planet
     * @param description of the planet
     * @param x the x coordinate of the planet
     * @param y the y coordinate of the planet
     * @param pid the "planet id" of the planet, which tells NPC where they
     * should be placed by the start of the game
     */
    public Planet(String name, String description, int x, int y, int pid) {
        super(name, description, pid);
        this.x = x;
        this.y = y;
    }

    /**
     * Empty constructor for JSON reading.
     */
    public Planet() {

    }

    // ***** GETTERS *****
    public boolean hasMoon() {
        return this.moonUuid != null;
    }

    public UUID getMoonUuid() {
        return this.moonUuid;
    }

    public int getx() {
        return this.x;
    }

    public int gety() {
        return this.y;
    }
    // ***** GETTERS END *****

    // ***** SETTERS *****
    public void setMoonUuid(UUID moonId) {
        this.moonUuid = moonId;
    }
    // ***** SETTERS END *****
}

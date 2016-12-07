package worldofzuul;

import java.util.ArrayList;
import java.util.UUID;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Holds all of the information regarding planets, except from NPC handling,
 * which is handled by the super class NPCHolder
 */
public class Planet extends NPCHolder implements Comparable<Planet> {

    //Initializing variables
    private UUID moonUuid;
    private int x;          //x-coordinate of the planet
    private int y;          //y-coordinate of the planet
    private int referenceNum;   //unique referencenumber for the planet

    //static counter for creating new referencenumbers, it starts at one, because the moon always has the number 0
    public static int referenceNumCounter = 1;

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
        this.referenceNum = Planet.referenceNumCounter;
        Planet.referenceNumCounter++;
    }

    public Planet() {
        this.referenceNum = Planet.referenceNumCounter;
        Planet.referenceNumCounter++;
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

    public int getReferenceNum() {
        return this.referenceNum;
    }
    // ***** GETTERS END *****

    // ***** SETTERS *****
    public void setMoonUuid(UUID moonId) {
        this.moonUuid = moonId;
    }
    // ***** SETTERS END *****

    @Override
    public int compareTo(Planet t) {
        return (this.referenceNum - t.referenceNum);
    }
}

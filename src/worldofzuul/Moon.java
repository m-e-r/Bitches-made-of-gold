package worldofzuul;

import java.util.UUID;

/**
 * Holds all of the information regarding a moon, they don't have a position,
 * because their UUID will get assigned to a planet
 */
public class Moon extends NPCHolder {

    private UUID parentPlanetUuid; //Which planet holds this moon

    /**
     * Constructor
     *
     * @param name of the moon
     * @param description of the moon
     * @param pid identifies which NPC and which planet holds this moon at the
     * start of the game
     */
    public Moon(String name, String description, int pid) {
        super(name, description, pid);
    }

    /**
     * Empty constructor for JSON reading
     */
    public Moon() {

    }

    // ***** GETTERS *****
    public UUID getParentPlanetUuid() {
        return this.parentPlanetUuid;
    }
    // ****** GETTERS END *****

    // ****** SETTERS *****
    public void setParentPlanetUuid(UUID parentPlanetUuid) {
        this.parentPlanetUuid = parentPlanetUuid;
    }
    // ****** SETTERS END *****
}

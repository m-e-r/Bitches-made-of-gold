/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul;

import java.util.ArrayList;
import java.util.UUID;

/**
 * A super class for Moons and Planets, as they have holding an NPC in common.
 * This class holds the information about which NPC is at what planet. Along
 * with other common attributes, as id, name, pid and description.
 *
 * @author DanielToft
 */
public abstract class NPCHolder implements PrintAble, PicturizeAble {

    private UUID id;
    private int pid;
    private ArrayList<UUID> npcIds;
    private String description;
    private String name;
    private String imagePath;
    private int warTimer;

    /**
     * Constructor
     *
     * @param name the name of the planet / moon
     * @param description the description of the planet / moon
     * @param pid the pid, which tells the game which NPCs should be placed
     * where at the beginning of the game.
     */
    public NPCHolder(String name, String description, int pid) {
        this.name = name;
        this.description = description;
        this.npcIds = new ArrayList<>();
        this.id = UUID.randomUUID();
        this.pid = pid;
        this.warTimer = -1;
    }

    public NPCHolder() {
        this.npcIds = new ArrayList<>();
        this.id = UUID.randomUUID();
        this.warTimer = -1;
    }

    // ****** GETTERS ******
    public UUID getId() {
        return this.id;
    }

    public int getPid() {
        return this.pid;
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
    /**
     * Gets all of the NPC ids that is currently at the planet / moon.
     *
     * @return an array of UUIDs
     */
    public UUID[] getNpcIds() {
        UUID[] returnArray = new UUID[this.npcIds.size()];
        int i = 0;
        for (UUID uuid : this.npcIds) {
            returnArray[i] = uuid;
            i++;
        }
        return returnArray;
    }

    public boolean hasNpcId(UUID id) {
        return this.npcIds.contains(id);
    }

    public int getWarTimer() {
        return this.warTimer;
    }
    // ***** GETTERS END *****

    // ***** SETTERS *****
    public void addNpcId(UUID npcId) {
        this.npcIds.add(npcId);
    }

    public void removeNpcId(UUID npcId) {
        this.npcIds.remove(npcId);
    }

    public void setWarTimer(int warTimer) {
        this.warTimer = warTimer;
    }
    // ***** SETTERS END *****
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuulgui;

import java.util.UUID;

/**
 *
 * @author MER
 */
public class CheatList {
    private String name;
    private UUID npc;
    
    public CheatList(UUID npc, String name) {
        this.npc = npc;
        this.name = name;
    }
    
    public UUID getNpc() {
        return this.npc;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author DanielToft
 */
public interface iGame {
    ArrayList<UUID> getListOfPlanets(); //
    String getName(UUID uuid); //
    String getDescription(UUID uuid); //
    int getPid(UUID uuid); //
    String getImgPath(UUID uuid); //
    ArrayList<UUID> getInventory(); //
    void startConversation(UUID uuid); //
    ArrayList<UUID> getAvailableNpcs(UUID uuid); //

    ArrayList<UUID> getPossiblePlanets(); //
    void travelToPlanet(UUID planet); //
    int getFuel(); //
    int getWarpFuel(); //
    boolean canWarp(); //
    int getReputation(); //
    int getInGameTime(); //

    String getDashboardUpdate(); //

    void processAnswer(String userAns); //
    String[] getAnswers(); //
}

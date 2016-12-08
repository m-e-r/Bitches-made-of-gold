/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuulgui;

import java.util.ArrayList;
import java.util.UUID;
import javafx.scene.image.Image;

/**
 *
 * @author emil
 */
public interface interfaceGUI {

	ArrayList<UUID> getListOfPlanets(); //
	String getName(UUID uuid); //
	String getDescription(UUID uuid); //
	String getPid(UUID uuid); //
        String getImgPath(UUID uuid); //
        ArrayList<UUID> getAvailableNpcs(UUID uuid); //
        ArrayList<UUID> getPossiblePlanets(); //
        ArrayList<UUID> getInventory(); //
        void startConversation(UUID uuid); //

    void travelToPlanet(UUID uuid);
    int getFuel(); //
    int getWarpFuel(); //
    boolean canWarp(); //
    int getReputation(); //
    int getInGameTime(); //

    String getDashboardUpdate(); //
    void processAnswer(String userAns); //
    String[] getAnswers();
    void setScenario(int scenarioNr); 
    ArrayList<UUID> getPlacementFromStar(); //
}

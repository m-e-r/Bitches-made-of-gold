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
    UUID getPlayerPosition(); //

    ArrayList<UUID> getPossiblePlanets(); //
    void travelToPlanet(UUID planet); //
    int getFuel(); //
    int getWarpFuel(); //
    boolean canWarp(); //
    int getReputation(); //
    int getInGameTime(); //
    UUID getMoonId(UUID uuid);
    String getDashboardUpdate(); //
    
    void dropItem(UUID uuid);
    void getHelp();
    
    void processWarp(UUID nextPosition);
    void processAnswer(String userAns); //
    String[] getAnswers(); //
    //void setScenario(int scenarioNr); 
    
    int[] getPositionCoordinates(UUID uuid); //
    
    public abstract ArrayList<UUID> getPossibleScenarios();
    public abstract void setScenario(UUID uuid);
    
    //NOTE: Nedenstående eksempel skal fjernes fra game.getPlayedMillis();
    //For at skrive tiden ud siden man har startet, skriv da disse linjer:
    //Calendar playedTime = new GregorianCalendar();
    //playedTime.setTimeInMillis(this.game.getPlayedMillis());
    //System.out.println("Hour: " + playedTime.get(Calendar.HOUR) + " minutes: " + playedTime.get(Calendar.MINUTE) + " seconds: " + playedTime.get(Calendar.SECOND));
    public abstract long getPlayedMillis();   
}

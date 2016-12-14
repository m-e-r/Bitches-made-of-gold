package worldofzuul;

import java.util.ArrayList;
import java.util.UUID;

/**
 * An interface that holds all of the methods that GUI will use to fetch
 * information from the Game class.
 */
public interface iGame {

    public abstract ArrayList<UUID> getListOfPlanets();

    public abstract String getName(UUID uuid);

    public abstract String getDescription(UUID uuid);

    public abstract int getPid(UUID uuid);

    public abstract String getImgPath(UUID uuid);

    public abstract String getImgPath(UUID uuid, boolean bool);

    public abstract ArrayList<UUID> getInventory();

    public abstract void startConversation(UUID uuid);

    public abstract ArrayList<UUID> getAvailableNpcs(UUID uuid);

    public abstract UUID getPlayerPosition();

    public abstract ArrayList<UUID> getPossiblePlanets();

    public abstract void travelToPlanet(UUID planet);

    public abstract int getFuel();

    public abstract int getWarpFuel();

    public abstract boolean canWarp();

    public abstract int getReputation();

    public abstract int getInGameTime();

    public abstract UUID getMoonId(UUID uuid);

    public abstract String getDashboardUpdate();

    public abstract void dropItem(UUID uuid);

    public abstract void processWarp(UUID nextPosition);

    public abstract void processAnswer(String userAns);

    public abstract String[] getAnswers();

    public abstract int[] getPositionCoordinates(UUID uuid);

    public abstract ArrayList<UUID> getPossibleScenarios();

    public abstract void setScenario(UUID uuid);

    public abstract long getPlayedMillis();

    public abstract void startGame(UUID scenario, String playerName);

    public abstract String getDeliveryPlanet(UUID uuid);

    public abstract String getDeliveryNpc(UUID uuid);

    public abstract boolean isWar(UUID uuid);

    public abstract ArrayList<String> quitGame();
    
    public abstract boolean isDead();
    
    public abstract int getItemDeliveryTime(UUID itemUuid);
    
    public abstract boolean getItemPapers(UUID itemUuid);
}

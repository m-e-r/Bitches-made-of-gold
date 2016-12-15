package worldofzuul;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * This class controls the flow of the game. It contains getters according to
 * the iGame interface, that can be used by the GUI.
 *
 * Written and commented by Emil Bøgh Harder, Kasper H. Christensen, Malte
 * Engelsted Rasmussen, Matias Marek, Daniel Anton Jørgensen & Daniel Skjold
 * Toft. Gruppe 17, E16, Software/IT 1. semester
 */
public class Game implements iGame {

    //Defines instance variables
    private Scenario scenario;
    private HashMap<UUID, Scenario> possibleScenarios;
    private Calendar startTime;
    private boolean isDead;
    private AudioPlayer audioPlayer;

    private HighScore currentPlayerScore;
    private ArrayList<HighScore> highScores;
    private Dashboard dashboard;
    private Player player;
    private HashMap<UUID, Planet> planets;
    private HashMap<UUID, Moon> moons;

    private HashMap<String, Integer> timerCounts;

    /**
     * Three maps of NPCs, the first one, npcs, holds all of the npcs, which is
     * used for starting a conversation, figuring out which npc is adressed,
     * etc. The difference of civilians and rebels are simply their movement
     * pattern. Rebels only move to and from moons, where civilians only move to
     * and from planets. The creation of a list allows methods of handling both
     * civilians and rebels, when there is no difference, f.ex. creation of a
     * new conversation. The only thing needed by the NPC is the conversation
     * id, which is common for both NPC types. The only reason for the
     * separation into two maps, is purely due to the movement pattern, as that
     * is the only thing that separates them.
     */
    private HashMap<UUID, NPC> npcs;
    private HashMap<UUID, NPC> civilians;
    private HashMap<UUID, NPC> rebels;
    private UUID startNpc;

    private HashMap<UUID, Item> items;
    private MovementCalculator movementCalculator;
    private FileHandler fileHandler;
    private Conversation currentConversation;
    private int time;
    private UUID startingPlanet;

    /**
     * Constructor for the class Game, using the method createRooms() it creates
     * the rooms, sets current room and creates a new parser object
     */
    public Game() {
        this.possibleScenarios = new HashMap<>();
        this.startTime = new GregorianCalendar();
        this.planets = new HashMap<>();
        this.moons = new HashMap<>();
        this.npcs = new HashMap<>();
        this.civilians = new HashMap<>();
        this.rebels = new HashMap<>();
        this.items = new HashMap<>();
        this.movementCalculator = new MovementCalculator();
        this.fileHandler = new FileHandler();
        this.timerCounts = new HashMap<>();
        this.timerCounts.put("warTimer", 300);
        this.timerCounts.put("tryNpcMovement", 100);
        this.timerCounts.put("extraDeliveryTime", 0);
        this.highScores = new ArrayList<>();
        this.dashboard = new Dashboard(); // Creates a new object of the type Dashboard. 
        this.audioPlayer = new AudioPlayer();

        this.createScenarios();
    }

    /**
     * This is the function to call if you want to launch the game! It prints
     * the welcome message, and then it loops, taking your commands, until the
     * game ends.
     */
    private void play() {
        this.startingPlanet = this.createPlanets();
        this.createNpcs();
        this.createItems();
        this.time = 0;
        this.startTime.setTimeInMillis(System.currentTimeMillis());

        this.createHighscores();

        this.player.setCurrentPlanet(this.startingPlanet);
        
        this.audioPlayer.playMusic();
    }

    /**
     * Calculates the planets that a certain position can travel to, based on
     * the amount of fuel. Uses Game's list of all planets, and
     * movementcalculator
     *
     * @param startX starting position
     * @param startY starting position
     * @param currentFuel the amount of fuel that can be expended
     * @return a list of planets that are possible to travel to
     */
    private ArrayList<UUID> getPossiblePlanets(int startX, int startY, int currentFuel) {
        ArrayList<UUID> reachablePlanets = new ArrayList<>();
        for (Planet planet : this.planets.values()) {
            if (this.movementCalculator.isReachable(startX, startY, planet.getx(), planet.gety(), currentFuel)) {
                reachablePlanets.add(planet.getId());
            }
        }
        if(this.planets.containsKey(this.player.getPlanetId())) {
            if(this.planets.get(this.player.getPlanetId()).hasMoon()) {
                reachablePlanets.add(this.planets.get(this.player.getPlanetId()).getMoonUuid());
            }
        }
        return reachablePlanets;
    }

    /**
     * Changes the position (planet) of the character refered in the parameter
     *
     * @param characterToTravel which character to move
     * @param planetId which planet to move to
     */
    private boolean travelToPlanet(Player characterToTravel, UUID nextPositionUuid) {
        int[] currentPosition = getPositionCoordinates(this.player.getPlanetId());
        int[] nextPosition = getPositionCoordinates(nextPositionUuid);
        NPCHolder nextNpcHolder = getNPCHolderFromUuid(nextPositionUuid);

        if (nextNpcHolder.getWarTimer() > this.time) {
            boolean hasAllPapers = false; //If the player does not have any items, it equals death!
            for (UUID uuid : this.player.getInventoryUuids()) {
                hasAllPapers = true; //If the player does not have any items, it will enter the loop, and therefore the default value should be true
                if (!this.items.get(uuid).getPapers()) {
                    hasAllPapers = false;
                    break;
                }
            }

            if (!hasAllPapers) {
                //Perhaps we should just issue a warning at first, that you need all the papers to enter this planet, because it has war
                // or you need to wait until the war ends.
                this.isDead = true;
                return true;
            }
        }

        if (this.movementCalculator.isReachable(currentPosition[0], currentPosition[1], nextPosition[0], nextPosition[1], characterToTravel.getFuel())) {
            characterToTravel.setCurrentPlanet(nextPositionUuid);
            this.audioPlayer.playFly();

            this.player.setFuel(this.player.getMaxFuel());

            int travelTime = 10;
            incrementTime(this.movementCalculator.calculateDistance(currentPosition[0], currentPosition[1], nextPosition[0], nextPosition[1]) / travelTime);
            incrementTime(20); //Adds some extra time used, because of when starting and breaking it costs extra time.
            return true;
        }
        return false;
    }

    /**
     * A method used for processing the "warp" command during runtime. Looks
     * very much like the travelToPlanet method. However this uses the Warp fuel
     * as a limiting factor. As Warp fuel is different from regular fuel it also
     * uses different movement calculations. There is a possibility of not
     * traveling, if you do not have enough warp fuel. Note: using warp skips
     * the checking of whether there is war (meaning you don't need papers for
     * all of your items when entering a planet with war).
     *
     * @param characterToTravel which character that should be moved
     * @param nextPositionUuid which planet or moon that is the intended target.
     */
    private void processWarp(Player characterToTravel, UUID nextPositionUuid) {

        if (characterToTravel.getWarpfuel() > 9) {
            characterToTravel.setCurrentPlanet(nextPositionUuid);
            this.audioPlayer.playWarp();
            characterToTravel.setWarpfuel(characterToTravel.getWarpfuel() - 10);
            this.incrementTime(1);
        }
    }

    /**
     * A method for starting a conversation with the NPC on the planet, that the
     * player is currently at
     *
     * @param npcId
     */
    @Override
    public void startConversation(UUID npcId) {
        //IF the NPC has a nextConversationId (if it is not null) use that!
        NPC npc = this.npcs.get(npcId);
        if (npc.hasNextConversationId()) {
            npc.setConversationId(npc.getNextConversationId());
            npc.setNextConversationId(-1);
        }

        if (!this.fileHandler.doesFileExist("data/" + this.scenario.getPath() + "/conversations/" + npc.getConversationId() + ".txt")) {
            System.out.println("The file you tried to start a conversation with a file that does not exist!");
            return;
        }

        this.currentConversation = new Conversation(npc.getConversationId());
        this.currentConversation.setNpcId(npcId);
        this.currentConversation.createWholeConversation(this.fileHandler.getText("data/" + this.scenario.getPath() + "/conversations/" + npc.getConversationId() + ".txt"));
        this.dashboard.print("A connection with " + npc.getName() + " has been established...");
        this.dashboard.print(npc.getName() + " looks like " + npc.getDescription());
        this.dashboard.print(npc.getName() + ": " + this.currentConversation.getQText());
    }

    /**
     * Takes in what the user has answered using the say command, and figures
     * out whether it is recognized It also calls for the execution of the
     * execution line
     *
     * @param answer is the second word that the user typed in along with say
     */
    @Override
    public void processAnswer(String answer) {
        if (answer == null) {
            return;
        }

        if (this.currentConversation == null) {
            this.dashboard.print("Sorry, you can't use say when you have no ongoing conversation!");
            return;
        }

        NPCHolder npcHolder = getNPCHolderFromUuid(this.player.getPlanetId());

        if (!npcHolder.hasNpcId(this.currentConversation.getNpcId())) {
            this.dashboard.print("Sorry, you're no longer at the same position as the NPC and can therefore not talk with him!");
            this.currentConversation = null;
            return;
        }

        this.currentConversation.processAnswer(answer);
        if (this.currentConversation.hasCurrentAnswer()) {
            this.dashboard.print(this.npcs.get(this.currentConversation.getNpcId()).getName() + ": " + this.currentConversation.getReactText());
            if (!this.processExecution(this.currentConversation.getExecutionLine(), this.currentConversation.getNpcId())) {
                if (this.currentConversation.getNextLineNumber() == -1) {
                    this.currentConversation = null;
                    this.dashboard.print("Conversation has been terminated");
                    return;
                }
                this.currentConversation.setNextQuestion(this.currentConversation.getNextLineNumber());
            }
            if(this.currentConversation.getQuestionNumber() < 1) {
                this.currentConversation = null;
                this.dashboard.print("Conversation has been terminated");
                return;
            }
            this.dashboard.print(this.npcs.get(this.currentConversation.getNpcId()).getName() + ": " + this.currentConversation.getQText());
        } else {
            this.dashboard.print(this.npcs.get(this.currentConversation.getNpcId()).getName() + ": Sorry, I don't know how to respond to that answer.");
        }
    }

    /**
     * Figures out what should happen according to the parameter executionLine,
     * and calls the relevant methods
     *
     * @param executionLine which commands that are to be executed
     * @param npcId which npc that the conversation is with
     * @return whether or not the conversation's question was changed during the
     * execution commands
     */
    private boolean processExecution(String executionLine, UUID npcId) {
        boolean changedQuestion = false;
        String[] allExecutions;
        allExecutions = executionLine.split(",");
        for (String eachExecution : allExecutions) {
            String[] executionSplit = eachExecution.split(":");
            //Depending on which string executionSplit[0] contains, the program will choose one of the following cases.
            //If not, the default case will be used, which does nothing, thus spelling errors in the conversation files is ignored.
            switch (executionSplit[0]) {
                case "deliverPackage":
                    this.deliverPackage(npcId);
                    break;
                case "pickupPackage":
                    this.pickupPackage(npcId);
                    break;

                case "nextConvoId":
                    try {
                        int convoId = Integer.parseInt(executionSplit[1]);
                        this.npcs.get(npcId).setNextConversationId(convoId);
                    } catch (NumberFormatException e) {

                    }
                    break;

                case "checkPackage":
                    this.checkPackage(npcId, executionSplit[1]);
                    changedQuestion = true;
                    break;

                case "checkPickup":
                    this.checkPickup(npcId, executionSplit[1]);
                    changedQuestion = true;
                    break;

                case "removeReputation":
                    try {
                        int reputationAmount = Integer.parseInt(executionSplit[1]);
                        this.player.setReputation((this.player.getReputation() - reputationAmount));
                    } catch (NumberFormatException e) {

                    }
                    if(this.player.getReputation() < 1) {
                        this.isDead = true;
                    }
                    break;

                case "getPapers":
                    this.getPapers();
                    break;

                case "checkExtraDeliveryTime":
                    this.checkExtraDeliveryTime(executionSplit[1]);
                    changedQuestion = true;
                    break;
                    
                case "getExtraDeliveryTime":
                    this.getExtraDeliveryTime();
                    break;

                case "checkBuyWarpFuel":
                    this.checkBuyWarpFuel(executionSplit[1]);
                    changedQuestion = true;
                    break;

                case "buyWarpFuel":
                    this.player.addWarpfuel(50);
                    this.player.setReputation(this.player.getReputation() - 5);
                    break;

                case "setAllowWarp":
                    this.player.setCanWarp(true);
                    this.dashboard.print("Dashboard: wow! I just got some warp equipment that are ready for use, just enable it in the top right to use it!");
                    break;

                default:
                    break;
            }
        }

        return changedQuestion;
    }

    /**
     * A method used for execution commands from the Conversation. The method
     * delivers a package according to the package id an NPC has. This method
     * does not include if the player has the package or not, and should
     * therefore only be executed after the "checkPackage".
     *
     * @param npcId the npc which has to receive the package
     */
    private void deliverPackage(UUID npcId) {
        Item item = this.items.get(this.npcs.get(npcId).getPackageId());
        this.player.setReputation(this.player.getReputation() + item.getReputationWorth());
        this.player.removeItem(item.getId(), item.getWeight());
        if (this.time <= item.getDeliveryTime()) {
            this.dashboard.print("Congratulations, you delivered " + item.getDescription() + " on time!");
            this.player.setReputation(this.player.getReputation() + item.getReputationWorth());
        } else {
            this.dashboard.print("Unfortunately, you did not deliver  " + item.getDescription() + " on time!");
            this.player.setReputation(this.player.getReputation() - item.getReputationWorth());
        }
        if (!item.getPapers()) {
            this.dashboard.print("Since you did not have the papers for " + item.getDescription() + " you lost some reputation. Go see the Headquarter for papers on your packages!");
            this.player.setReputation(this.player.getReputation() - (item.getReputationWorth() * 3));
        }
        if(this.player.getReputation() < 1) {
            this.isDead = true;
        }
    }

    /**
     * This method is executed from the execution commands from Conversation.
     * This method picks up every package the NPC has, if you don't have space
     * for the package you're trying to pick up, it will print that it failed.
     *
     * @param npcId the npc that the user picks up packages from
     * @return whether you succeeded or not to pick up all the packages
     */
    private boolean pickupPackage(UUID npcId) {
        incrementTime(1);
        for (UUID itemUuid : this.npcs.get(npcId).getInventoryUuids()) {
            if (this.player.addItem(itemUuid, this.items.get(itemUuid).getWeight())) {
                this.npcs.get(npcId).removeItem(itemUuid, this.items.get(itemUuid).getWeight());
                this.items.get(itemUuid).setDeliveryTime(this.time + 200);
            } else {
                this.dashboard.print("You were unable to pick up " + this.items.get(itemUuid).getDescription() + ", since you don't have space in your inventory!");
                return false;
            }
        }
        return true;
    }

    /**
     * A method used to execute executionlines from Conversation. It will set
     * two different next questions, according to whether the player has a
     * package UUID that is equal to what the NPC wants delivered.
     *
     * @param npcId the npc that the player wants to deliver to
     * @param executionSplit used to get the two different question numbers that
     * you have to proceed to
     */
    private void checkPackage(UUID npcId, String executionSplit) {
        String[] whichQuestion = executionSplit.split(";");
        int[] questionNumbers = new int[2];
        try {
            questionNumbers[0] = Integer.parseInt(whichQuestion[0]);
            questionNumbers[1] = Integer.parseInt(whichQuestion[1]);
        } catch (NumberFormatException e) {
            System.out.println("Something is wrong with the conversation files, please contact the developers!");
        }

        for (UUID itemUuid : this.player.getInventoryUuids()) {
            if (this.npcs.get(npcId).getPackageId() == itemUuid) {
                this.currentConversation.setNextQuestion(questionNumbers[0]);
                return;
            }
        }

        this.currentConversation.setNextQuestion(questionNumbers[1]);
    }

    /**
     * This method is used to execute executionlines from Conversation. This
     * sets two different question numbers according to whether the npc has any
     * packages to pickup or not.
     *
     * @param npcId the npc to check whether it has items to pickup or not
     * @param executionSplit used to extract which question to head to next
     */
    private void checkPickup(UUID npcId, String executionSplit) {
        String[] whichQuestion = executionSplit.split(";");
        int[] questionNumbers = new int[2];
        try {
            questionNumbers[0] = Integer.parseInt(whichQuestion[0]);
            questionNumbers[1] = Integer.parseInt(whichQuestion[1]);
        } catch (NumberFormatException e) {

        }

        if (this.npcs.get(npcId).getInventoryUuids().length > 0) { //The NPC can have 0 items
            Item curItem = this.items.get(this.npcs.get(npcId).getInventoryUuids()[0]);
            if (this.player.hasInventorySpaceFor(curItem.getWeight())) {
                this.currentConversation.setNextQuestion(questionNumbers[0]);
                return;
            }
        }

        this.currentConversation.setNextQuestion(questionNumbers[1]);
    }

    /**
     * This method is used to execute executionlines from Conversation. This
     * takes all of the players items and adds papers to them.
     */
    private void getPapers() {
        for (UUID uuid : this.player.getInventoryUuids()) {
            this.items.get(uuid).setPapersTrue();
        }
    }

    /**
     * Check whether or not it is possible to get more time for delivering the player's items.
     * 
     * @param executionSplit the string that contains the next question numbers
     */
    private void checkExtraDeliveryTime(String executionSplit) {
        String[] whichQuestion = executionSplit.split(";");
        int[] questionNumbers = new int[2];
        try {
            questionNumbers[0] = Integer.parseInt(whichQuestion[0]);
            questionNumbers[1] = Integer.parseInt(whichQuestion[1]);
        } catch (NumberFormatException e) {
            System.out.println("Something is wrong with the conversation files, please contact the developers!");
        }

        if(this.time > this.timerCounts.get("extraDeliveryTime")) {
            this.currentConversation.setNextQuestion(questionNumbers[0]);
            this.timerCounts.put("extraDeliveryTime", this.time + 500);
            return;
        }

        this.currentConversation.setNextQuestion(questionNumbers[1]);
    }
    
    /**
     * This method is used to execute executionlines from Conversation. This
     * takes all of the players items and adds extra time to their delivery time
     * to the items.
     */
    private void getExtraDeliveryTime() {
        for (UUID uuid : this.player.getInventoryUuids()) {
            Item item = this.items.get(uuid);
            item.setDeliveryTime(item.getDeliveryTime() + 200);
        }
        this.dashboard.print("Dashboard: I just received a bunch of messages! Most of them spam, but some of them state that the delivery time for each of your packages has been pushed to a later point.");
        this.dashboard.print("Dashboard: I guess it is time for a coffee break then!");
        this.dashboard.print("Dashboard: ....");
        this.dashboard.print("Dashboard: Wait... I don't drink coffee...");
    }

    /**
     * This method is used to execute executionlines from Conversation. This
     * checks whether or not the player can buy warp fuel
     *
     * @param executionSplit which contains the question numbers for the next
     * questions
     */
    private void checkBuyWarpFuel(String executionSplit) {
        String[] whichQuestion = executionSplit.split(";");
        int[] questionNumbers = new int[2];
        try {
            //Note, the split command somehow splits "1|2" into three array indexes: "1", "|" and "2"
            questionNumbers[0] = Integer.parseInt(whichQuestion[0]);
            questionNumbers[1] = Integer.parseInt(whichQuestion[1]);
        } catch (NumberFormatException e) {
            System.out.println("Something is wrong with the conversation files, please contact the developers!");
        }

        if (this.player.canWarp() && this.player.getReputation() > 5) {
            this.currentConversation.setNextQuestion(questionNumbers[0]);
            return;
        }
        this.currentConversation.setNextQuestion(questionNumbers[1]);
    }

    /**
     * Creates the planets!
     *
     * @return what UUID the player should be starting on
     */
    private UUID createPlanets() {

        UUID returnUuid = null;
        //Creating the items list
        int i = 0;
        while (true) {
            if (!this.fileHandler.doesFileExist("data/" + this.scenario.getPath() + "/planets/" + i + ".json")) {
                break;
            }
            Planet newPlanet = this.fileHandler.getJSON("data/" + this.scenario.getPath() + "/planets/" + i + ".json", Planet.class);
            this.planets.put(newPlanet.getId(), newPlanet);
            i++;

            if (newPlanet.getPid() == 0) {
                returnUuid = newPlanet.getId();
            }
        }

        createMoons();

        return returnUuid;
    }

    /**
     * This creates the moons from JSON files, and places them according to
     * their PID. This method assumes that every Moon has a PID that will match
     * a planet's PID.
     */
    private void createMoons() {

        int i = 0;
        while (true) {
            if (!this.fileHandler.doesFileExist("data/" + this.scenario.getPath() + "/moons/" + i + ".json")) {
                break;
            }
            Moon newMoon = this.fileHandler.getJSON("data/" + this.scenario.getPath() + "/moons/" + i + ".json", Moon.class);
            this.moons.put(newMoon.getId(), newMoon);
            i++;
        }

        HashMap<Integer, Planet> planetPids = new HashMap<>();
        for (Planet planet : this.planets.values()) {
            planetPids.put(planet.getPid(), planet);
        }

        for (Moon moon : this.moons.values()) {
            if (planetPids.containsKey(moon.getPid())) {
                planetPids.get(moon.getPid()).setMoonUuid(moon.getId());
                moon.setParentPlanetUuid(planetPids.get(moon.getPid()).getId());
            }
        }
    }

    /**
     * Creates the NPCs
     */
    private void createNpcs() {
        int i = 0;
        while (true) {
            if (!this.fileHandler.doesFileExist("data/" + this.scenario.getPath() + "/civilians/" + i + ".json")) {
                break;
            }
            NPC newNpc = this.fileHandler.getJSON("data/" + this.scenario.getPath() + "/civilians/" + i + ".json", NPC.class);
            this.npcs.put(newNpc.getId(), newNpc);
            this.civilians.put(newNpc.getId(), newNpc);
            
            if(i == 0) {
                this.startNpc = newNpc.getId();
            }
            i++;
        }

        ArrayList<NPCHolder> npcHolders = new ArrayList<>();
        for (Planet planet : this.planets.values()) {
            npcHolders.add(planet);
        }

        placeNpcs(this.civilians.values(), npcHolders);

        createRebels();
    }

    /**
     * A method used to create the rebels from JSON files. The method calls the
     * method placeNPCs with the list of rebels and moons.
     */
    private void createRebels() {

        int i = 0;
        while (true) {
            if (!this.fileHandler.doesFileExist("data/" + this.scenario.getPath() + "/rebels/" + i + ".json")) {
                break;
            }
            NPC newNpc = this.fileHandler.getJSON("data/" + this.scenario.getPath() + "/rebels/" + i + ".json", NPC.class);
            this.npcs.put(newNpc.getId(), newNpc);
            this.rebels.put(newNpc.getId(), newNpc);
            i++;
        }

        ArrayList<NPCHolder> npcHolders = new ArrayList<>();
        for (Moon moon : this.moons.values()) {
            npcHolders.add(moon);
        }
        placeNpcs(this.rebels.values(), npcHolders);
    }

    /**
     * A method for placing NPCs according to the two parameters. The method
     * goes through 3 steps when placing NPCs: 1. it tries to match NPCs with
     * PIDs and planets/moons with PIDs. Which can mean several NPCs at the same
     * planet. 2. it then adds NPCs (who has no PID) to planets/moons without
     * NPCs. 3. it then adds NPCs (who has no PID) to random planets/moons.
     * These steps makes sure, that if there is NPCs without PIDs, these NPCs
     * will be placed on empty planets/moons, and when there is no more empty
     * planets/moons, NPCs without PIDs will be placed "randomly".
     *
     * @param npcList the list of NPCs to place (this "list" comes from the
     * .values() from a HashMap)
     * @param holdersList of the type NPCHolder, which is the superclass for
     * Planets and Moons. NPCHolder holds the information and behaviour that
     * handles NPCs at planets/moons.
     */
    private void placeNpcs(Collection<NPC> npcList, ArrayList<NPCHolder> holdersList) {
        //An array list that holds the planets/moons without an NPC.
        //By the start all planets/moons are a part of this list.
        //The planets/moons are removed from this list when they get an NPC.
        ArrayList<NPCHolder> hasNoNpc = new ArrayList<>();
        //A HashMap, which makes it easy for this method to fecth the right planet/moon according to their PID.
        //A PID is unique for each planet/moon.
        HashMap<Integer, NPCHolder> planetPids = new HashMap<>();
        //A list used for step 2 and 3 (see the method Javadoc). 
        //It holds all of the NPCs without a PID.
        ArrayList<NPC> hasNoPid = new ArrayList<>();

        //Place all of the planets/moons in the lists.
        for (NPCHolder npcHolder : holdersList) {
            hasNoNpc.add(npcHolder);
            planetPids.put(npcHolder.getPid(), npcHolder);
        }

        //Goes through the whole list of NPCs that has to be placed, and places them if they have an PID.
        //If they don't have a PID, the NPC will be added to the list "hasNoPid".
        for (NPC npc : npcList) {
            if (npc.getPid() == -1 || !planetPids.containsKey(npc.getPid())) {
                hasNoPid.add(npc);
            } else {
                planetPids.get(npc.getPid()).addNpcId(npc.getId());
                npc.setPlanetId(planetPids.get(npc.getPid()).getId());
                if (hasNoNpc.contains(planetPids.get(npc.getPid()))) {
                    hasNoNpc.remove(planetPids.get(npc.getPid()));
                }
            }
        }

        //Goes through the NPC list that has no PID and places them on empty planets/moons,
        // stops when there are not empty planets/moons left.
        int i = 0;
        for (NPC npc : hasNoPid) {
            //If the planet/moon list that has no NPC is empty, break this loop
            if (hasNoNpc.isEmpty()) {
                break;
            }

            if (i >= hasNoNpc.size()) {
                break;
            }

            hasNoNpc.get(i).addNpcId(npc.getId());
            npc.setPlanetId(hasNoNpc.get(i).getId());
            hasNoNpc.remove(i);
            i++;
        }

        //The NPCHolder list is made into an array, as it is easier to acces random entries in that compared to a list.
        NPCHolder[] planets = new NPCHolder[holdersList.size()];
        holdersList.toArray(planets);
        for (NPC npc : hasNoPid) {
            //If the NPC already has a planet, skip placing them. (Should hopefully not be needed)
            if (npc.getPlanetId() != null) {
                continue;
            }

            //Random which planet/moon that should get the next NPC
            i = (int) (Math.random() * holdersList.size());

            planets[i].addNpcId(npc.getId());
            npc.setPlanetId(planets[i].getId());
        }
    }

    /**
     * This method is used for creating items from JSON files, determining
     * receivers and placing the items. This method follows the algorithm (it is
     * simplified here): 1. Creating the items from JSON files, and filling
     * necessary lists 2. Making sure that there is as many items used run time
     * as there is NPCs (and more filling of lists) 3a. Finding and adding
     * receivers according to the RIDs of both NPCs and items. 3b. Finding and
     * adding receivers that have no RIDs 4a. Finding and placing items at the
     * right NPCs based on PIDs 4b. Finding and placing items without PIDs
     */
    private void createItems() {
        //There is more JSON files with items, than there actually has to be used in game.
        //This list holds all the items currently in use
        ArrayList<Item> itemsUsed = new ArrayList<>();

        //Contains the items that has no delivery and pickup place.
        //All items starts out in these lists, and slowly gets removed during this method.
        ArrayList<Item> itemsHaveNoDelivery = new ArrayList<>();
        ArrayList<Item> itemsHaveNoPickup = new ArrayList<>();

        //Contains the NPCS that has no package it needs delivered and item the player can pickup.
        //All items starts out in these lists, and slowly gets removed during this method.
        ArrayList<NPC> npcsHaveNoDelivery = new ArrayList<>();
        ArrayList<NPC> npcsHaveNoPickup = new ArrayList<>();

        //Holds all of the items and npcs that has a PID or RID
        HashMap<Integer, Item> itemsWithRid = new HashMap<>(); //Could just a be a list, as the key is never used
        HashMap<Integer, Item> itemsWithIid = new HashMap<>(); //Could just a be a list, as the key is never used
        HashMap<Integer, NPC> npcsWithRid = new HashMap<>();
        HashMap<Integer, NPC> npcsWithIid = new HashMap<>();

        //Filling the lists with NPCs
        for (NPC npc : this.npcs.values()) {
            npcsHaveNoDelivery.add(npc);
            npcsHaveNoPickup.add(npc);
            if (npc.getRid() != -1) {
                npcsWithRid.put(npc.getRid(), npc);
            }
            if (npc.getIid() != -1) {
                npcsWithIid.put(npc.getIid(), npc);
            }
        }

        //1. Creating the items from JSON
        int i = 0;
        while (true) {
            if (!this.fileHandler.doesFileExist("data/" + this.scenario.getPath() + "/items/" + i + ".json")) {
                break;
            }
            Item newItem = this.fileHandler.getJSON("data/" + this.scenario.getPath() + "/items/" + i + ".json", Item.class);
            this.items.put(newItem.getId(), newItem);
            i++;
            if (npcsWithIid.containsKey(newItem.getIid()) || npcsWithRid.containsKey(newItem.getRid())) {
                itemsUsed.add(newItem);
            }

        }

        //2. Fill up itemsUsed, so that it has as many items as there are NPCs
        ArrayList<Item> allItems = new ArrayList<>(this.items.values());
        //As long as the size of items used is smaller than the list of NPCs
        while (itemsUsed.size() < this.npcs.size()) {
            while (true) {
                int randomIndex = (int) (Math.random() * allItems.size());

                //If the random picked item is already stated as being used,
                // it will skip the rest of the while(true) and generate a new random index.
                if (itemsUsed.contains(allItems.get(randomIndex))) {
                    continue;
                }

                itemsUsed.add(allItems.get(randomIndex));
                break;
            }
        }

        //START: Filling the lists and hashmaps for items
        for (Item item : itemsUsed) {
            itemsHaveNoDelivery.add(item);
            itemsHaveNoPickup.add(item);
            if (item.getRid() != -1) {
                itemsWithRid.put(item.getRid(), item);
                item.setPapersTrue();
            }
            if (item.getIid() != -1) {
                itemsWithIid.put(item.getIid(), item);
                item.setPapersTrue();
            } else {
                if(Math.random() > 0.5) {
                    item.setPapersFalse();
                } else {
                    item.setPapersTrue();
                }
                
            }
        }
        //END: Filling the lists and hashmaps for items

        //START: 2a. Adding receivers to both items and npcs
        for (Item item : itemsWithRid.values()) {
            //Uses the NPC HashMaps
            if (npcsWithRid.containsKey(item.getRid())) {
                NPC npc = npcsWithRid.get(item.getRid());
                npc.setPackageId(item.getId());
                item.setNpcId(npc.getId());

                itemsHaveNoDelivery.remove(item);
                npcsHaveNoDelivery.remove(npc);
            }
        }

        //2b. Adding receivers for items and NPCs without an RID
        for (Item item : itemsHaveNoDelivery) {
            if (npcsHaveNoDelivery.size() > 0) {
                int randomNpcIndex = (int) (Math.random() * npcsHaveNoDelivery.size());
                NPC npc = npcsHaveNoDelivery.get(randomNpcIndex);
                item.setNpcId(npc.getId());
                npc.setPackageId(item.getId());

                npcsHaveNoDelivery.remove(npc);
            } else {
                break;
            }
        }
        //END: Adding receivers to both items and npcs

        //START: 3a. Adding where the items are going to be picked up
        for (Item item : itemsWithIid.values()) {
            if (npcsWithIid.containsKey(item.getIid())) {
                NPC npc = npcsWithIid.get(item.getIid());
                npc.addItem(item.getId(), item.getWeight());

                itemsHaveNoPickup.remove(item);
                npcsHaveNoPickup.remove(npc);
            }
        }

        //3b. Adding where items without and PID are going to be picked up
        for (NPC npc : npcsHaveNoPickup) {
            if (itemsHaveNoPickup.size() <= 0) {
                System.out.println("Something is wrong with the conversation files, please contact the developers!");
                break;
            }
            if (itemsHaveNoPickup.size() == 2) {
                if (itemsHaveNoPickup.get(0).getId().equals(npc.getPackageId())) {
                    itemsHaveNoPickup.get(1).setNpcId(npc.getId());
                    npc.addItem(itemsHaveNoPickup.get(1).getId(), itemsHaveNoPickup.get(1).getWeight());
                    itemsHaveNoPickup.remove(1);
                } else {
                    itemsHaveNoPickup.get(0).setNpcId(npc.getId());
                    npc.addItem(itemsHaveNoPickup.get(0).getId(), itemsHaveNoPickup.get(0).getWeight());
                    itemsHaveNoPickup.remove(0);
                }
                continue;
            }
            while (true) {
                int randomItemIndex = (int) (Math.random() * itemsHaveNoPickup.size());
                Item item = itemsHaveNoPickup.get(randomItemIndex);

                if (npc.getPackageId().equals(item.getId())) {
                    if(itemsHaveNoPickup.size() == 1) {
                        UUID tempItemUuid = itemsHaveNoPickup.get(0).getId();
                        UUID tempNpcUuid = this.items.get(tempItemUuid).getNpcId();
                        System.out.println("Deleted NPC: " + this.npcs.get(tempNpcUuid));
                        this.npcs.remove(tempNpcUuid);
                        if(this.civilians.containsKey(tempNpcUuid)) {
                            this.civilians.remove(tempNpcUuid);
                        } else if(this.rebels.containsKey(tempNpcUuid)) {
                            this.rebels.remove(tempNpcUuid);
                        }
                        break;
                    }
                    continue;
                }

                npc.addItem(item.getId(), item.getWeight());
                itemsHaveNoPickup.remove(item);
                break;
            }
        }
        //END: Adding where the items are going to be picked up
    }

    /**
     * Gets a NPCHolder object from an UUID, this can either be a planet or moon
     * UUID.
     *
     * @param positionUuid a planet or moon UUID
     * @return the NPCHolder object
     */
    private NPCHolder getNPCHolderFromUuid(UUID positionUuid) {
        if (this.planets.containsKey(positionUuid)) {
            return this.planets.get(positionUuid);
        } else {
            return this.moons.get(positionUuid);
        }
    }

    /**
     * Gets the coordinates of a position based on a UUID
     *
     * @param positionUuid a planet or moons UUID
     * @return an integer array of the size 2, with the x on index 0 and y on
     * index 1
     */
    @Override
    public int[] getPositionCoordinates(UUID positionUuid) {
        Planet planet;
        if (this.planets.containsKey(positionUuid)) {
            planet = this.planets.get(positionUuid);
        } else {
            planet = this.planets.get(this.moons.get(positionUuid).getParentPlanetUuid());
        }
        int[] returnArray = new int[2];
        returnArray[0] = planet.getx();
        returnArray[1] = planet.gety();
        return returnArray;

    }

    /**
     * This method prepares and calls the method that does the actual
     * calculation for whether the NPC should move or not.
     */
    private void tryNpcMovement() {
        ArrayList<NPCHolder> npcHolders = new ArrayList<>();
        for (Moon moon : this.moons.values()) {
            npcHolders.add(moon);
        }
        tryNpcMovementCalculations(this.rebels.values(), npcHolders);

        npcHolders = new ArrayList<>();
        for (Planet planet : this.planets.values()) {
            npcHolders.add(planet);
        }
        tryNpcMovementCalculations(this.civilians.values(), npcHolders);
    }

    /**
     * A method that goes through all of the NPCs passed in the parameter and
     * places them at the planets/moons passed in the parameter.
     *
     * @param npcList The NPCs that has to be placed
     * @param holdersList The places the NPCs can be placed
     */
    private void tryNpcMovementCalculations(Collection<NPC> npcList, ArrayList<NPCHolder> holdersList) {
        for (NPC npc : npcList) {
            if(this.currentConversation != null) {
                if(this.currentConversation.getNpcId().equals(npc.getId())) {
                    continue;
                }
            }
            
            NPCHolder[] npcHolders = new NPCHolder[holdersList.size()];
            holdersList.toArray(npcHolders);

            if (npc.getChanceToMove() > 0) {
                int randomNumber = (int) (Math.random() * 10);
                if (npc.getChanceToMove() > randomNumber) {
                    int randomPlanet = (int) (Math.random() * npcHolders.length);

                    //Make sure that the random generated new position, 
                    // is not already the NPC's position.
                    while (npcHolders[randomPlanet].getId() == npc.getPlanetId()) {
                        randomPlanet = (int) (Math.random() * npcHolders.length);
                    }

                    //Move the NPC
                    getNPCHolderFromUuid(npc.getPlanetId()).removeNpcId(npc.getId());
                    npc.setPlanetId(npcHolders[randomPlanet].getId());
                    npcHolders[randomPlanet].addNpcId(npc.getId());
                }
            }
        }
    }

    /**
     * Checks the current player's highscore, and if that highscore is better
     * than the one fetched from the JSON file, save the current player's
     * highscore as the highest.
     */
    private void saveHighScore() {
        this.currentPlayerScore.setRep(this.player.getReputation());
        this.currentPlayerScore.setTime(this.time);
        Collections.sort(this.highScores);
        for (int i = 0; i < 10; i++) {
            this.fileHandler.writeToFile("data/" + this.scenario.getPath() + "/highscores/" + i + ".json", this.highScores.get(i).toJsonString());
        }
    }

    /**
     * A method for reading all of the highscore JSON files and storing them.
     */
    private void createHighscores() {
        for (int i = 0; i < 10; i++) {
            if (!this.fileHandler.doesFileExist("data/" + this.scenario.getPath() + "/highscores/" + i + ".json")) {
                break;
            }

            HighScore newHighScore = this.fileHandler.getJSON("data/" + this.scenario.getPath() + "/highscores/" + i + ".json", HighScore.class);
            this.highScores.add(newHighScore);
        }
        this.currentPlayerScore = new HighScore(this.player.getReputation(), this.time, this.player.getName());
        this.highScores.add(currentPlayerScore);
        Collections.sort(this.highScores);
    }

    /**
     * Methods for incrementing the time ingame
     *
     * @param i the amount to increment the time with
     */
    private void incrementTime(int i) {
        this.time += i;

        //War timer check
        if (this.timerCounts.get("warTimer") <= this.time) {
            tryStartWars(0.1, 100);
            this.timerCounts.put("warTimer", this.timerCounts.get("warTimer") + 150);
        }
        
        //Try NPC movement timer check
        if (this.timerCounts.get("tryNpcMovement") <= this.time) {
            tryNpcMovement();
            this.timerCounts.put("tryNpcMovement", this.timerCounts.get("tryNpcMovement") + 100);
        }
    }

    /**
     * For each planet and moon, it attempts to start a war.
     *
     * @param chance between 0 and 1, determines how big of a chance there is,
     * the closer to 1, then bigger the chance
     * @param length the length in the time unit
     */
    private void tryStartWars(double chance, int length) {
        //What if the planet already has a war? Fine! It will just extend it!

        for (Planet planet : this.planets.values()) {
            if (Math.random() < chance) {
                planet.setWarTimer(this.time + length);
            }
        }

        for (Moon moon : this.moons.values()) {
            if (Math.random() < chance) {
                moon.setWarTimer(this.time + length);
            }
        }
    }

    /**
     * A method used to create the possible scenarios based on the file, that is
     * located in the root of the data folder. It saves these possible scenarios
     * in the hashmap.
     */
    private void createScenarios() {
        if (!this.fileHandler.doesFileExist("data/scenarios.txt")) {
            System.out.println("The scenarios file is broken!");
        }

        for (String scenarioLine : this.fileHandler.getText("data/scenarios.txt")) {
            String[] splittedScenarioLine = scenarioLine.split(";");
            Scenario scenario = new Scenario(splittedScenarioLine[0], splittedScenarioLine[2], splittedScenarioLine[1]);
            this.possibleScenarios.put(scenario.getId(), scenario);
        }
    }

    /**
     * A getter method for GUI, to get all of the planets' UUID.
     *
     * @return an arraylist of UUIDs
     */
    @Override
    public ArrayList<UUID> getListOfPlanets() {
        return new ArrayList<>(this.planets.keySet());
    }

    /**
     * Returns the name that belongs to the UUID passed in through the
     * parameter.
     *
     * @param uuid the UUID that you want to get the name for
     * @return a string that holds the name
     */
    @Override
    public String getName(UUID uuid) {
        PrintAble printAble;
        if (this.npcs.containsKey(uuid)) {
            printAble = this.npcs.get(uuid);
        } else if (this.items.containsKey(uuid)) {
            printAble = this.items.get(uuid);
        } else if (this.planets.containsKey(uuid) || this.moons.containsKey(uuid)) {
            printAble = this.getNPCHolderFromUuid(uuid);
        } else if (this.possibleScenarios.containsKey(uuid)) {
            printAble = this.possibleScenarios.get(uuid);

        } else {
            return null;
        }
        return printAble.getName();
    }

    /**
     * Returns the name that belongs to the UUID passed in through the
     * parameter.
     *
     * @param uuid the UUID that you want to get the name for
     * @return a string that holds the name
     */
    @Override
    public String getDescription(UUID uuid) {
        PrintAble printAble;
        if (this.npcs.containsKey(uuid)) {
            printAble = this.npcs.get(uuid);
        } else if (this.items.containsKey(uuid)) {
            printAble = this.items.get(uuid);
        } else if (this.planets.containsKey(uuid) || this.moons.containsKey(uuid)) {
            printAble = this.getNPCHolderFromUuid(uuid);
        } else if (this.possibleScenarios.containsKey(uuid)) {
            printAble = this.possibleScenarios.get(uuid);
        } else {
            return null;
        }

        return printAble.getDescription();
    }

    /**
     * Gets the PID for the UUID passed in through the parameter.
     *
     * @param uuid the UUID you want to get the PID for
     * @return a int that is the PID
     */
    @Override
    public int getPid(UUID uuid) {
        return this.getNPCHolderFromUuid(uuid).getPid();
    }

    /**
     * A getter method for an object's image's path, so the GUI can get the
     * image by itself.
     *
     * @param uuid the UUID
     * @param isIcon if this is true, the method will attempt to fetch an icon
     * @return an image
     */
    @Override
    public String getImgPath(UUID uuid, boolean isIcon) {
        PicturizeAble picturizeAble;
        if (this.npcs.containsKey(uuid)) {
            picturizeAble = this.npcs.get(uuid);
        } else if (this.items.containsKey(uuid)) {
            picturizeAble = this.items.get(uuid);
        } else if (this.planets.containsKey(uuid) || this.moons.containsKey(uuid)) {
            picturizeAble = this.getNPCHolderFromUuid(uuid);
        } else {
            return null;
        }

        String folder = "images";
        if (isIcon) {
            folder = "icons";
        }

        String returnString = "data/" + this.scenario.getPath() + "/" + folder + "/" + picturizeAble.getImagePath();
        return returnString;
    }

    /**
     * Returns the image path for a certain UUID.
     *
     * @param uuid the uuid to get a image path for
     * @return the string that contains the path
     */
    @Override
    public String getImgPath(UUID uuid) {
        return this.getImgPath(uuid, false);
    }

    /**
     * Returns the player's fuel amount.
     *
     * @return an integer
     */
    @Override
    public int getFuel() {
        return this.player.getFuel();
    }

    /**
     * Returns the player's warp fuel amount
     *
     * @return an integer
     */
    @Override
    public int getWarpFuel() {
        return this.player.getWarpfuel();
    }

    /**
     * Returns whether the player can warp or not.
     *
     * @return a boolean
     */
    @Override
    public boolean canWarp() {
        return this.player.canWarp();
    }

    /**
     * Returns the amount of reputation that a player has.
     *
     * @return an integer
     */
    @Override
    public int getReputation() {
        return this.player.getReputation();
    }

    /**
     * Returns the ingame time (NOT the real time)
     *
     * @return an integer
     */
    @Override
    public int getInGameTime() {
        return this.time;
    }

    /**
     * Returns the saved string in the dashboard, that are used from every place
     * where something wants to print something.
     *
     * @return a string, that can contain multiple line breaks.
     */
    @Override
    public String getDashboardUpdate() {
        return this.dashboard.getSavedString();
    }

    /**
     * Gets an array list of UUIDs for the player's inventory
     *
     * @return an ArrayList of UUIDs
     */
    @Override
    public ArrayList<UUID> getInventory() {
        ArrayList<UUID> returnArray = new ArrayList<>();
        for (UUID uuid : this.player.getInventoryUuids()) {
            returnArray.add(uuid);
        }
        return returnArray;
    }

    /**
     * Gets the NPCs at a current planet or moon.
     *
     * @param uuid the uuid of the planet or moon.
     * @return an ArrayList of UUIDs
     */
    @Override
    public ArrayList<UUID> getAvailableNpcs(UUID uuid) {
        NPCHolder npcHolder = this.getNPCHolderFromUuid(uuid);
        ArrayList<UUID> returnArray = new ArrayList<>();
        for (UUID npcUuid : npcHolder.getNpcIds()) {
            returnArray.add(npcUuid);
        }
        return returnArray;
    }

    /**
     * Gets the possible planets that are reachable with the amount of fuel
     *
     * @return an ArrayList of UUID
     */
    @Override
    public ArrayList<UUID> getPossiblePlanets() {
        int[] currentPosition = this.getPositionCoordinates(this.player.getPlanetId());
        return this.getPossiblePlanets(currentPosition[0], currentPosition[1], this.player.getFuel());
    }

    /**
     * Changes the player's current UUID.
     *
     * @param uuid the uuid of the planet or moon that is the target of the
     * travel
     */
    @Override
    public void travelToPlanet(UUID uuid) {
        this.travelToPlanet(this.player, uuid);
    }

    /**
     * Gets an array of answers.
     *
     * @return a String array
     */
    @Override
    public String[] getAnswers() {
        if (this.currentConversation == null) {
            return null;
        }
        return this.currentConversation.getPossibleAnswers();
    }

    /**
     * Drops an item based on an item's UUID
     *
     * @param uuid of the item to be removed
     */
    @Override
    public void dropItem(UUID uuid) {
        for (UUID itemId : this.player.getInventoryUuids()) {
            if (itemId == uuid) {
                this.player.removeItem(itemId, this.items.get(itemId).getWeight());
                this.player.setReputation(this.player.getReputation() - this.items.get(itemId).getReputationWorth());
                if(this.player.getReputation() < 1) {
                    this.isDead = true;
                }
                return;
            }
        }
    }

    /**
     * Uses a the warp method to travel the player
     *
     * @param nextPosition the UUID of the targeted planet or moon
     */
    @Override
    public void processWarp(UUID nextPosition) {
        this.processWarp(this.player, nextPosition);
    }

    /**
     * Gets the UUID of the planet or moon that the player is currently on.
     *
     * @return a UUID
     */
    @Override
    public UUID getPlayerPosition() {
        return this.player.getPlanetId();
    }

    /**
     * Gets the moon UUID for a planet. If the planet does not have a moon, it
     * will return null.
     *
     * @param uuid the planet's UUID
     * @return a UUID for the moon
     */
    @Override
    public UUID getMoonId(UUID uuid) {
        if (this.moons.containsKey(uuid)) {
            return null;
        }
        if (this.planets.get(uuid).hasMoon()) {
            return this.planets.get(uuid).getMoonUuid();
        } else {
            return null;
        }
    }

    /**
     * Gets the possible scenarios for the game as a list of UUIDs.
     *
     * @return an arraylist of UUIDs
     */
    @Override
    public ArrayList<UUID> getPossibleScenarios() {
        ArrayList<UUID> returnArray = new ArrayList<>();
        for (Scenario posScenario : this.possibleScenarios.values()) {
            returnArray.add(posScenario.getId());
        }
        return returnArray;
    }

    /**
     * Sets the game scenario to the UUID passed in through the parameter.
     *
     * @param uuid the UUID corresponding to the scenario.
     */
    @Override
    public void setScenario(UUID uuid) {
        this.scenario = this.possibleScenarios.get(uuid);
    }

    /**
     * Calculates the current amount of time played in milliseconds (Unix time)
     * and returns it.
     *
     * @return a long that holds the milliseconds played since the start of the
     * game
     */
    @Override
    public long getPlayedMillis() {
        Calendar playedTime = new GregorianCalendar();
        playedTime.setTimeInMillis(System.currentTimeMillis() - this.startTime.getTimeInMillis());

        return (System.currentTimeMillis() - this.startTime.getTimeInMillis());
    }

    /**
     * A method that gets called after picking name and scenario. It sets the
     * scenario and name and then calls the play method.
     *
     * @param scenario the UUID of the scenario
     * @param playerName the String that contains the player's name (can be
     * empty)
     */
    @Override
    public void startGame(UUID scenario, String playerName) {
        this.scenario = this.possibleScenarios.get(scenario);
        this.player = new Player(playerName, 600, 10);
        this.play();
    }

    /**
     * Figures out which planet / moon an item has to be delivered at and
     * formats it as a string.
     *
     * @param itemUuid the UUID of the item that is being checked
     * @return a String containing the name of the location an item has to be
     * delivered at
     */
    @Override
    public String getDeliveryPlanet(UUID itemUuid) {
        Item item = this.items.get(itemUuid);
        NPC deliveryNpc = this.npcs.get(item.getNpcId());
        UUID deliveryNpcHolderUuid = deliveryNpc.getPlanetId();
        if (this.planets.containsKey(deliveryNpcHolderUuid)) {
            return "Location: " + this.planets.get(deliveryNpcHolderUuid).getName();
        } else {
            return "Location: moon of " + this.planets.get(this.moons.get(deliveryNpcHolderUuid).getParentPlanetUuid()).getName();
        }
    }

    /**
     * Figures out which NPC is the receiver and then formats it as a string.
     *
     * @param itemUuid the UUID of the item being delivered
     * @return a String containing whom to deliver the item to
     */
    @Override
    public String getDeliveryNpc(UUID itemUuid) {
        return "Deliver to: " + this.npcs.get(this.items.get(itemUuid).getNpcId()).getName();
    }

    /**
     * Figures out whether there is war on a planet/moon or not
     *
     * @param uuid of the planet or moon
     * @return a boolean of whether there is war or not
     */
    @Override
    public boolean isWar(UUID uuid) {
        if (this.getNPCHolderFromUuid(uuid).getWarTimer() > this.time) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns an ArrayList of formatted strings from the HighScore objects.
     *
     * @return an ArrayList of Strings
     */
    @Override
    public ArrayList<String> quitGame() {
        this.saveHighScore();
        this.audioPlayer.playThanks();
        ArrayList<String> returnArray = new ArrayList();
        for (HighScore hs : this.highScores) {
            returnArray.add(hs.toString());
        }
        return returnArray;
    }
    
    /**
     * A getter method for whether the player is dead.
     * @return a boolean, true if dead
     */
    @Override
    public boolean isDead() {
        return this.isDead;
    }

    @Override
    public int getItemDeliveryTime(UUID itemUuid) {
        return this.items.get(itemUuid).getDeliveryTime();
    }

    @Override
    public boolean getItemPapers(UUID itemUuid) {
        return this.items.get(itemUuid).getPapers();
    }
    
    @Override
    public UUID getStartNpc() {
        return this.startNpc;
    }
}

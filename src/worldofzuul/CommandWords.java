package worldofzuul;

import java.util.HashMap;

/**
 * This class is used to hold the handles to each CommandWord object (the enum)
 *
 */
public class CommandWords {

    private HashMap<String, CommandWord> validCommands; //Contains the commands known to the system, gotten by the enum class

    /**
     * Constructor, it creates a HashMap, as defined above, and then it loops
     * through each valid CommandWord (the enum to contain all of the recognized
     * cmds) It puts the recognized commands into the HashMap The for each loop
     * is used to put each command's object into the HashMap, so that the handle
     * to object can always be retrieved
     */
    public CommandWords() {
        validCommands = new HashMap<String, CommandWord>(); //Creates the HashMap
        for (CommandWord command : CommandWord.values()) { //Foreach loop, CommandWord.values() returns a collection (probably just an array) that it loops through
            if (command != CommandWord.UNKNOWN) { //If it is not equal to unknown, it means it is known
                validCommands.put(command.toString(), command); //Puts the command into the HashMap, using the CommandWord's string and the object itself (the string is defined in the enum)
            }
        }
    }

    /**
     * A method to get a command from a string, it basically uses the parameter
     * string to fetch something in the HashMap and then returns it
     *
     * @param commandWord a string, that is to be "converted" into a commandword
     * @return a CommandWord object, if the parameter is not fund in the hashmap
     * it returns an "unknown" CommandWord
     */
    public CommandWord getCommandWord(String commandWord) {
        CommandWord command = validCommands.get(commandWord); //Fetches the handle (from the HashMap) to the object 
        //that corresponds the first command word
        if (command != null) { //If the command is valid,
            return command; //It returns the object from the HashMap
        } else { //If it is not a valid command
            return CommandWord.UNKNOWN; //Return the unknown enum
        }
    }

    /**
     * A function to check whether a string is recognized by the system or not
     * (recognized commands are saved in the HashMap validCommands)
     *
     * @param aString the string to check
     * @return true if the string is recognized as a command
     */
    public boolean isCommand(String aString) {
        //A HashMap method to check whether the parameter is a know key in the hashmap
        return validCommands.containsKey(aString);
    }

    /**
     * Prints all the keys in the HashMap, which means all of the commands
     * recognized
     */
    public void showAll() {
        //Note: the "key" in the HashMap is the index of the HashMap, like the number inside [] in an array
        for (String command : validCommands.keySet()) { //For each row in the HashMap, save the "key"
            System.out.print(command + "  "); //print out the index, note: the command is merely a String
        }
        System.out.println(); //Print a new line at the end
    }
}

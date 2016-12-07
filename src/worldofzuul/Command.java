/**
 * This class is part of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two parts: a CommandWord and a string
 * (for example, if the command was "take map", then the two parts
 * are TAKE and "map").
 *
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the CommandWord is UNKNOWN.
 *
 * If the command had only one word, then the second word is <null>.
 *
 * @author Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */
package worldofzuul;

/**
 * Holds information regarding the command typed in by the user, the information
 * is in two parts, the command word of the type CommandWord (an enum) and a
 * second word of the type String.
 *
 *
 */
public class Command {

    //Defiens variables
    private CommandWord commandWord;
    private String secondWord;

    /**
     * The constructor for the class, it simply stores the two parameters
     * parsend in
     *
     * @param commandWord of the type CommandWord, which contains the first word
     * of the command typed in by the user
     * @param secondWord of the type String, contains the second word
     */
    public Command(CommandWord commandWord, String secondWord) {
        this.commandWord = commandWord;
        this.secondWord = secondWord;
    }

    /**
     * A simple getter method, used to retrieve the command word (first word of
     * the command)
     *
     * @return
     */
    public CommandWord getCommandWord() {
        return commandWord;
    }

    /**
     * A simple getter method, used to retrieve the second word
     *
     * @return a String, that contains the second word
     */
    public String getSecondWord() {
        return secondWord; //Return the second word of the objec
    }

    /**
     * Checks whether the command word (first word) is known
     *
     * @return true if the command is not recognized
     */
    public boolean isUnknown() {
        return (commandWord == CommandWord.UNKNOWN); //This is a short "if" statement, see what is inside the (...) as if(...)
    }

    /**
     * Checks whether the command has a second word
     *
     * @return true if the second word is not null, and therefore exists
     */
    public boolean hasSecondWord() {
        return (secondWord != null);
    }
}

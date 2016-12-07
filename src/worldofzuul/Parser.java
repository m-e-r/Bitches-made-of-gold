package worldofzuul;

import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Used to take input from the user, and create a command using the usertyped
 * words
 *
 */
public class Parser {

    private CommandWords commands; //Defines variable
    private Scanner reader; //Defines variable

    /**
     * Constructor, creates an object of the type CommandWords (which is
     * basically a HashMap used to contain all of the valid enums) Creates an
     * object of the type Scanner, which takes input from the keyboard
     */
    public Parser() {
        commands = new CommandWords(); //New object of type commandWords 
        reader = new Scanner(System.in); //New scanner object, takes input from keyboard
    }

    /**
     * Method that gets the command from the user input, it uses the scanner to
     * take what the user types in
     *
     * @return a new object of the type Command, in the process, it gets an
     * object handle from the class CommandWords
     */
    public Command getCommand() {
        //***Defines variables***
        String inputLine;
        String word1 = null;
        String word2 = null;
        //***********************

        System.out.print("> ");

        inputLine = reader.nextLine(); //Saves user typed line

        Scanner tokenizer = new Scanner(inputLine);     //Creates new object of type Scanner using inputLine and
        //defines variable with return from Scanner object
        if (tokenizer.hasNext()) { //Checks if inputLine have another word
            word1 = tokenizer.next(); //If true, assigns variable with return, holds 1. word
            if (tokenizer.hasNext()) { //Checks if inputLine have yet another word
                word2 = tokenizer.next(); //If true, assigns variable with return, holds 2. word
            }
        }

        //Instanciates a new object of the type Command (check Javadoc for Command!)
        //It saves the object (holding the cmd) returned from .getCommandWord and the second word, which is a string
        return new Command(commands.getCommandWord(word1), word2);
    }

    /**
     * Prints all of the commands, using a method in CommandWords
     */
    public void showCommands() {
        commands.showAll(); //A method to print out all the command words
    }
}

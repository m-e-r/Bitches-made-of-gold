/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul;

/**
 * A class to hold information regarding highscores.
 */
public class HighScore {

    //Attributes
    private String name;   // the name from the player
    private int rep;   //the reputation from this current game, when the game is over
    private int time; // the time taken to for the player to finish the game

    /**
     * Empty constructor for JSON reading.
     */
    public HighScore() {

    }

    /**
     * "Normal" constructor.
     *
     * @param rep the reputation amount
     * @param time the time done in the game
     * @param name the name of the highscore achiever
     */
    public HighScore(int rep, int time, String name) {
        this.name = name;
        this.rep = rep;
        this.time = time;
    }

    /**
     * Returns a string reputation of the informationer in the highscore.
     *
     * @return a string with the highscore information
     */
    @Override
    public String toString() {
        String stringTime = "" + this.time;
        String stringRep = "" + this.rep;
        String theHighScore = this.name + ":" + stringRep + ":" + stringTime;
        return theHighScore;
    }

    /**
     * Creates a string that can be saved in a JSON file, and with syntax that
     * can be read.
     *
     * @return a string
     */
    public String toJsonString() {
        String jSonString = "{\"name\":\"" + this.name + "\",\"rep\":" + this.rep + ",\"time\":" + this.time + "}";
        return jSonString;
    }

    // ***** GETTERS *****
    public int getRep() {
        return this.rep;
    }

    public int getTime() {
        return this.time;
    }

    public String getName() {
        return this.name;
    }
    // ***** GETTERS END *****
}

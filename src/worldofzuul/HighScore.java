package worldofzuul;

/**
 * A class to hold information regarding highscores.
 */
public class HighScore implements Comparable<HighScore> {

    //Attributes
    private String name;   // the name from the player
    private int rep;   // the reputation from this current game
    private int time; // the time used

    /**
     * Empty constructor for JSON reading.
     */
    public HighScore() {

    }

    /**
     * Constructor.
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
     * Returns a string of the information stored in this object.
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
     * Returns a string that can be saved in a JSON file, and with syntax that
     * can be read by JSON.
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

    // ***** SETTERS *****
    public void setRep(int rep) {
        this.rep = rep;
    }

    public void setTime(int time) {
        this.time = time;
    }
    // ***** SETTERS END *****

    /**
     * Used for comparing two highscores. Implemented by the interface called
     * Comparable.
     *
     * @param h the HighScore object to compare to
     * @return an integer that describes whether the compared HighScore should
     * be above or below the current object
     */
    @Override
    public int compareTo(HighScore h) {
        //First compare the reputation
        if (this.rep > h.getRep()) {
            return -1;
        } else if (this.rep < h.getRep()) {
            return 1;
        } else { //If the reputation is equal to eachother, compare the time instead!
            if (this.time > h.getTime()) {
                return -1;
            } else if (this.time < h.getTime()) {
                return 1;
            }
            return 0;
        }
    }
}

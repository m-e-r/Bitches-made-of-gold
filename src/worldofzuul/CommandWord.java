package worldofzuul;

/**
 * An enum class. It contains the commands recognized by the system
 */
public enum CommandWord {
    /**
     * The commands recognized by the system
     */
    GO("go"), QUIT("quit"), HELP("help"), DROP("drop"), PRINT("print"), SCAN("scan"), UNKNOWN("?"), SAY("say"), GREET("greet"), WARP("warp"), NAME("name"), SCENARIO("scenario");

    private String commandString;

    /**
     * Constructor for the class, it saves the commandString
     *
     * @param commandString saves the string typed in by the user
     */
    CommandWord(String commandString) {
        this.commandString = commandString;
    }

    /**
     * Makes each object of the enum printable, it prints the line typed in by
     * the user
     *
     * @return a String, that is the whole string typed in by the user
     * @Overrides the method inherited
     */
    @Override
    public String toString() {
        return commandString;
    }
}

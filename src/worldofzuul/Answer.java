package worldofzuul;

/**
 * Holds information regarding each answer. This is associated with the Question
 * class, and both are a part of the Conversation "system".
 */
public class Answer {

    //Attributes
    private int nextLineNumber;
    private String referenceWord;
    private String reactText;
    private String executionLine;

    /**
     * Constructor
     *
     * @param nextLineNumber the next question to proceed to, if this answer is
     * answered
     * @param referenceWord what word are used by the user to "answer" this
     * answer
     * @param reactText what the NPC replies
     * @param executionLine what methods / affects this answer has, if the
     * answer is answered
     */
    public Answer(int nextLineNumber, String referenceWord, String reactText, String executionLine) {
        this.referenceWord = referenceWord;
        this.executionLine = executionLine;
        this.nextLineNumber = nextLineNumber;
        this.reactText = reactText;
    }

    // ***** GETTERS *****
    public String getReferenceWord() {
        return this.referenceWord;
    }

    public String getExecutionLine() {
        return this.executionLine;
    }

    public String getReactText() {
        return this.reactText;
    }

    public int getNextLineNumber() {
        return this.nextLineNumber;
    }
    // ***** GETTERS END *****
}

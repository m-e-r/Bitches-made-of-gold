package worldofzuul;

import java.util.ArrayList;

/**
 * Used to hold information for each question in a conversation. It holds
 * information about it self (the text that should be printed), and the possible
 * answers that the user can answer to this player.
 */
public class Question {

    //Attributter
    private String qText;
    private int numOfAns;
    private ArrayList<Answer> answers;
    private Answer currentAnswer;

    /**
     * Constructor
     *
     * @param qText the text that should be printed when the played "gets" to
     * this question
     * @param numOfAns the amount of possible answers this question has
     */
    public Question(String qText, int numOfAns) {
        this.qText = qText;
        this.numOfAns = numOfAns;
        this.answers = new ArrayList<>();
        this.currentAnswer = null;
    }

    /**
     * Finds the answer based on the answer reference that the player has typed
     * in.
     *
     * @param answerRef the string of what the player has typed in
     */
    public void findAnswer(String answerRef) { //Method for finding an answer
        for (Answer answer : this.answers) {
            if (answerRef.equals(answer.getReferenceWord())) {
                this.currentAnswer = answer;
                break;
            } else {
                this.currentAnswer = null;
            }
        }
    }

    /**
     * Gets a string with the possible answers.
     *
     * @return a string that contains the possible answers formatted correctly
     */
    public String[] getPossibleAnswers() {
        String[] returnStrings = new String[this.answers.size()];
        for(int i = 0; i < this.answers.size(); i++) {
            returnStrings[i] = this.answers.get(i).getReferenceWord();
        }
        /*
        String returnString = "";
        for (Answer answer : this.answers) {
            returnString += answer.getReferenceWord() + ", ";
        }
        */
        return returnStrings;
    }

    // ***** GETTERS *****
    public String getQText() {
        return this.qText;
    }

    public int getNumOfAns() {
        return this.numOfAns;
    }

    public boolean hasCurrentAnswer() {
        return this.currentAnswer != null;
    }

    public String getExecutionLine() {
        return this.currentAnswer.getExecutionLine();
    }

    public String getReactText() {
        return this.currentAnswer.getReactText();
    }

    public int getNextLineNumber() {
        return this.currentAnswer.getNextLineNumber();
    }
    // ***** GETTERS END *****

    /**
     * Adds a answer with the parameters. See Answers class for description of
     * these parameters.
     *
     * @param nextLineNumber
     * @param referenceWord
     * @param reactText
     * @param exeLine
     */
    public void addAnswer(int nextLineNumber, String referenceWord, String reactText, String exeLine) {
        this.answers.add(new Answer(nextLineNumber, referenceWord, reactText, exeLine));
    }
}

package worldofzuul;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A new object of conversation gets created everytime a player meets an NPC
 * This handles all of the question and answers the question has To create the
 * conversation, you need to read the equivalent JSON file The creation of a
 * conversation is done in the method "createWholeConversation" and therefore
 * not in the constructor
 */
public class Conversation {

    private int currentQuestionNumber;
    private int conversationId;
    private UUID npcId;
    private Question currentQuestion;
    private ArrayList<Question> questionList;

    public Conversation(int conversationId) {
        this.conversationId = conversationId;
        this.currentQuestionNumber = 0;
        this.questionList = new ArrayList<>();
    }

    /**
     * Creates the whole conversation based on a list of strings coming from a
     * file This is soon to be changed, when JSON starts working
     *
     * @param text the list fetched from a file
     */
    public void createWholeConversation(List<String> text) {
        for (int i = 1; i <= text.size();) {
            String qText = text.get(i);
            int numOfAns = Character.getNumericValue(text.get(i + 1).charAt(0));
            this.questionList.add(new Question(qText, numOfAns));

            int count = 1;
            int n = i;
            while (count <= numOfAns) {
                String ansText = text.get(n + 3);
                String reactText = text.get(n + 4);
                int nextLineNumber = Character.getNumericValue(text.get(n + 5).charAt(0));
                String exeLine = text.get(n + 6);

                this.questionList.get(this.questionList.size() - 1).addAnswer(nextLineNumber, ansText, reactText, exeLine);

                count++;
                n += 5;
            }
            i += 3 + 5 * numOfAns;
        }
        this.currentQuestion = this.questionList.get(this.currentQuestionNumber);

    }

    /**
     * Gets the current question of the conversation to try and find an answer
     * from the parameter
     *
     * @param userAns a string, which is the second word the use typed in along
     * with the command "say"
     */
    public void processAnswer(String userAns) {
        this.currentQuestion.findAnswer(userAns);
    }

    /**
     * Gets all of the possible answers as a probably formatted string
     *
     * @return a string
     */
    public String[] getPossibleAnswers() {
        return this.currentQuestion.getPossibleAnswers();
    }

    /**
     * Sets the next answer to be equal to the parameter
     *
     * @param questionNumber the number of the question
     */
    public void setNextQuestion(int questionNumber) {
        this.currentQuestionNumber = questionNumber;
        this.currentQuestion = this.questionList.get(this.currentQuestionNumber);
    }

    // ***** GETTERS *****
    public int getConversationId() {
        return this.conversationId;
    }

    public UUID getNpcId() {
        return this.npcId;
    }

    public String getQText() {
        return this.currentQuestion.getQText();
    }
    // ***** GETTERS END *****

    // ***** SETTERS *****
    public void setNpcId(UUID npcId) {
        this.npcId = npcId;
    }
    // ***** SETTERS END *****

    // ***** GETTERS FROM ANSWER *****
    public boolean hasCurrentAnswer() {
        return this.currentQuestion.hasCurrentAnswer();
    }

    public String getExecutionLine() {
        return this.currentQuestion.getExecutionLine();
    }

    public String getReactText() {
        return this.currentQuestion.getReactText();
    }

    public int getNextLineNumber() {
        return this.currentQuestion.getNextLineNumber();
    }
    // ***** GETTERS FROM ANSWER END *****
}

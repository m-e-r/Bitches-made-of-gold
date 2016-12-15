package worldofzuul;

/**
 * A class used for storing the lines that Game wants to have printed.
 */
public class Dashboard {

    private String savedString = "";

    /**
     * A method used to print something.
     *
     * @param toPrint what to print
     */
    public void print(String toPrint) {
        String returnString = "";
        String[] splittedString = toPrint.split(" ");
        boolean firstCut = false;
        int lastCut = 0;
        while(lastCut < splittedString.length) {
            if(!firstCut) {
                firstCut = true;
            } else {
                returnString += "\t";
            }

            int cuttedSize = 0;
            while(lastCut < splittedString.length) {
                cuttedSize += splittedString[lastCut].length();
                if(cuttedSize > 75) {
                    returnString += "\n";
                    break;
                }
                returnString += splittedString[lastCut] + " ";
                lastCut++;
            }
        }
        this.savedString += returnString + "\n";
    }

    /**
     * An overloaded method, that allows Game to print a newline without writing
     * anything in the parameter list.
     */
    public void print() {
        this.savedString += "\n";
    }

    /**
     * Returns the saved string and clears it.
     *
     * @return
     */
    public String getSavedString() {
        String tempString = this.savedString;
        this.savedString = "";
        return tempString;
    }
}

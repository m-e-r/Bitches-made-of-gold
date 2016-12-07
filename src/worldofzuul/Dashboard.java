/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul;

/**
 * A method used as the place where this whole system communicates with the
 * user.
 */
public class Dashboard {

    private String savedString = "";
    
    /**
     * A method used to print something.
     *
     * @param toPrint what to print
     */
    public void print(String toPrint) {
        this.savedString += toPrint + "\n";
        System.out.print(this.getSavedString());
    }

    /**
     * An overloaded method, that allows Game to print a newline without writing
     * anything in the parameter list.
     */
    public void print() {
        this.savedString += "\n";
        System.out.print(this.getSavedString());
    }

    public String getSavedString() {
        String tempString = this.savedString;
        this.savedString = "";
        return tempString;
    }
}

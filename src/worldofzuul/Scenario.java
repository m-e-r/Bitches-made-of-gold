/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul;

/**
 *
 * @author DanielToft
 */
class Scenario implements PrintAble {

    private String name, description, path;

    public Scenario(String name, String description, String path) {
        this.name = name;
        this.description = description;
        this.path = path;
    }

    // ***** GETTERS *****
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getPath() {
        return this.path;
    }
    // ***** GETTERS END *****
}

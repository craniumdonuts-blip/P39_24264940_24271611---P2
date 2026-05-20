package project2.model;

import project2.model.Inventory;

/**
 *
 * @author Angie
 */
public class Player {

    private String name;
    private TraitType trait;
    private int totalPoints;
    private Inventory inventory;

    // constructor 
    public Player() {
        this.name = "";
        this.totalPoints = 0;
        this.inventory = new Inventory();
    }

    // getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TraitType getTrait() {
        return trait;
    }

    public void setTrait(TraitType trait) {
        this.trait = trait;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    // be able to plus/minus total points
    public void changeTotalPoints(int points) {
        totalPoints += points;
    }

    public Inventory getInventory() {
        return inventory;
    }

}

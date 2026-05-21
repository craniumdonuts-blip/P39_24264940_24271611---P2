package project2.model;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author angie
 */
public class Choice {

    private int pointValue;
    private String choiceDesc;
    private Item requiredItem;
    private TraitType requiredTrait;
    private String nextSceneID;
    private List<Item> givenItems;
    private int number;
    private String transitionText;
    private boolean removesItem;
    private boolean requiresEmptyInventory;
    private Scene scene;

    public Choice(
            int number,
            String choiceDesc,
            String nextSceneId,
            int pointValue,
            Item requiredItem,
            List<Item> givenItems) {
        this.number = number;
        this.choiceDesc = choiceDesc;
        this.nextSceneID = nextSceneId;
        this.pointValue = pointValue;
        this.requiredItem = requiredItem;
        this.givenItems = givenItems;
    }

    // getters and setters
    public String getNextSceneId() {
        return nextSceneID;
    }

    public String getChoiceDesc() {
        return choiceDesc;
    }

    public void setChoiceDesc(String choiceDesc) {
        this.choiceDesc = choiceDesc;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setPointValue(int pointValue) {
        this.pointValue = pointValue;
    }

    public String getTransitionText() {
        return transitionText;
    }

    public void setTransitionText(String transitionText) {
        this.transitionText = transitionText;
    }

    public List<Item> getGivenItems() {
        return givenItems;
    }

    public void setRemovesItem(boolean removesItem) {
        this.removesItem = removesItem;
    }

    public void setRequiredTrait(TraitType trait) {
        this.requiredTrait = trait;
    }

    public void setRequiresEmptyInventory(boolean val) {
        this.requiresEmptyInventory = val;
    }

    // get player input
    public static int getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n> ");

        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return getInput(); //retry
        }
    }

    // choices available depending on player trait and item
    public boolean isAvailable(Player player) {
        if (player == null) {
            return false;
        }

        // Check required trait
        if (this.requiredTrait != null && player.getTrait() != this.requiredTrait) {
            return false;
        }

        // Check requires empty inventory (true means inventory must have zero items)
        if (this.requiresEmptyInventory) {
            Inventory inv = player.getInventory();
            if (inv == null || !inv.isEmpty()) {
                return false;
            }
        }

        // Check required item (if any) by name using Inventory.hasItem(String)
        if (this.requiredItem != null) {
            Inventory inv = player.getInventory();
            if (inv == null || !inv.hasItem(this.requiredItem.getName())) {
                return false;
            }
        }

        // All checks passed
        return true;
    }

    // plus/minus points from player total points depending on the point value of choice
    public void applyPointEffect(Player player) {
        player.changeTotalPoints(pointValue);
    }

    // check if choice needs to remove item
    public boolean isRemovesItem() {
        return removesItem;
    }

}

package project2.controller;

import project2.view.GameEventListener;
import java.util.ArrayList;
import project2.model.*;
import java.util.List;

/**
 *
 * @author ella
 */
public class GameController {

    private Game game;
    private SaveDAO saveDAO;

    // add gameeventlistener
    private List<GameEventListener> listeners = new ArrayList<>();

    public void addGameEventListener(GameEventListener l) {
        listeners.add(l);
    }

    // go through list of listeners on game events
    private void fireSceneChanged(Scene scene) {
        for (GameEventListener l : listeners) {
            l.onSceneChanged(scene);
        }
    }

    private void fireGameOver() {
        for (GameEventListener l : listeners) {
            l.onGameOver();
        }
    }

    // New game controller 
    public GameController() {
        this.game = new Game();
        this.saveDAO = new SaveDAO();
    }

    // Start new game
    public void startNewGame(String name, TraitType trait) {
        game.start(name, trait);
    }

    // Load saved data from file
    public void loadGame(int slot) {
        Player loaded = saveDAO.load(slot);

        if (loaded != null) {
            game.start(loaded, saveDAO.getLastLoadedSceneId());
        }
    }

    // delete saved data from file 
    public void deleteSave(int slot) {
        saveDAO.delete(slot);
    }

    // Write save
    public void saveGame(int slot) {
        saveDAO.save(game.getPlayer(), game.getCurrentScene().getSceneId(), slot);
    }

    // Check if save slot already exists
    public boolean slotExists(int slot) {
        return saveDAO.slotExists(slot);
    }

    // Returns filled save slots for GUI to display
    public List<Integer> listSaves() {
        return saveDAO.listSaves();
    }

    // For GUI buttons
    public List<Choice> getAvailableChoices() {
        return game.getCurrentScene().getAvailableChoices(game.getPlayer());
    }

    // Lets GUI read player info
    public Player getPlayer() {
        return game.getPlayer();
    }

    // GUI display text
    public Scene getCurrentScene() {
        return game.getCurrentScene();
    }

    // Check if game is over
    public boolean isGameOver() {
        return game.getCurrentScene() instanceof Ending;
    }

    // GUI get transition text for the choice
    public String processChoice(int num) {
        String transition = game.processChoice(num);
        if (isGameOver()) {
            fireGameOver();
        } else {
            fireSceneChanged(game.getCurrentScene());
        }
        return transition;
    }

    // Get end scene text
    public String getEndingText() {
        game.checkEnding();
        return game.getCurrentScene().getDisplayText(game.getPlayer().getTrait());
    }

    // Get current scene text
    public String getCurrentSceneText() {
        return game.getCurrentScene().getDisplayText(game.getPlayer().getTrait());
    }

    // returns NPC name for current scene, or null if no NPC
    public String getCurrentNpcName() {
        return game.getCurrentScene().getNpcName();
    }

    // returns player inventory items for inventory overlay in GUI
    public List<Item> getInventoryItems() {
        return game.getPlayer().getInventory().getItems();
    }

    // get transition text for a choice without advancing the scene
    public String getTransitionText(int choiceNum) {
        Choice choice = game.getCurrentScene().getChoice(choiceNum, game.getPlayer());
        if (choice == null) {
            return "";
        }
        String text = choice.getTransitionText();
        return text != null ? text : "";
    }

    // get the transition image key (e.g. s1c1) for mapping backgrounds
    public String getTransitionImageKey(int choiceNum) {
        Choice choice = game.getCurrentScene().getChoice(choiceNum, game.getPlayer());
        if (choice == null) {
            return null;
        }
        return game.getCurrentScene().getSceneId() + "c" + choiceNum;
    }

    // check ending
    public void checkEnding() {
        game.checkEnding();
        fireGameOver();
    }
}

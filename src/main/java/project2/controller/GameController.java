package project2.controller;

import project2.model.*;
import java.util.List;

/**
 *
 * @author ella
 */
public class GameController {

    private Game game;
    private SaveManager saveManager;

    // New game controller 
    public GameController() {
        this.game = new Game();
        this.saveManager = new SaveManager("saves");
    }

    // Start new game
    public void startNewGame(String name, TraitType trait) {
        game.start(name, trait);
    }

    // Load saved data from file
    public void loadGame(int slot) {
        Player loaded = saveManager.load(slot);

        if (loaded != null) {
            game.start(loaded, saveManager.getLastLoadedSceneId());
        }
    }

    // Write save
    public void saveGame(int slot) {
        saveManager.save(game.getPlayer(), game.getCurrentScene().getSceneId(), slot);
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
        return game.getCurrentScene().isEndScene();
    }

    // GUI get transition text for the choice
    public String processChoice(int num) {
        return game.processChoice(num);
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

}

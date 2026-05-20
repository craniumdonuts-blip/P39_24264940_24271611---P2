package Project2;

import project2.controller.Game;
import project2.controller.SaveManager;
import project2.model.Player;
import project2.model.Choice;

/**
 *
 * @author ella
 */
public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        SaveManager saveManager = new SaveManager("saves");

        System.out.println("=== Greetings, traveller! ===");
        System.out.println("  1. New game");
        System.out.println("  2. Load game");
        System.out.println("  3. Delete save");

        int choice = Choice.getInput();

        switch (choice) {
            case 1: {
                game.start();
                break;
            }
            case 2: {
                saveManager.listSaves();
                System.out.print("Load from slot (1-5): ");
                int slot = Choice.getInput();
                Player loaded = saveManager.load(slot);
                if (loaded != null) {
                    game.start(loaded, saveManager.getLastLoadedSceneId());
                } else {
                    System.out.println("No save found, starting new game");
                    game.start();
                }
                break;
            }
            case 3: {
                saveManager.listSaves();
                System.out.print("Delete slot (1-5): ");
                int slot = Choice.getInput();
                saveManager.deleteFile(slot);
                game.start();
                break;
            }
            default:
                System.out.println("ERROR! Starting new game");
                game.start();
        }
    }
}

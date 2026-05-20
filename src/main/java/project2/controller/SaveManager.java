package project2.controller;

import project2.model.TraitType;
import project2.model.Player;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ella
 */
public class SaveManager {

    private static final int MAX_SLOTS = 5;
    private String saveDir;
    private String lastLoadedSceneId;

    public SaveManager(String saveDirectory) {
        this.saveDir = saveDirectory;
        this.lastLoadedSceneId = null;

        // Create the save directory if it doesn't already exist
        File dir = new File(saveDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void save(Player player, String sceneId, int slot) {
        if (!isValidSlot(slot)) {
            System.out.println("Invalid slot — must be between 1 and " + MAX_SLOTS + ".");
            return;
        }

        File file = getFile(slot);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("name:" + player.getName());
            writer.newLine();
            writer.write("trait:" + player.getTrait().name());
            writer.newLine();
            writer.write("points:" + player.getTotalPoints());
            writer.newLine();
            writer.write("sceneId:" + sceneId);
            writer.newLine();
            writer.write("inventory:" + player.getInventory().toSaveString());
            writer.newLine();
            System.out.println("Game saved to slot " + slot + ".");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    public Player load(int slot) {
        if (!isValidSlot(slot)) {
            System.out.println("Invalid slot — must be between 1 and " + MAX_SLOTS + ".");
            return null;
        }

        if (!slotExists(slot)) {
            System.out.println("No save found in slot " + slot + ".");
            return null;
        }

        File file = getFile(slot);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String name = readValue(reader.readLine());
            String traitStr = readValue(reader.readLine());
            int points = Integer.parseInt(readValue(reader.readLine()));
            String sceneId = readValue(reader.readLine());
            String invString = readValue(reader.readLine());

            // Restore player
            Player player = new Player();
            player.setName(name);
            player.setTrait(TraitType.valueOf(traitStr));
            player.changeTotalPoints(points);
            player.getInventory().fromSaveString(invString);

            // Store sceneId for Game to get
            this.lastLoadedSceneId = sceneId;

            System.out.println("Game loaded from slot " + slot + ".");
            return player;

        } catch (IOException | NumberFormatException e) {
            System.out.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    public void deleteFile(int slot) {
        if (!isValidSlot(slot)) {
            System.out.println("Invalid slot — must be between 1 and " + MAX_SLOTS + ".");
            return;
        }

        File file = getFile(slot);
        if (file.exists()) {
            file.delete();
            System.out.println("Save slot " + slot + " deleted.");
        } else {
            System.out.println("No save found in slot " + slot + ".");
        }
    }

    public void listSaves() {
        System.out.println("\n--- Save Slots ---");
        for (int i = 1; i <= MAX_SLOTS; i++) {
            if (slotExists(i)) {
                System.out.println("  Slot " + i + ": [SAVED]");
            } else {
                System.out.println("  Slot " + i + ": [EMPTY]");
            }
        }
    }

    public boolean slotExists(int slot) {
        return getFile(slot).exists();
    }

    private File getFile(int slot) {
        return new File(saveDir + "/save_slot_" + slot + ".txt");
    }

    private boolean isValidSlot(int slot) {
        return slot >= 1 && slot <= MAX_SLOTS;
    }

    private String readValue(String line) {
        if (line == null) {
            return "";
        }
        int colonIndex = line.indexOf(":");
        return colonIndex >= 0 ? line.substring(colonIndex + 1) : "";
    }

    public String getLastLoadedSceneId() {
        return lastLoadedSceneId;
    }
}

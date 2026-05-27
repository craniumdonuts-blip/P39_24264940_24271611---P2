package project2.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ella
 */
public abstract class Scene implements Displayable {

    private String sceneId;
    private String sceneDesc;
    // Trait specific scenes
    private Map<TraitType, String> varDialogue;
    private List<Choice> choices;
    private Npc npc;
    private boolean isEndScene;

    public Scene(String sceneId, String sceneDesc, boolean isEndScene) {
        this.sceneId = sceneId;
        this.sceneDesc = sceneDesc;
        this.isEndScene = isEndScene;
        this.choices = new ArrayList<>();
        this.varDialogue = new HashMap<>();
        this.npc = null;
    }

    // Different options for different traits
    public void display(TraitType trait) {

        // Print NPC dialogue if this scene has an NPC
        if (npc != null) {
            System.out.println("\n" + npc.getName() + " says:");
            System.out.println("  \"" + npc.getSpeak(trait) + "\"");
        }
        // Use trait variant if one exists
        if (varDialogue.containsKey(trait)) {
            System.out.println(varDialogue.get(trait));
        } else {
            System.out.println(sceneDesc);
        }

    }

    // Return choice
    public Choice getChoice(int number) {
        for (Choice c : choices) {
            if (c.getNumber() == number) {
                return c;
            }
        }
        return null;
    }

    // Return the choice matching number that is available for the given player.
    public Choice getChoice(int number, Player player) {
        for (Choice c : choices) {
            if (c.getNumber() == number && c.isAvailable(player)) {
                return c;
            }
        }
        return null;
    }

    // Return avaliable choices
    public List<Choice> getAvailableChoices(Player player) {
        List<Choice> available = new ArrayList<>();
        java.util.Set<Integer> seenNumbers = new java.util.HashSet<>();

        for (Choice c : choices) {
            int num = c.getNumber();
            // If we've already added a choice for this number, skip further variants
            if (seenNumbers.contains(num)) {
                continue;
            }
            // Add the first available variant for this number and mark the number as handled
            if (c.isAvailable(player)) {
                available.add(c);
                seenNumbers.add(num);
            }
            // If not available
        }
        return available;
    }
    
    // Get scene and npc text to display in GUI
    public String getDisplayText(TraitType trait) {
        
        StringBuilder text = new StringBuilder();
        
        if (npc != null) {
            text.append(npc.getName()).append(" says:\n");
            text.append(" \"").append(npc.getSpeak(trait)).append("\"\n");
        }
        
        if (varDialogue.containsKey(trait)) {
            text.append(varDialogue.get(trait));
        } else {
            text.append(sceneDesc);
        }
        return text.toString();
    }

    // Add choice
    public void addChoice(Choice choice) {
        choices.add(choice);
    }

    // Set NPC
    public void setNpc(Npc npc) {
        this.npc = npc;
    }

    // Adds trait description
    public void addVarDialogue(TraitType trait, String description) {
        varDialogue.put(trait, description);
    }

    // Getters
    public String getSceneId() {
        return sceneId;
    }

    public boolean isEndScene() {
        return isEndScene;
    }

}

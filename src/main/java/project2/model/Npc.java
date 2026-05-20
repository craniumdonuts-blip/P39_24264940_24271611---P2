package project2.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ella
 */
public class Npc {

    private String name;
    // Default story
    private String defSpeak;
    // Trait specific story
    private Map<TraitType, String> varSpeak;

    public Npc(String name, String defSpeak) {
        this.name = name;
        this.defSpeak = defSpeak;
        this.varSpeak = new HashMap<>();
    }

    // Return dialogue 
    public String getSpeak(TraitType trait) {
        if (varSpeak.containsKey(trait)) {
            return varSpeak.get(trait);
        }
        return defSpeak;
    }

    // Add trait specific dialogue
    public void addVarDialogue(TraitType trait, String varDialogue) {
        varSpeak.put(trait, varDialogue);
    }

    public String getName() {
        return name;
    }
}

package project2;

/**
 *
 * @author ella
 */
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import project2.model.*;
import project2.controller.*;

public class GameTests {

    private Player player;

    @Before
    public void setUp() {
        player = new Player();
        player.setName("Test");
        player.setTrait(TraitType.BRAVE);
    }

    // Test 1: Player points update correctly
    @Test
    public void testPlayerPointsChange() {
        player.changeTotalPoints(10);
        assertEquals(10, player.getTotalPoints());
        player.changeTotalPoints(-5);
        assertEquals(5, player.getTotalPoints());
    }

    // Test 2: Player inventory adds, checks and removes items correctly
    @Test
    public void testInventoryAddCheckAndRemove() {
        player.getInventory().addItem(new Item("Crystal Dagger", "A small crystal dagger"));
        assertTrue(player.getInventory().hasItem("Crystal Dagger"));
        assertFalse(player.getInventory().hasItem("Shield"));
        player.getInventory().removeItem("Crystal Dagger");
        assertFalse(player.getInventory().hasItem("Crystal Dagger"));
    }

    // Test 3: Choice unavailable for wrong trait across all three traits
    @Test
    public void testChoiceUnavailableWrongTrait() {
        Choice choice = new Choice(1, "Test choice", "s2", 10, null, new java.util.ArrayList<>());
        choice.setRequiredTrait(TraitType.TIMID);

        // BRAVE player cannot access TIMID choice
        player.setTrait(TraitType.BRAVE);
        assertFalse(choice.isAvailable(player));

        // CUNNING player cannot access TIMID choice
        player.setTrait(TraitType.CUNNING);
        assertFalse(choice.isAvailable(player));

        // TIMID player can access TIMID choice
        player.setTrait(TraitType.TIMID);
        assertTrue(choice.isAvailable(player));
    }

    // Test 4: SaveDAO saves and loads player data and scene correctly
    @Test
    public void testSaveAndLoad() {
        SaveDAO saveDAO = new SaveDAO();
        player.changeTotalPoints(15);
        saveDAO.save(player, "s2", 1);
        Player loaded = saveDAO.load(1);
        assertEquals("Test", loaded.getName());
        assertEquals(TraitType.BRAVE, loaded.getTrait());
        assertEquals(15, loaded.getTotalPoints());
        assertEquals("s2", saveDAO.getLastLoadedSceneId());
        saveDAO.delete(1);
    }

    // Test 5: SaveDAO delete removes save correctly
    @Test
    public void testDeleteSave() {
        SaveDAO saveDAO = new SaveDAO();
        saveDAO.save(player, "s1", 2);
        assertTrue(saveDAO.slotExists(2));
        saveDAO.delete(2);
        assertFalse(saveDAO.slotExists(2));
    }
}

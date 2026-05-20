package project2.controller;

import project2.controller.SaveManager;
import project2.model.TraitType;
import project2.model.Scene;
import project2.model.Player;
import project2.model.Npc;
import project2.model.Item;
import project2.model.Inventory;
import project2.model.Ending;
import project2.model.Choice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author ella
 */
public class Game {
    // Calling other classes - check names & requirements 

    private Player player;
    private Scene currentScene;
    private final Map<String, Scene> scenes;
    private SaveManager saveManager;
    private Inventory inventory;

    // Game constructor
    public Game() {
        this.scenes = new HashMap<>();
        this.saveManager = new SaveManager("saves");
    }

    // Start game for new player
    public void start() {
        player = new Player();
        selectTrait();
        initScenes();
        loadScene("s1");
        run();
    }

    // Start game for loaded player (continue game)
    public void start(Player loadPlayer, String sceneId) {
        this.player = loadPlayer;
        initScenes();
        loadScene(sceneId);
        run();
    }

    // Gameplay loop
    private void run() {
        while (true) {
            currentScene.display(player.getTrait());

            // Stop if ending scene
            if (checkEnding()) {
                break;
            }

            List<Choice> available = currentScene.getAvailableChoices(player);
            for (Choice c : available) {
                System.out.println(" " + c.getNumber() + ". " + c.getChoiceDesc());
            }
            System.out.println(" 0. Save game");

            int chosen = Choice.getInput();

            // ask if player wants to save
            if (chosen == 0) {
                System.out.print("Save to slot (1-5): ");
                int slot = Choice.getInput();
                saveManager.save(player, currentScene.getSceneId(), slot);
                // loop continues - player picks again
            } else {
                processChoice(chosen);
            }
        }
    }

    // Load a scene
    public void loadScene(String sceneId) {
        Scene next = scenes.get(sceneId);
        if (next != null) {
            currentScene = next;
        } else {
            System.out.println("[ERROR] scene " + sceneId);
        }
    }

    // Processing choice
    public void processChoice(int num) {
        Choice choice = currentScene.getChoice(num, player);


        if (choice == null) {
            System.out.println("[ERROR] choice");
            return;
        }

        // Text after choice is made
        if (choice.getTransitionText() != null) {
            System.out.println("\n" + choice.getTransitionText());
        }

        // Update points
        player.changeTotalPoints(choice.getPointValue());

        // Add item (if in choice)
        for (Item item : choice.getGivenItems()) {
            player.getInventory().addItem(item);
            System.out.println("you got: " + item.getName());
        }

        // Remove random item if choice triggers it
        if (choice.isRemovesItem()) {
            player.getInventory().removeRandomItem();
        }

        // Next scene
        loadScene(choice.getNextSceneId());
    }

    public boolean checkEnding() {
        if (!currentScene.isEndScene()) {
            return false;
        }

        int points = player.getTotalPoints();

        if (points >= 20) {
            loadScene("good");
        } else if (points >= 0) {
            loadScene("neutral");
        } else {
            loadScene("bad");
        }

        // Ending extends scene
        // Display changes text
        Ending ending = (Ending) currentScene;
        ending.display(player.getTrait());
        return true;
    }

    // Trait selection
    private void selectTrait() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("enter your name: ");
        String name = scanner.nextLine().trim();
        player.setName(name);

        System.out.println("who are you, traveller?");
        System.out.println("  1. brave");
        System.out.println("  2. cunning");
        System.out.println("  3. timid");

        while (true) {
            System.out.print("\n> ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1 ->
                        player.setTrait(TraitType.BRAVE);
                    case 2 ->
                        player.setTrait(TraitType.CUNNING);
                    case 3 ->
                        player.setTrait(TraitType.TIMID);
                    default -> {
                        System.out.println("please enter 1, 2, or 3.");
                        continue;
                    }
                }
                System.out.println("you chose: " + player.getTrait());
                break;
            } catch (NumberFormatException e) {
                System.out.println("please enter 1, 2, or 3.");
            }
        }
    }

    // Add scenes
    public void addScene(Scene scene) {
        scenes.put(scene.getSceneId(), scene);
    }

    private void initScenes() {
        // Items
        List<Item> noItems = new ArrayList<>();

        List<Item> survivalGuide = new ArrayList<>();
        survivalGuide.add(new Item("Survival Guide", "A handy Survival Guide"));

        List<Item> loot = new ArrayList<>();
        loot.add(new Item("Coin Pouch", "Small pouch of coins"));
        loot.add(new Item("Bread", "A fresh loaf of bread"));
        loot.add(new Item("Crystal Dagger", "A small crystal dagger"));

        List<Item> mysteryBrew = new ArrayList<>();
        mysteryBrew.add(new Item("Mystery Brew", "A potion of something?"));

        List<Item> mysteryBrewAndHealth = new ArrayList<>();
        mysteryBrewAndHealth.add(new Item("Mystery Brew", "A potion of something?"));
        mysteryBrewAndHealth.add(new Item("Health Potion", "Restores health"));

        List<Item> roomVoucher = new ArrayList<>();
        roomVoucher.add(new Item("Room Voucher", "A voucher for one free stay at the Inn"));

        List<Item> cloak = new ArrayList<>();
        cloak.add(new Item("Cloak", "A black cloak with a hood"));

        List<Item> coinPouch = new ArrayList<>();
        coinPouch.add(new Item("Coin Pouch", "Small pouch of coins"));

        List<Item> shield = new ArrayList<>();
        shield.add(new Item("Shield", "A well loved circle shield"));

        // Opening Scene s1 //
        Scene s1 = new Scene("s1", """
                               \nYou wake slowly, the dawn light peers down through the trees, 
                               dissipating into layers of mist. A swift breeze flutters leaves, 
                               carrying subtle sounds of song, it seems you are not alone here. 
                               Surrounded by forest, you notice a path stretching ahead of you.\n""", false);
        // Choices
        Choice s1c1 = new Choice(1, "Follow the path", "s2", 10, null, noItems);
        s1c1.setTransitionText("As you walk the music fades and the mist lifts,\n"
                + "the forest feels quiet and serene.");

        if (player.getTrait() == TraitType.TIMID) {
            Choice s1c2 = new Choice(2, "Abandon the path", "s2", -10, null, survivalGuide);
            s1c2.setTransitionText("As you walk the music builds and\n"
                    + "the mist transforms suddenly into thick fog, \n"
                    + "you can barely see ahead of you.\n\n"
                    + "Warily you slow your pace, the forest feels unsettling.\n"
                    + "Faintly you hear from all around, \"here...lost one...\"\n"
                    + "A book forms in the mist, dropping flat at "
                    + "your feet. \"Survival Guide\"");
            s1.addChoice(s1c1);
            s1.addChoice(s1c2);
        } else {
            Choice s1c2 = new Choice(2, "Abandon the path", "s2", -10, null, noItems);
            if (player.getTrait() == TraitType.BRAVE) {
                s1c2.setTransitionText("As you walk the music builds and\n"
                        + "the mist transforms suddenly into thick fog, \n"
                        + "you can barely see ahead of you.\n\n"
                        + "Confidently you don't break pace.\n"
                        + "Even when you see the same tree 3 times...");
            } else if (player.getTrait() == TraitType.CUNNING) {
                s1c2.setTransitionText("As you walk the music builds and\n"
                        + "the mist transforms suddenly into thick fog, \n"
                        + "you can barely see ahead of you.\n\n"
                        + "You scan the little ground you can see\n"
                        + "to grab a sharp rock, marking trees as \n"
                        + "you walk, in an attempt to not get lost.");
            } else {
                s1c2.setTransitionText("As you walk the music builds and\n"
                        + "the mist transforms suddenly into thick fog, \n"
                        + "you can barely see ahead of you.");
            }
            s1.addChoice(s1c1);
            s1.addChoice(s1c2);
        }
        scenes.put(s1.getSceneId(), s1);

        // Scene 2 s2 //
        Scene s2 = new Scene("s2", """
                               \nThrough the trees you make out a small cabin. Moss and
                               vines have overtaken the walls, which are half rubble...
                               But strangely enough, the chimney is smoking.\n""", false);
        // Choices
        Choice s2c1 = new Choice(1, "Ignore the cabin", "s3", 5, null, noItems);
        s2c1.setTransitionText("You walk past the cabin.");

        Choice s2c2 = new Choice(2, "Go inside", "s3", 10, null, loot);
        if (player.getTrait() == TraitType.BRAVE) {
            s2c2.setTransitionText("You swing open the cabin door, standing alert.\n"
                    + "You scan the room, spotting a chest off to the side. \n"
                    + "Immediately you swing open the chest lid revealing\n"
                    + "supplies, bread, a coin pouch, and a crystal dagger \n"
                    + "that catches your eye.");
        } else if (player.getTrait() == TraitType.TIMID) {
            s2c2.setTransitionText("Carefully you open the cabin door, it smells of \n"
                    + "mold, and dust stirs through the air. Quietly, you make\n"
                    + "your way to a chest in a dark corner, peering in you find\n"
                    + "supplies. Bread, a coin pouch, and a crystal dagger.\n"
                    + "\n"
                    + "You decide to read the survival guide before you continue.\n"
                    + "It says: \"Never stray from the path, for it is the right way\"\n"
                    + "In bold in the center, all of the other pages are blank...");
        } else if (player.getTrait() == TraitType.CUNNING) {
            s2c2.setTransitionText("You grab a nearby branch to push the cabin door\n"
                    + "ajar, you scan the dark gloomy room, the fireplace seems\n"
                    + "recently put out. After deciding its safe, you proceed. \n"
                    + "You find a chest in the darkness, it contains bread, a coin pouch,\n"
                    + "and a crystal dagger. \n"
                    + "'The bread is fresh, somebody lives here' you think, with\n"
                    + "haste you leave the cabin.");
        } else {
            s2c2.setTransitionText("You enter the dark gloomy cabin and find a\n"
                    + "chest, containing bread, a coin pouch, and a crystal dagger.");
        }

        s2.addChoice(s2c1);
        s2.addChoice(s2c2);
        scenes.put(s2.getSceneId(), s2);

        // Scene 3 s3 //
        Scene s3 = new Scene("s3", """
                               \nThe path splits ahead of you, both ways look almost identical. 
                               Lamp posts line either side, flickering faintly in the dim light. 
                               The air is still, and the usual sounds of the forest have gone quiet.\n""", false);

        // Left — brave with dagger (checked at runtime via isAvailable)
        Choice s3c1brave = new Choice(1, "Left", "s4", 0, new Item("Crystal Dagger"), noItems);
        s3c1brave.setRequiredTrait(TraitType.BRAVE);
        s3c1brave.setTransitionText("The lamp posts grow dimmer as you walk,\n"
                + "something feels off. A hooded figure steps out blocking your way.\n"
                + "You don't flinch, drawing the crystal dagger slowly.\n"
                + "They pause, then back into the trees without a word.");

        // Left — cunning with empty inventory (checked at runtime via isAvailable)
        Choice s3c1cunning = new Choice(1, "Left", "s4", 0, null, noItems);
        s3c1cunning.setRequiredTrait(TraitType.CUNNING);
        s3c1cunning.setRequiresEmptyInventory(true);
        s3c1cunning.setTransitionText("The lamp posts grow dimmer as you walk,\n"
                + "something feels off. A hooded figure steps out blocking your way.\n"
                + "The figure reaches for you, quick on your feet you duck under their arm,\n"
                + "slipping past before they can react. They grasp at nothing as you walk\n"
                + "briskly on. You don't look back.");

        // Left — everyone else, gets robbed
        Choice s3c1default = new Choice(1, "Left", "s4", -15, null, noItems);
        s3c1default.setTransitionText("The lamp posts grow dimmer as you walk,\n"
                + "something feels off. A hooded figure steps out blocking your way.\n"
                + "The figure reaches for you, and before you can react, snatches\n"
                + "something and disappears into the trees.");
        s3c1default.setRemovesItem(true);

        Choice s3c2 = new Choice(2, "Right", "s4", 5, null, noItems);
        s3c2.setTransitionText("The lamp posts cast a warm glow as you walk, the path feels calm.\n"
                + "Faintly in the distance you begin to make out sounds of a town.");


        s3.addChoice(s3c1brave);
        s3.addChoice(s3c1cunning);
        s3.addChoice(s3c1default);

        s3.addChoice(s3c2);
        scenes.put(s3.getSceneId(), s3);

        // Scene 4 s4 //
        // NPC
        Npc elara = new Npc("Elara", "Welcome, welcome! Every bottle a wonder, every brew a mystery!\nTake one, consider it a gift from Elara's Emporium!");
        Npc innOwner = new Npc("Inn Owner", "Ah, a traveller! Don't get many of those passing through lately.\nI run the inn next door. Here, take this. First night's on me.");
        Npc halfling = new Npc("Halfling", "Oh... hello. Are you... new here? I don't see many travellers\ncome through. You seem nice.");

        Scene s4 = new Scene("s4", """
                               \nThe path opens up and the trees thin out, giving way to a small 
                               town. Cobblestone streets wind between timber framed buildings, 
                               flower boxes line the windowsills and warm light flickers from within. 
                               The smell of wood smoke drifts through the air. A potions shop sits to your 
                               left, a tavern further down the road, and a stone bridge arches over 
                               the river at the edge of town.\n""", false);

        Choice s4c1;
        if (player.getTrait() == TraitType.CUNNING) {
            s4c1 = new Choice(1, "Potions shop", "s5", 10, null, mysteryBrewAndHealth);
            s4c1.setTransitionText(elara.getName() + ": \"" + elara.getSpeak(TraitType.CUNNING) + "\"\n"
                    + "\nThe shop is cluttered with colourful bottles and strange dried herbs\n"
                    + "hanging from the ceiling. While she chatters away, your eyes wander\n"
                    + "to a neatly labelled health potion on the shelf behind her. You slip\n"
                    + "it into your pocket alongside the mystery brew she presses into your\n"
                    + "hands. She waves you off cheerfully, none the wiser.");
        } else {
            s4c1 = new Choice(1, "Potions shop", "s5", 0, null, mysteryBrew);
            s4c1.setTransitionText(elara.getName() + ": \"" + elara.getSpeak(TraitType.BRAVE) + "\"\n"
                    + "\nThe shop is cluttered with colourful bottles and strange dried herbs\n"
                    + "hanging from the ceiling. She thrusts a small bottle into your hands\n"
                    + "before you can say a word. The label reads \"Mystery Brew.\"\n"
                    + "You pocket it and leave.");
        }

        Choice s4c2;
        if (player.getTrait() == TraitType.TIMID) {
            s4c2 = new Choice(2, "Tavern", "s5", 10, null, cloak);
            s4c2.setTransitionText("The tavern is warm and loud, a little overwhelming.\n"
                    + "You hover near the door until a small halfling at a corner table\n"
                    + "catches your eye and offers a shy wave. You take a seat beside them.\n"
                    + halfling.getName() + ": \"" + halfling.getSpeak(TraitType.TIMID) + "\"\n"
                    + "After a quiet moment they slide their spare cloak across the table.\n"
                    + "\"You looked cold,\" they say softly.");
        } else {
            s4c2 = new Choice(2, "Tavern", "s5", 10, null, roomVoucher);
            s4c2.setTransitionText("The tavern is warm and loud, the smell of ale and roasted meat\n"
                    + "thick in the air. A stout man eating alone at the bar spots you\n"
                    + "and waves you over.\n"
                    + innOwner.getName() + ": \"" + innOwner.getSpeak(TraitType.BRAVE) + "\"\n"
                    + "He slides a small voucher across to you with a smile.");
        }

        Choice s4c3;
        if (player.getTrait() == TraitType.BRAVE) {
            s4c3 = new Choice(3, "Bridge", "s5", 10, null, shield);
            s4c3.setTransitionText("You stride out onto the bridge, the water rushing quietly below.\n"
                    + "Halfway across something catches your eye, a shield leaning against\n"
                    + "the railing, left behind or forgotten. You pick it up, giving it a\n"
                    + "firm knock. Solid. You strap it to your back and carry on.");
        } else {
            s4c3 = new Choice(3, "Bridge", "s5", 5, null, coinPouch);
            s4c3.setTransitionText("The bridge stretches over a wide calm river, the water dark\n"
                    + "and glassy below. Halfway across you notice a coin pouch wedged\n"
                    + "between the railings, left behind by someone passing through.\n"
                    + "You pocket it and take in the view for a moment.");
        }

        s4.addChoice(s4c1);
        s4.addChoice(s4c2);
        s4.addChoice(s4c3);
        scenes.put(s4.getSceneId(), s4);

        // Scene 5 s5 - Ending //
        Scene s5 = new Scene("s5", """
                               \nThe inn sits at the end of a cobblestone lane, warm light and 
                               the sound of chatter spilling out from within. You push open 
                               the door to find the place busy and lively, locals filling 
                               every table. You make your way to the front desk.\n""", true);

        scenes.put(s5.getSceneId(), s5);

        Npc innkeeperBad = new Npc("Innkeeper", "Welcome to the Inn! Let me see what we can do for you.\"\n  \"I am so sorry, we have no rooms available.");
        Npc innkeeperNeutral = new Npc("Innkeeper", "Welcome to the Inn! Let me see what we can do for you.\"\n  \"You're in luck! One room left.");
        Npc innkeeperGood = new Npc("Innkeeper", "Welcome to the Inn! Let me see what we can do for you.\"\n  \"In the meantime feel free to join the townsfolk feast!");

        String badDesc;
        if (player.getTrait() == TraitType.BRAVE) {
            badDesc = "\nYou shrug it off and find a dry spot on the street outside, oh well.";
        } else if (player.getTrait() == TraitType.CUNNING) {
            badDesc = "\nYou peek around the corner, but you can't see any spare rooms.\n"
                    + "You huddle behind the Inn to sleep.";
        } else {
            badDesc = "\nYou thank them quietly, and find a sheltered spot nearby.\n"
                    + "This will be a long night.";
        }
        Ending bad = new Ending("bad", badDesc, -20, -1);
        bad.setNpc(innkeeperBad);
        scenes.put(bad.getSceneId(), bad);

        String neutralDesc;
        if (player.getTrait() == TraitType.BRAVE) {
            neutralDesc = "\nYou drop your things, before falling into bed.\n"
                    + "You are asleep almost immediately.";
        } else if (player.getTrait() == TraitType.CUNNING) {
            neutralDesc = "\nYou give the room a quick scan, nothing unordinary.\n"
                    + "Satisfied, you settle into bed and sleep soundly.";
        } else {
            neutralDesc = "\nYou thank them quietly and head upstairs. The noise from below\n"
                    + "slowly fades as you pull the blanket up, relieved to have a safe\n"
                    + "place to rest. You sleep well.";
        }
        Ending neutral = new Ending("neutral", neutralDesc, 0, 19);
        neutral.setNpc(innkeeperNeutral);
        scenes.put(neutral.getSceneId(), neutral);

        String goodDesc;
        if (player.getTrait() == TraitType.BRAVE) {
            goodDesc = "\nYou don't need to be asked twice. You spend the night swapping\n"
                    + "stories over a feast, laughing loud and eating well.\n"
                    + "You head to bed confident and excited for the journey.";
        } else if (player.getTrait() == TraitType.CUNNING) {
            goodDesc = "\nYou take a seat, listening more than talking, picking up every\n"
                    + "bit of local gossip you can. By the end of the night you know\n"
                    + "more about this town than most people who live here.\n"
                    + "You head to your room quietly satisfied.";
        } else {
            goodDesc = "\nYou hesitate, then take a seat at the edge of the group.\n"
                    + "By the end of the night you are laughing and making friends.\n"
                    + "You head to bed feeling lighter than you have in a while.";
        }
        Ending good = new Ending("good", goodDesc, 20, 40);
        good.setNpc(innkeeperGood);
        scenes.put(good.getSceneId(), good);
    }

    // Getters and setters
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player p) {
        this.player = p;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Map<String, Scene> getScenes() {
        return scenes;
    }

}

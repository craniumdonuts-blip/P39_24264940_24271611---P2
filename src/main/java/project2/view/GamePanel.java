
package project2.view;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import project2.controller.GameController;
import project2.model.Choice;
import project2.model.Item;
import project2.model.Scene;

/**
 *
 * @author angie
 */
public class GamePanel extends JPanel implements GameEventListener{
    private GameController controller;
    private GameWindow window;
    private BGPanel background;
    
    // map scene/transition id to image paths
    private Map<String, String> sceneImages = new HashMap<>();
    
    //top bar
    private JButton saveBtn;
    private JButton inventoryBtn;
    
    //dialogue area
    private JLabel npcNameLabel; // hidden if no NPC
    private JTextArea dialogueBox; // shows scene text or transition text
    
    // choices. rebuilt every scene
    private JPanel choicePanel;
    
    // next button. only visible during transitions
    private JButton nextBtn;
    
    //popups. hidden until button clicked
    private JPanel savePopup;
    private JPanel inventoryPopup;
    
    // true when player is reading transition text
    // false when in a scene
    private boolean showingTransition = false;
    
    // stores which choice the player clicked 
    private int pendingChoiceNum = 0;
    
    public GamePanel(GameController controller, GameWindow window){
        this.controller = controller;
        this.window = window;
        setLayout(new BorderLayout());
        
        // set first background
        background = new BGPanel("/images/s1Placeholder.png"); // jumps straight into s1
        background.setLayout(new BorderLayout());
        
        // add images and components
        initSceneImages();
        initComponents();
        
        controller.addGameEventListener(this);
        
        // allows popups to appear on top of background and game content
        JLayeredPane layers = new JLayeredPane();
        layers.add(background, JLayeredPane.DEFAULT_LAYER); // sits at the bottom
        layers.add(inventoryPopup, JLayeredPane.POPUP_LAYER); // popups sit on top
        layers.add(savePopup, JLayeredPane.POPUP_LAYER);
        
        addComponentListener(new ResizeListener(background, savePopup, inventoryPopup));
        
        add(layers);
    }
    
    // sets scene backgrounds
    private void initSceneImages(){
        // scene backgrounds
        sceneImages.put("s1", "/images/s1Placeholder.png");
        sceneImages.put("s2", "/images/s2Placeholder.png");
        sceneImages.put("s3", "/images/s3Placeholder.png");
        sceneImages.put("s4", "/images/s4Placeholder.png");
        sceneImages.put("s5", "/images/s5Placeholder.png");
        
        //transition backgrounds (shown after choice, before next scene)
        sceneImages.put("s1c1", "/images/s1c1Placeholder.png");
        sceneImages.put("s1c2", "/images/s1c2Placeholder.png");
        
        sceneImages.put("s2c1", "/images/s2c1Placeholder.png");
        sceneImages.put("s2c2", "/images/s2c2Placeholder.png");
        
        sceneImages.put("s3c1", "/images/s3c1Placeholder.png");
        sceneImages.put("s3c2", "/images/s3c2Placeholder.png");
        
        sceneImages.put("s4c1", "/images/s4c1Placeholder.png");
        sceneImages.put("s4c2", "/images/s4c2Placeholder.png");
        sceneImages.put("s4c3", "/images/s4c3Placeholder.png");
    }
    
    // builds buttons, dialogue box, npcNameLabel, choicePanel, nextBtn,
    // savePopup, inventoryPopup
    private void initComponents(){
        saveBtn = new JButton("Save");
        inventoryBtn = new JButton("Inventory");
        npcNameLabel = new JLabel();
        dialogueBox = new JTextArea();
        nextBtn = new JButton("Next");
        nextBtn.setVisible(false);
        savePopup = new JPanel();
        inventoryPopup = new JPanel();
        
        // npc name label hidden first
        npcNameLabel.setVisible(false);
        
        // both popups start hidden
        savePopup.setLayout(new BoxLayout(savePopup, BoxLayout.Y_AXIS));
        savePopup.setVisible(false);
        
        inventoryPopup.setLayout(new BoxLayout(inventoryPopup, BoxLayout.Y_AXIS));
        inventoryPopup.setVisible(false);
        
        // setting up choices
        choicePanel = new JPanel();
        choicePanel.setOpaque(false); // so background shows
        
        //top bar with save & inventory
        JPanel topBar = new JPanel();
        topBar.setOpaque(false); // so background shows
        topBar.add(saveBtn);
        topBar.add(inventoryBtn);
        background.add(topBar, BorderLayout.NORTH);
        
        //dialogue area at bottom
        JPanel dialogueArea = new JPanel();
        dialogueArea.setLayout(new BorderLayout());
        dialogueArea.setOpaque(false);
        dialogueArea.add(npcNameLabel, BorderLayout.NORTH);
        dialogueArea.add(dialogueBox, BorderLayout.CENTER);
        background.add(dialogueArea, BorderLayout.SOUTH);
        
        // bottom panel that holds both choicePanel and next button
        // swaps between them (because adding two things to SOUTH of dialogueArea)
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(choicePanel);
        bottomPanel.add(nextBtn);
        dialogueArea.add(bottomPanel, BorderLayout.SOUTH);
        
        // when next button is clicked go to handleNext()
        nextBtn.addActionListener(e -> handleNext());
        
        // when save or inventory button clicked go to their toggle methods
        saveBtn.addActionListener(e -> toggleSavePopup());
        inventoryBtn.addActionListener(e -> toggleInventoryPopup());
    }
    
    // called when a choice button is clicked. 
    // shows transition text + transition background
    // hides choice buttons, shows next button
    private void showTransition(int choiceNum){
        String transitionText = controller.getTransitionText(choiceNum);
        String imageKey = controller.getTransitionImageKey(choiceNum);
        
        dialogueBox.setText(transitionText);
        updateBackground(imageKey);
        
        choicePanel.setVisible(false);
        nextBtn.setVisible(true);
        showingTransition = true;
        
        //store which choice was made so Next can finish it
        pendingChoiceNum = choiceNum;
    }
    
    //called when Next is clicked during a transition. 
    //actually advances the scene in the model
    private void handleNext(){
        controller.processChoice(pendingChoiceNum);
        //processChoice fires onSceneChanged or onGameOver via listener
        nextBtn.setVisible(false);
        showingTransition = false;
    }
    
    //rebuilds dialogue box and npc name for the current scene
    private void updateScene(){
        String npcName = controller.getCurrentNpcName();
        if (npcName != null){
            npcNameLabel.setText(npcName);
            npcNameLabel.setVisible(true);
        } else {
            npcNameLabel.setVisible(false);
        }
        dialogueBox.setText(controller.getCurrentSceneText());
    }
    
    //rebuilds choice buttons for the current scene
    private void updateChoices(){
        choicePanel.removeAll();
        List<Choice> choices = controller.getAvailableChoices();

        if (choices.isEmpty()) {
            // no choices means end of scene, show a continue button
            JButton continueBtn = new JButton("Continue");
            continueBtn.addActionListener(e -> {
                controller.checkEnding();
                // onGameOver() will fire via the listener
            });
            choicePanel.add(continueBtn);
        } else {
            for (Choice choice : choices) {
                JButton btn = new JButton(choice.getChoiceDesc());
                btn.addActionListener(e -> showTransition(choice.getNumber()));
                choicePanel.add(btn);
            }
        }

        choicePanel.revalidate();
        choicePanel.repaint();
        choicePanel.setVisible(true);
    }
    
    // change background per scene
    private void updateBackground(String key){
        String path = sceneImages.get(key);
        if (path != null){
            background.setImage(path);
        }
    }
    
    // populate gamePanel
    public void startGame(){
        updateScene();
        updateChoices();
        updateBackground(controller.getCurrentScene().getSceneId());
    }
    
    // save popup
    private void toggleSavePopup(){
        if (savePopup.isVisible()){
            savePopup.setVisible(false);
            return;
        }
        
        // rebuilds slots every time it opens (in case saves changed)
        savePopup.removeAll();
        
        List<Integer> saves = controller.listSaves();
        
        //existing slots
        for(int slot : saves){
            JButton slotBtn = new JButton("Slot " + slot);
            slotBtn.addActionListener(e -> {
                controller.saveGame(slot);
                savePopup.setVisible(false);
            });
            savePopup.add(slotBtn);
        }
        
        // new slot button
        int newSlot = saves.size()+1;
        JButton newBtn = new JButton("New Slot " + newSlot);
        newBtn.addActionListener(e -> {
            controller.saveGame(newSlot);
            savePopup.setVisible(false);
        });
        savePopup.add(newBtn);
        
        //close button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> savePopup.setVisible(false));
        savePopup.add(closeBtn);
        
        savePopup.revalidate();
        savePopup.repaint();
        savePopup.setVisible(true);
    }
    
    // inventory popup
    private void toggleInventoryPopup(){
        if(inventoryPopup.isVisible()){
            inventoryPopup.setVisible(false);
            return;
        }
        
        inventoryPopup.removeAll();
        
        List<Item> items = controller.getInventoryItems();
        
        if (items.isEmpty()){
            inventoryPopup.add(new JLabel("Your inventory is empty"));
        } else {
            JPanel itemList = new JPanel();
            itemList.setLayout(new BoxLayout(itemList, BoxLayout.Y_AXIS));
            
            for (Item item : items){
                // each item shows name & description
                JLabel itemLabel = new JLabel(item.getName() + " - " + item.getDescription());
                itemList.add(itemLabel);
            }
            
            inventoryPopup.add(itemList);
        }
        
        // close button
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> inventoryPopup.setVisible(false));
        inventoryPopup.add(closeBtn);
        
        inventoryPopup.revalidate();
        inventoryPopup.repaint();
        inventoryPopup.setVisible(true);
    }
    

    // GameEventListener methods
    // fired by controller after processChoice advances the scene
    @Override
    public void onSceneChanged(Scene scene) {
        updateScene();
        updateChoices();
        updateBackground(scene.getSceneId());
    }
    @Override
    public void onGameOver() {
        window.showPanel("ENDING");
    }
    
    // called once when panel is first added to the screen
    // fills scene content before player sees it
    @Override
    public void addNotify(){
        super.addNotify();
        if (controller.getCurrentScene() == null) return;
        updateScene();
        updateChoices();
        updateBackground(controller.getCurrentScene().getSceneId());
    }
}

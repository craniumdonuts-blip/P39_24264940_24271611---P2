
package project2.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import project2.controller.GameController;

/**
 *
 * @author angie
 */
public class MainMenuPanel extends JPanel{
    private GameController controller;
    private GameWindow window;
    private BGPanel background;
    
    private JLabel greetingLabel = new JLabel();
    
    // Buttons for new game, load game, delete save
    private JButton newGameBtn;
    private JButton loadGameBtn;
    private JButton deleteGameBtn;
    
    // slot boxes shown when load or delete is clicked
    private JPanel slotPanel = new JPanel();
    
    public MainMenuPanel(GameController controller, GameWindow window){
        this.controller = controller;
        this.window = window;
        setLayout(new BorderLayout());
        
        // create the background
        background = new BGPanel("/images/MainMenuPlaceholder.png");
        background.setLayout(new BorderLayout());
        
        // call initComponents to get menu buttons
        initComponents();
        
        // set greetingLabel text
        greetingLabel.setText("Greetings, traveller!");
        greetingLabel.setFont(new Font("Sans Serif", Font.PLAIN, 30));

        // create wrapper panel with FlowLayout centred
        JPanel greetingWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        greetingWrapper.setOpaque(false); // so background shows
        greetingWrapper.add(greetingLabel);
        
        slotPanel.setOpaque(false); // so background shows
        
        // adding components to background panel
        background.add(greetingWrapper, BorderLayout.NORTH);
        background.add(slotPanel, BorderLayout.CENTER);
        
        add(background);
    }
    
    // builds the menu buttons
    private void initComponents() {
        newGameBtn = new JButton("1. New Game");
        loadGameBtn = new JButton("2. Load Game");
        deleteGameBtn = new JButton("3. Delete Game");
        
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false); // so background shows
        
        btnPanel.add(newGameBtn);
        btnPanel.add(loadGameBtn);
        btnPanel.add(deleteGameBtn);
        
        newGameBtn.addActionListener(e -> window.showPanel("CHARACTER_SETUP"));
        loadGameBtn.addActionListener(e -> showSlots("load"));
        deleteGameBtn.addActionListener(e -> showSlots("delete"));
        
        background.add(btnPanel, BorderLayout.SOUTH);
    }
    
    // builds the slot boxes from controller.listSaves()
    private void showSlots(String action){
        slotPanel.removeAll(); // clear previous slots
        
        List<Integer> saves = controller.listSaves();
        
        if (saves.isEmpty()){
            JLabel noSavesLabel = new JLabel("No saves found");
            noSavesLabel.setFont(new Font("Sans Serif", Font.PLAIN, 30));
            slotPanel.add(noSavesLabel);
        } else {
            for (int slot : saves){
                JButton slotBtn = new JButton("Slot " + slot);
                slotBtn.addActionListener(e -> handleSlotClick(slot, action));
                slotPanel.add(slotBtn);
            }
        }
        
        slotPanel.revalidate(); //refresh panel
        slotPanel.repaint();
    }
    
    // handles what happens when a slot box is clicked
    private void handleSlotClick(int slot, String action){
        switch (action){
            case "load":
                controller.loadGame(slot);
                window.showPanel("GAME");
                break;
            case "delete":
                controller.deleteSave(slot);
                showSlots("delete"); //refresh slot panel to show deletion
                break;
        }
    }
}
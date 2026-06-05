package project2.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
public class MainMenuPanel extends JPanel {

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

    public MainMenuPanel(GameController controller, GameWindow window) {
        this.controller = controller;
        this.window = window;
        setLayout(new BorderLayout());

        // create the background
        background = new BGPanel("/images/MainMenu&CharacterSetup.png");
        background.setLayout(new java.awt.GridBagLayout());

        // call initComponents to get menu buttons
        initComponents();

        // set greetingLabel text
        greetingLabel.setText("Greetings, traveller!");
        greetingLabel.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        greetingLabel.setForeground(Color.white);

        slotPanel.setOpaque(false); // so background shows

        // add greeting and slot panel to centre of background
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        background.add(greetingLabel, gbc);

        gbc.gridy = 2;
        background.add(slotPanel, gbc);

        add(background, BorderLayout.CENTER);
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

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        background.add(btnPanel, gbc);
    }

    // builds the slot boxes from controller.listSaves()
    private void showSlots(String action) {
        slotPanel.removeAll(); // clear previous slots

        List<Integer> saves = controller.listSaves();

        if (saves.isEmpty()) {
            JLabel noSavesLabel = new JLabel("No saves found");
            noSavesLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));
            noSavesLabel.setForeground(Color.white);
            slotPanel.add(noSavesLabel);
        } else {
            for (int slot : saves) {
                JButton slotBtn = new JButton("Slot " + slot);
                slotBtn.addActionListener(e -> handleSlotClick(slot, action));
                slotPanel.add(slotBtn);
            }
        }

        slotPanel.revalidate(); //refresh panel
        slotPanel.repaint();
    }

    // handles what happens when a slot box is clicked
    private void handleSlotClick(int slot, String action) {
        switch (action) {
            case "load":
                controller.loadGame(slot);
                window.showPanel("GAME");
                window.startGame();
                break;
            case "delete":
                controller.deleteSave(slot);
                showSlots("delete"); //refresh slot panel to show deletion
                break;
        }
    }
}
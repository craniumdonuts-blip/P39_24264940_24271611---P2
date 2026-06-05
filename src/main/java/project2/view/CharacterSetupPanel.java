package project2.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import project2.controller.GameController;
import project2.model.TraitType;

/**
 *
 * @author angie
 */
public class CharacterSetupPanel extends JPanel {

    private GameController controller;
    private GameWindow window;
    private BGPanel background;

    // for player name
    private JLabel enterNameLabel = new JLabel();
    private JTextField playerName = new JTextField(20); //20 = column width

    // for traits
    private JLabel askTraitLabel = new JLabel(); //prompts player to choose a trait
    private JButton braveBtn;
    private JButton cunningBtn;
    private JButton timidBtn;
    private TraitType selectedTrait; //stores the trait the player has selected before confirming

    // confirm button
    private JButton confirmBtn;

    public CharacterSetupPanel(GameController controller, GameWindow window) {
        this.controller = controller;
        this.window = window;
        setLayout(new BorderLayout());

        // create the background
        background = new BGPanel("/images/MainMenu&CharacterSetup.png");
        background.setLayout(new java.awt.GridBagLayout());

        // call initComponents to get buttons
        initComponents();

        // set enterNameLabel text
        enterNameLabel.setText("Enter your name: ");
        enterNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        enterNameLabel.setForeground(Color.white);

        // set askTraitLabel text
        askTraitLabel.setText("Who are you, traveller?");
        askTraitLabel.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        askTraitLabel.setForeground(Color.white);

        // create name wrapper panel
        JPanel nameWrapper = new JPanel();
        nameWrapper.setOpaque(false); // so background shows
        nameWrapper.add(enterNameLabel);
        nameWrapper.add(playerName);

        // create centre panel with both labels stacked
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 10));
        centerPanel.setOpaque(false); // so background shows
        centerPanel.add(askTraitLabel);
        centerPanel.add(nameWrapper);

        // add centre panel to background using GridBagLayout to centre vertically
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        background.add(centerPanel, gbc);

        add(background, BorderLayout.CENTER);
    }

    // builds the buttons
    private void initComponents() {
        braveBtn = new JButton("1. Brave");
        cunningBtn = new JButton("2. Cunning");
        timidBtn = new JButton("3. Timid");
        confirmBtn = new JButton("Confirm?");

        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false); // so background shows

        btnPanel.add(braveBtn);
        btnPanel.add(cunningBtn);
        btnPanel.add(timidBtn);
        btnPanel.add(confirmBtn);

        braveBtn.addActionListener(e -> selectedTrait = TraitType.BRAVE);
        cunningBtn.addActionListener(e -> selectedTrait = TraitType.CUNNING);
        timidBtn.addActionListener(e -> selectedTrait = TraitType.TIMID);
        confirmBtn.addActionListener(e -> confirmSelection());

        // add buttons below centre panel
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        background.add(btnPanel, gbc);
    }

    //called when confirm is clicked. moves to first game scene with name and trait
    private void confirmSelection() {
        String name = playerName.getText().trim();
        if (name.isEmpty() || selectedTrait == null) {
            // Do nothing/show a message
            return;
        }
        controller.startNewGame(name, selectedTrait);
        window.showPanel("GAME");
        window.startGame();
    }
}
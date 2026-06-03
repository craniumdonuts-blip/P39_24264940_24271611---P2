package project2.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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

public class CharacterSetupPanel extends JPanel{
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
    
    public CharacterSetupPanel(GameController controller, GameWindow window){
        this.controller = controller;
        this.window = window;
        setLayout(new BorderLayout());
        
        // create the background
        background = new BGPanel("/images/CharacterSetupPlaceholder.png");
        background.setLayout(new BorderLayout());
        
        // call initComponents to get buttons
        initComponents();
        
        // set enterNameLabel text
        enterNameLabel.setText("Enter your name: ");
        enterNameLabel.setFont(new Font("Sans Serif", Font.PLAIN, 30));
                
        // set askTraitLabel text
        askTraitLabel.setText("Who are you, traveller?");
        askTraitLabel.setFont(new Font("Sans Serif", Font.PLAIN, 30));

        // create name wrapper panel with FlowLayout centred
        JPanel nameWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nameWrapper.setOpaque(false); // so background shows
        nameWrapper.add(enterNameLabel);
        nameWrapper.add(playerName);
        
        // create askTrait wrapper panel with FlowLayout centred
        JPanel askTraitWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        askTraitWrapper.setOpaque(false); // so background shows
        askTraitWrapper.add(askTraitLabel);
        
        // create north panel so both name and trait show at the top
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.setOpaque(false); // so background shows
        northPanel.add(askTraitWrapper, BorderLayout.NORTH);
        northPanel.add(nameWrapper, BorderLayout.CENTER);
        
        // adding components to background panel
        background.add(northPanel, BorderLayout.NORTH);
        
        add(background);
    }
    
    // builds the buttons
    private void initComponents(){
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
        
        background.add(btnPanel, BorderLayout.SOUTH);
    }
    
    //called when confirm is clicked. moves to first game scene with name and trait
    private void confirmSelection(){
        String name = playerName.getText().trim();
        controller.startNewGame(name, selectedTrait);
        window.showPanel("GAME");
        window.startGame();
    }
}

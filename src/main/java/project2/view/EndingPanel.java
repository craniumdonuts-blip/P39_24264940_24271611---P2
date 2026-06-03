
package project2.view;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import project2.controller.GameController;

/**
 *
 * @author angie
 */

// Displays the ending scene: NPC name, ending text, and a return to main menu button.
// Implements GameEventListener to receive onGameOver() from the controller.

public class EndingPanel extends JPanel implements GameEventListener{
    private GameController controller;
    private GameWindow window;
    private BGPanel background;
    
    // dialogue area
    private JLabel npcNameLabel; // shows innkeeper name
    private JTextArea endingText; // shows trait-specific ending text
    
    // navigation
    private JButton mainMenuBtn;

    public EndingPanel(GameController controller, GameWindow window) {
        this.controller = controller;
        this.window = window;
        setLayout(new BorderLayout());

        background = new BGPanel("/images/endingPlaceholder.png");
        background.setLayout(new BorderLayout());

        initComponents();

        controller.addGameEventListener(this);

        add(background);
    }
    
    // builds npcNameLabel, endingText, mainMenuBtn and adds them to background
    private void initComponents() {
        npcNameLabel = new JLabel();
        npcNameLabel.setVisible(false); // hidden until populateEnding() sets it

        endingText = new JTextArea();
        endingText.setEditable(false);
        endingText.setOpaque(false); // so background shows through
        endingText.setLineWrap(true);
        endingText.setWrapStyleWord(true);

        mainMenuBtn = new JButton("Main Menu");
        mainMenuBtn.addActionListener(e -> window.showPanel("MAIN_MENU"));

        // dialogue area at bottom
        JPanel dialogueArea = new JPanel();
        dialogueArea.setLayout(new BorderLayout());
        dialogueArea.setOpaque(false);
        dialogueArea.add(npcNameLabel, BorderLayout.NORTH);
        dialogueArea.add(endingText, BorderLayout.CENTER);
        dialogueArea.add(mainMenuBtn, BorderLayout.SOUTH);
        background.add(dialogueArea, BorderLayout.SOUTH);
    }

    // reads ending text and NPC name from controller and updates the display
    private void populateEnding() {
        // getEndingText() calls checkEnding() which loads good/neutral/bad
        // and returns the trait-specific ending text
        String text = controller.getEndingText();
        endingText.setText(text);

        String npcName = controller.getCurrentNpcName();
        if (npcName != null) {
            npcNameLabel.setText(npcName);
            npcNameLabel.setVisible(true);
        } else {
            npcNameLabel.setVisible(false);
        }
        
        // set background
        background.setImage("/images/endingPlaceholder.png");
    }
    
    // fired by controller when an Ending scene is reached
    @Override
    public void onGameOver() {
        populateEnding();
        window.showPanel("ENDING");
    }

    // not needed in EndingPanel but required by GameEventListener
    @Override
    public void onSceneChanged(project2.model.Scene scene) {}
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project2.view;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import project2.controller.GameController;

/**
 *
 * @author angie
 */
public class GameWindow extends JFrame{
    // Used to show a component one at a time
    private CardLayout cardLayout = new CardLayout();
    private JPanel container = new JPanel(cardLayout);
    
    // holds all panels in a CardLayout and swaps between them
    public GameWindow(GameController controller){
        MainMenuPanel mainMenu = new MainMenuPanel(controller);
        // add other panels here later
        
        // adds panels into card deck, giving it a name
        container.add(mainMenu, "MAIN_MENU");
        // adds card deck into JFrame
        add(container);
        
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centres frame on the screen
        setVisible(true);
    }
    
    // called by controller to switch between panels
    public void showPanel(String name){
        cardLayout.show(container, name);
    }
}

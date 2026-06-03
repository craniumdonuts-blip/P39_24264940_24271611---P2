/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package project2.view;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author angie
 */
public class BGPanel extends JPanel{
    private Image image;

    // gets the image needed for the panel/class
    public BGPanel(String imagePath) {
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage();
    }
    
    //swaps the image at runtime depending on what scene the game is at
    public void setImage(String imagePath){
        this.image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        repaint(); //redraw with new image
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, getWidth(), getHeight(), null);
    }
}

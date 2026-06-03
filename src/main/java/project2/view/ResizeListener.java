package project2.view;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

/**
 *
 * @author angie
 */

// for GamePanel's layers. resize everything when panel size changes
public class ResizeListener extends ComponentAdapter{
    private BGPanel background;
    private JPanel savePopup;
    private JPanel inventoryPopup;
    
    public ResizeListener(BGPanel background, JPanel savePopup, JPanel inventoryPopup){
        this.background = background;
        this.savePopup = savePopup;
        this.inventoryPopup = inventoryPopup;
    }
    
    @Override
    public void componentResized(ComponentEvent e){
        int w = e.getComponent().getWidth();
        int h = e.getComponent().getHeight();
        
        background.setBounds(0, 0, w, h);
        savePopup.setBounds((w - 400) / 2, (h - 300) / 2, 400, 300);
        inventoryPopup.setBounds((w - 400) / 2, (h - 300) / 2, 400, 300);
    }
}

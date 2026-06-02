package Project2;

import project2.controller.GameController;
import project2.view.GameWindow;

/**
 *
 * @author ella
 */
public class Main {

    public static void main(String[] args) {
        // launches game window
        GameController controller = new GameController();
        new GameWindow(controller);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package project2.view;

import project2.model.Scene;

/**
 *
 * @author angie
 */
// implemented by view panels that need to react to game state changes
// controller calls these
public interface GameEventListener {
    // used after choice is processed, view updates scene display
    void onSceneChanged(Scene scene);
    // used when end scene is reached, view shows endingpanel
    void onGameOver();
}

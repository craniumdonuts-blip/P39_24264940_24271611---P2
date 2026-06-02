
package project2.model;

/**
 *
 * @author ella
 */
public class RegScene extends Scene{
    
    // Non-end scene
    public RegScene(String sceneID, String sceneDesc) {
        // End scene false for regular scenes
        super(sceneID, sceneDesc, false);
    }
    
    // For scenes that trigger an ending check
    public RegScene(String sceneId, String sceneDesc, boolean isEndScene) {
        super(sceneId, sceneDesc, isEndScene);
    }
}

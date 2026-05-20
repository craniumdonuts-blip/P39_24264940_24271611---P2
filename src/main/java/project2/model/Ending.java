package project2.model;

/**
 *
 * @author ella
 */
public class Ending extends Scene {

    private int minPoints = -20;
    private int maxPoints = 40;

    public Ending(String sceneId, String sceneDesc, int minPoints, int maxPoints) {
        super(sceneId, sceneDesc, true); // Always true for isEnding
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public boolean triggerEnding(Player player) {
        int points = player.getTotalPoints();
        return points >= minPoints && points <= maxPoints;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

}

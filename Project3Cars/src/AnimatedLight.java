// TODO Document
public class AnimatedLight implements Runnable {
    private int xPos;
    private int yPos;
    private LightState lightState = LightState.GREEN;

    public AnimatedLight(int x, int y) {
        xPos = x;
        yPos = y;
    }

    @Override
    public void run() {
        // TODO Every cycle, a time is randomly generated between [5, 15] seconds. This is the time before the light turns red.
        // TODO Every cycle, a time is randomly generated between [2, 5] seconds. This is the time the light stays red.
        // TODO The light starts green, changes to yellow 3 seconds before it turns red, then turns green and restarts again.
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public LightState getLightState() {
        return lightState;
    }

}

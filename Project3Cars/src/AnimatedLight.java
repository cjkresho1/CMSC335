
/**
 * AnimatedLight.java
 * Date: 10.10.23
 * @author Charles Kresho
 * Purpose: AnimatedLight is a state machine that runs on its own thread. The light state changes every so often, and will notify listeners to its change
 */
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class AnimatedLight implements Runnable {
    private int xPos;
    private int yPos;
    private LightState lightState = LightState.GREEN; // Lights always start as green
    private boolean isPaused, terminate; // Flags for the logic of the light
    private double timeRemainingSec; // Time remainin in the current state of the state machine
    Random rand;

    // Locks for thread logic
    private final Lock lock = new ReentrantLock();
    private final Condition unpauseCondition = lock.newCondition();

    // Listeners for event notification
    private ChangeEvent changeEvent = null;
    private EventListenerList carListenerList = new EventListenerList();

    // Theoretical "frame rate"
    private static final double REFRESH_RATE = 144.0;

    /**
     * Create a new AnimatedLight that is green
     * @param x x pixel coordinate of the light
     * @param y y pixel coordinate of the light
     */
    public AnimatedLight(int x, int y) {
        xPos = x;
        yPos = y;
        this.isPaused = false;
        rand = new Random();
        // Lights stay green for between 10-15 seconds
        timeRemainingSec = rand.nextInt(11) + 5;
        terminate = false;
    }

    @Override
    public void run() {
        // The light loops forever until destroyed
        while (true) {
            // If flagged, break the run loop to terminate the thread
            if (terminate) {
                break;
            }

            // Lock the light so that changes and updates can be made
            lock.lock();
            try {
                // If the light should be paused, await unpause
                while (isPaused) {
                    unpauseCondition.await();
                }

                // While running, tick every "frame"
                try {
                    Thread.sleep((long) (1000.0 / REFRESH_RATE));
                } catch (InterruptedException e) {
                }

                // Reduce the time remaining by a frame's worth of time
                timeRemainingSec -= (1.0 / REFRESH_RATE);

                // If the light's time is up (within an error margin), change to the next state
                if (timeRemainingSec < 0.001) {
                    switch (lightState) {
                        case GREEN:
                            lightState = LightState.YELLOW;
                            // Lights remain yellow for 3 seconds
                            timeRemainingSec = 3.0;
                            break;
                        case YELLOW:
                            lightState = LightState.RED;
                            // Let cars know the light has changed color
                            fireLightStateChanged();
                            // Lights remain red for between 5-10 seconds
                            timeRemainingSec = (double) rand.nextInt(6) + 5.0;
                            break;
                        case RED:
                            lightState = LightState.GREEN;
                            // Let cars know the light has changed color
                            fireLightStateChanged();
                            // Lights stay green for between 10-15 seconds
                            timeRemainingSec = rand.nextInt(11) + 5;
                            break;
                        default:
                            System.out.println("Default case on light switch happened. Should ever happen...");
                            timeRemainingSec = 1.0;
                            break;
                    }
                }
            } catch (InterruptedException e) {
            } finally {
                // Release the lock when done updating
                lock.unlock();
            }
        }
    }

    /**
     * Pause the current light
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Unpause the current light
     */
    public void unpause() {
        while (isPaused) {
            // Lock while unpausing so that the light doesn't attempt to change itself whle the pause is being released
            lock.lock();
            isPaused = false;
            unpauseCondition.signalAll();
            lock.unlock();
        }
    }

    /**
     * Terminate the thread the current light is running on
     */
    public void terminate() {
        terminate = true;
        // Unpause so that the thread can terminate
        unpause();
    }

    /**
     * Get the xPosition of the light
     * @return x pixel of the light
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Set the xPosition of the light
     * @param xPos x pixel of the light
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * Get the yPosition of the light
     * @return y pixel of the light
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * Set the yPosition of the light
     * @param yPos y pixel of the light
     */
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * Return the light's color as a LightState
     * @return LightState of the light
     */
    public LightState getLightState() {
        return lightState;
    }

    /*
     * The rest of this is event handling code copied from
     * DefaultBoundedRangeModel.
     */
    public void addChangeListener(ChangeListener l) {
        carListenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        carListenerList.remove(ChangeListener.class, l);
    }

    protected void fireLightStateChanged() {
        Object[] listeners = carListenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

}

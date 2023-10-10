// TODO Document

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
    private LightState lightState = LightState.GREEN;
    private boolean isPaused, terminate;
    private double timeRemainingSec;
    Random rand;
    private final Lock lock = new ReentrantLock();
    private final Condition unpauseCondition = lock.newCondition();


    private ChangeEvent changeEvent = null;
    private EventListenerList carListenerList = new EventListenerList();

    private static final double REFRESH_RATE = 144.0;

    public AnimatedLight(int x, int y) {
        xPos = x;
        yPos = y;
        this.isPaused = false;
        rand = new Random();
        timeRemainingSec = rand.nextInt(11) + 5;
        terminate = false;
    }

    @Override
    public void run() {
        while (true) {
            if (terminate) {
                break;
            }

            lock.lock();
            try {
                while (isPaused) {
                    unpauseCondition.await();
                }
                try {
                    Thread.sleep((long) (1000.0 / REFRESH_RATE));
                } catch (InterruptedException e) {
                }

                timeRemainingSec -= (1.0 / REFRESH_RATE);

                if (timeRemainingSec < 0.01) {
                    switch (lightState) {
                        case GREEN:
                            lightState = LightState.YELLOW;
                            timeRemainingSec = 3.0;
                            break;
                        case YELLOW:
                            lightState = LightState.RED;
                            fireLightStateChanged();
                            timeRemainingSec = (double) rand.nextInt(6) + 5.0;
                            break;
                        case RED:
                            lightState = LightState.GREEN;
                            fireLightStateChanged();
                            timeRemainingSec = (double) rand.nextInt(7) + 1.0;
                            break;
                        default:
                            System.out.println("Default case on light switch happened. Should ever happen...");
                            timeRemainingSec = 1.0;
                            break;
                    }
                }
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }
    }

    public void pause() {
        while (!isPaused) {
            isPaused = true;
        }
    }

    public void unpause() {
        while (isPaused) {
            lock.lock();
            isPaused = false;
            unpauseCondition.signalAll();
            lock.unlock();
        }
    }

    public void terminate() {
        terminate = true;
        unpause();
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

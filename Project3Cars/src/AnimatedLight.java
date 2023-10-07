// TODO Document

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AnimatedLight implements Runnable {
    private int xPos;
    private int yPos;
    private LightState lightState = LightState.GREEN;
    private boolean isPaused, terminate;
    private double timeRemainingSec;
    Random rand;
    private final Lock lock = new ReentrantLock();
    private final Condition unpauseCondition = lock.newCondition();

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
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                timeRemainingSec -= 0.10;

                if (timeRemainingSec < 0.01) {
                    switch (lightState) {
                        case GREEN:
                            lightState = LightState.YELLOW;
                            timeRemainingSec = 3.0;
                            break;
                        case YELLOW:
                            lightState = LightState.RED;
                            timeRemainingSec = (double) rand.nextInt(3) + 3.0;
                            break;
                        case RED:
                            lightState = LightState.GREEN;
                            timeRemainingSec = (double) rand.nextInt(11) + 5.0;
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

}

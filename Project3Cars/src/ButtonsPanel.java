
/**
 * ButtonsPanel.java
 * Date: 10.10.23
 * @author Charles Kresho
 * Purpose: A panel that has all the buttons (and a timer) needed to run the CarPanel. Starts its own thread for the timer.
 */
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class ButtonsPanel extends JPanel implements Runnable {

    // Have default values of 1 car and 3 lights, and a state of stoped
    private int numCars = 1;
    private int numLights = 3;
    private boolean running = false;
    private boolean paused = false;

    // Event listener stuff
    private ChangeEvent changeEvent = null;
    private EventListenerList listenerList = new EventListenerList();

    // Button panel swing elements
    private JPanel topPanel, middlePanel, bottomPanel;
    private JButton startStopButton, pauseResumeButton, addCarButton, removeCarButton, addLightButton,
            removeLightButton;
    private JLabel clockLabel;

    /**
     * Maximum amount of cars in the simulation.
     */
    private static final int MAX_CARS = 3;

    /**
     * Maximum amount of lights in the simulation.
     */
    private static final int MAX_LIGHTS = 5;

    /**
     * Formatter for the current time stamp.
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm:ss a");

    /**
     * Create a new ButtonsPanel.
     */
    public ButtonsPanel() {

        // Create the clock label
        clockLabel = new JLabel(getCurrentTime());

        // Create the buttons
        startStopButton = new JButton("Start");
        startStopButton.addActionListener(new StartStopHandeler());

        pauseResumeButton = new JButton("Resume");
        pauseResumeButton.setEnabled(false);
        pauseResumeButton.addActionListener(new PauseResumeHandeler());

        addCarButton = new JButton("Car+");
        addCarButton.addActionListener(new AddCarHandeler());

        removeCarButton = new JButton("Car-");
        removeCarButton.addActionListener(new RemoveCarHandeler());

        addLightButton = new JButton("Light+");
        addLightButton.addActionListener(new AddLightHandeler());

        removeLightButton = new JButton("Light-");
        removeLightButton.addActionListener(new RemoveLightHandeler());

        // Set up everything
        GridLayout layout = new GridLayout(0, 1);
        layout.setHgap(5);
        layout.setVgap(5);
        setLayout(layout);

        topPanel = new JPanel();
        middlePanel = new JPanel();
        bottomPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        middlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        topPanel.add(clockLabel);
        middlePanel.add(startStopButton);
        middlePanel.add(pauseResumeButton);
        bottomPanel.add(addCarButton);
        bottomPanel.add(removeCarButton);
        bottomPanel.add(addLightButton);
        bottomPanel.add(removeLightButton);

        add(topPanel);
        add(middlePanel);
        add(bottomPanel);

        // Start the clock ticking
        Thread thread = new Thread(this, "Button Pannel Thread");
        thread.start();
    }

    @Override
    public void run() {
        // Let the clock tick forever and ever and ever...
        while (true) {
            try {
                // Updates every quarter second for slightly more accurate ticks to the system clock
                Thread.sleep(250);
            } catch (InterruptedException e) {
            }

            clockLabel.setText(getCurrentTime());
        }
    }

    /**
     * Helper method to get the current time as a string.
     * @return Current time as a formatted string.
     */
    private String getCurrentTime() {
        LocalDateTime date = LocalDateTime.now();
        return "Current time: " + date.format(TIME_FORMATTER);
    }

    /**
     * Get the number of cars in the simulation.
     * @return Number of cars in the simulation.
     */
    public int getNumCars() {
        return numCars;
    }

    /**
     * Get the number of lights in the simluation.
     * @return number of lights in the simulation.
     */
    public int getNumLights() {
        return numLights;
    }

    /**
     * Is the simulation paused.
     * @return if the simulation is paused or not.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Is the simulation running or stopped.
     * @return if the simulation is running or stopped.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Updates the pause button and the pause/running flags when the stop/start button is pressed.
     * Fires an update to listeners.
     */
    private class StartStopHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (running) {
                running = false;
                paused = false;
                startStopButton.setText("Start");
                pauseResumeButton.setText("Pause");
                pauseResumeButton.setEnabled(false);
            } else {
                running = true;
                paused = false;
                startStopButton.setText("Stop");
                pauseResumeButton.setText("Pause");
                pauseResumeButton.setEnabled(true);
            }

            fireStateChanged();
        }
    }

    /**
     * Pauses and unpauses the simulation.
     * Fire an update to listeners.
     */
    private class PauseResumeHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (paused) {
                paused = false;
                pauseResumeButton.setText("Pause");
            } else {
                paused = true;
                pauseResumeButton.setText("Resume");
            }

            fireStateChanged();
        }
    }

    /**
     * Increase the number of simulated cars, up to the max.
     * Fires an update to listeners.
     */
    private class AddCarHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numCars = Math.min(MAX_CARS, numCars + 1);
            fireStateChanged();
        }
    }

    /**
     * Decrease the number of simulated cars.
     * Fires an update to listeners.
     */
    private class RemoveCarHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numCars = Math.max(0, numCars - 1);
            fireStateChanged();
        }
    }

    /**
     * Increase the number of simulated lights, up to the max.
     * Fires an update to listeners.
     */
    private class AddLightHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numLights = Math.min(MAX_LIGHTS, numLights + 1);
            fireStateChanged();
        }
    }

    /**
     * Decrease the number of simulated lights.
     * Fires an update to listeners.
     */
    private class RemoveLightHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numLights = Math.max(0, numLights - 1);
            fireStateChanged();
        }
    }

    /*
     * The rest of this is event handling code copied from
     * DefaultBoundedRangeModel.
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
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

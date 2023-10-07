import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

// TODO Document
// TODO Add a ticking clock to the top of the panel (add an extra row for it)
public class ButtonsPanel extends JPanel {

    private int numCars = 3;
    private int numLights = 3;
    private boolean running = false;
    private boolean paused = false;
    private ChangeEvent changeEvent = null;
    private EventListenerList listenerList = new EventListenerList();

    private JPanel topPanel, bottomPanel;
    private JButton startStopButton, pauseResumeButton, addCarButton, removeCarButton, addLightButton,
            removeLightButton;

    private static final int MAX_CARS_AND_LIGHTS = 5;

    public ButtonsPanel() {
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
        bottomPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        topPanel.add(startStopButton);
        topPanel.add(pauseResumeButton);
        bottomPanel.add(addCarButton);
        bottomPanel.add(removeCarButton);
        bottomPanel.add(addLightButton);
        bottomPanel.add(removeLightButton);

        add(topPanel);
        add(bottomPanel);
    }

    public int getNumCars() {
        return numCars;
    }

    public int getNumLights() {
        return numLights;
    }

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

    private class AddCarHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numCars = Math.min(MAX_CARS_AND_LIGHTS, numCars + 1);
            fireStateChanged();
        }
    }

    private class RemoveCarHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numCars = Math.max(0, numCars - 1);
            fireStateChanged();
        }
    }

    private class AddLightHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numLights = Math.min(MAX_CARS_AND_LIGHTS, numLights + 1);
            fireStateChanged();
        }
    }

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

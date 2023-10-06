import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel {

    private int numCars;
    private int numLights;
    boolean running, paused;

    JButton startStopButton, pauseResumeButton, addCarButton, removeCarButton, addLightButton, removeLightButton;

    public ButtonsPanel() {
        numCars = 3;
        numLights = 3;
        running = false;
        paused = false;

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
        GridLayout layout = new GridLayout(0, 2);
        layout.setHgap(5);
        layout.setVgap(5);
        setLayout(layout);

        add(startStopButton);
        add(pauseResumeButton);
        add(addCarButton);
        add(removeCarButton);
        add(addLightButton);
        add(removeLightButton);
    }

    public int getNumCars() {
        return numCars;
    }

    public int getNumLights() {
        return numLights;
    }

    private class StartStopHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // TODO StartStopHandeler

            if (running) {
                running = false;
                paused = false;
                startStopButton.setText("Start");
                pauseResumeButton.setText("Pause");
                pauseResumeButton.setEnabled(false);
            }
            else {
                running = true;
                paused = false;
                startStopButton.setText("Stop");
                pauseResumeButton.setText("Pause");
                pauseResumeButton.setEnabled(true);
            }
        }
    }

    private class PauseResumeHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // TODO PauseResumeHandeler

            if (paused) {
                paused = false;
                pauseResumeButton.setText("Pause");
            }
            else {
                paused = true;
                pauseResumeButton.setText("Resume");
            }
        }
    }

    private class AddCarHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numCars++;
        }
    }

    private class RemoveCarHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numCars--;
        }
    }

    private class AddLightHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numLights++;
        }
    }

    private class RemoveLightHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            numLights--;
        }
    }
}

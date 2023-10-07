import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// TODO Document

public class CarsPanel extends JPanel implements ChangeListener {
    // Stores a list of all cars currently in the program
    private ArrayList<AnimatedCar> cars;
    private ArrayList<AnimatedLight> lights;
    private ButtonsPanel sourcePanel;

    public CarsPanel(ButtonsPanel source) {
        // TODO Finish CarsPanel constructor
        sourcePanel = source;
        sourcePanel.addChangeListener(this);

        cars = new ArrayList<AnimatedCar>();
        lights = new ArrayList<AnimatedLight>();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Lights and Cars are updated when needed
        // TODO Lights and Cars all pause and start when needed
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // For each car, paint it where it shows up on the panel
        for (int i = 0; i < cars.size(); i++) {
            AnimatedCar curCar = cars.get(i);
            g.drawImage(curCar.getCar(), curCar.getxPos(), curCar.getyPos(), this);
        }

        // For each light, paint a circle of the specified color where it shows up on the panel
        // TODO paint a line that indicates the location of each light???
        for (int i = 0; i < lights.size(); i++) {
            AnimatedLight curLight = lights.get(i);
            switch (curLight.getLightState()) {
                case GREEN:
                    g.setColor(Color.GREEN);
                    break;
                case RED:
                    g.setColor(Color.RED);
                    break;
                case YELLOW:
                    g.setColor(Color.YELLOW);
                    break;
                default:
                    System.out.println("Default case on light switch happened. Should ever happen...");
                    break;

            }
            g.fillOval(curLight.getxPos(), curLight.getyPos(), 10, 10);
        }
    }
}

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CarsPanel extends JPanel {
    // Stores a list of all cars currently in the program
    private ArrayList<AnimatedCar> cars;

    public CarsPanel() {
        // TODO Finish CarsPanel constructor

        cars = new ArrayList<AnimatedCar>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // For each car, paint it where it shows up on the panel
        for (int i = 0; i < cars.size(); i++) {
            AnimatedCar curCar = cars.get(i);
            g.drawImage(curCar.getCar(), curCar.getxPos(), curCar.getyPos(), this);
        }
    }
}


/**
 * Project3Runner.java
 * Date: 10.10.23
 * @author Charles Kresho
 * Purpose: Project3Runner sets up the inital state of the window, and sets everything spinning in motion.
 */
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Project3Runner implements ChangeListener {
    private CarsPanel cars;
    private ButtonsPanel buttons;

    private static JFrame frame;
    private JPanel mainPane;

    /**
     * Create a new window with buttons and cars and lights :D (after doing documentation for over 2 hours, I need a smile in my life)
     */
    public Project3Runner() {
        // Set up the panels
        buttons = new ButtonsPanel();
        cars = new CarsPanel(buttons);

        cars.addWindowListener(this);

        // Set up the window and add the panels to it
        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

        mainPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(buttons);
        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(cars);
        mainPane.add(Box.createGlue());

        // Make the window visible and start the program running.
        mainPane.setOpaque(true);
        frame.setContentPane(mainPane);

        frame.pack();
        frame.setVisible(true);

        Thread carThread = new Thread(cars, "Car Thread");
        carThread.start();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        frame.pack();
    }

    /**
     * For safety, call this as a new thread. Set up the GUI for the program.
     */
    private static void createAndShowGUI() {
        // Attempt to match the look and feel of the window.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Why do anything except use the default?
        }

        frame = new JFrame("Cars and Stoplights");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        new Project3Runner();

    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
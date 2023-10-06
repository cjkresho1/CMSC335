import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.Dimension;

public class Project3Runner {
    private CarsPanel cars;
    private ButtonsPanel buttons;

    private JPanel mainPane;

    public Project3Runner() {
        buttons = new ButtonsPanel();
        cars = new CarsPanel();

        mainPane = new JPanel();
        mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.PAGE_AXIS));

        mainPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(buttons);
        mainPane.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPane.add(cars);
        mainPane.add(Box.createGlue());
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting look and feel");
        }

        JFrame frame = new JFrame("Cars and Stoplights");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Project3Runner runner = new Project3Runner();
        runner.mainPane.setOpaque(true);
        frame.setContentPane(runner.mainPane);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
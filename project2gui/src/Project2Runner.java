
/**
 * ProjectRunner.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: To provide a command line interface for the user to calculate the area/volume of various shapes.
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Project2Runner implements ItemListener {
    JPanel cards;
    private String[] shapeOptions = { "Circle", "Rectangle", "Square", "Triangle", "Sphere", "Cube", "Cone",
            "Cylinder", "Torus" };
    private JTextField radiusField;
    private JTextField lengthField;
    private JTextField heightField;
    private JTextField widthField;
    private JTextField majorRadiusField;
    private JTextField minorRadiusField;

    public void addComponentToPane(Container pane) {
        JPanel comboBoxPane = new JPanel();
        JComboBox<String> cb = new JComboBox<String>(shapeOptions);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);

        

        JButton calculateButton = new JButton("Create Shape");
        calculateButton.addActionListener(new CalculateHandeler());

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ExitHandler());

        radiusField = new JTextField(10);
        // Will be centered and far right in its location
        JLabel radiusFieldLabel = new JLabel("Radius: ");
        radiusFieldLabel.setVerticalAlignment(JLabel.CENTER);
        radiusFieldLabel.setHorizontalAlignment(JLabel.RIGHT);

        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);

        //***** Create the Circle Card *****//
        JPanel circleCard = new JPanel();
        circleCard.setLayout(new GridBagLayout());

        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;
        circleCard.add(radiusFieldLabel, cons);

        cons.gridx = 1;
        circleCard.add(radiusField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        circleCard.add(calculateButton, cons);

        cons.gridx = 1;
        circleCard.add(exitButton, cons);

         
        //***** Create the Rectangle Card *****//
        JPanel rectangleCard = new JPanel();
        rectangleCard.add(new JTextField("TextField", 20));
         
        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(circleCard, shapeOptions[0]);
        cards.add(rectangleCard, shapeOptions[1]);
         
        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }

    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, (String) evt.getItem());
    }

    private static void createAndShowGUI() {
        // Create the window
        JFrame frame = new JFrame("Java Shapes GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the CardLayout
        Project2Runner shapesLayout = new Project2Runner();
        shapesLayout.addComponentToPane(frame.getContentPane());

        // Display the window
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class CalculateHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
        }
    }

    private static class ExitHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Being 100% transparent, this was in the tutorial for making a card layout, so I'm using it for safety
        // I know it has something to do with allowing the thread to dispatch the job when it's ready
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

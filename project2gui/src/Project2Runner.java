
/**
 * Project2Runner.java 
 * Date: 09.12.2023
 * @author Charles Kresho
 * Purpose: To provide a GUI interface for the user to calculate the area/volume of various shapes.
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
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Project2Runner implements ItemListener {
    private static JFrame window;
    private JPanel cards;
    private static final String[] shapeOptions = { "Circle", "Rectangle", "Square", "Triangle", "Sphere", "Cube",
            "Cone", "Cylinder", "Torus" };
    // Binary flag system, where the least significant bit is radius, second least is length, etc. 
    // An "on" flag indicates that the respective shape uses that field in its calculation
    private static final int[] shapeFieldFlags = { 0b000001, 0b000110, 0b000010, 0b000110, 0b000001, 0b001110, 0b000101,
            0b000101, 0b110000 };

    // Global JPanel items so they can be used in different methods to position and reposition
    private JPanel circleCard, rectangleCard, squareCard, triangleCard, sphereCard, cubeCard, coneCard, cylinderCard,
            torusCard;
    private JTextField radiusField, lengthField, heightField, widthField, majorRadiusField, minorRadiusField;
    private JLabel shapeAttributeLabel, radiusFieldLabel, lengthFieldLabel, minorRadiusLabel, majorRadiusLabel,
            widthFieldLabel, heightFieldLabel;
    private JButton calculateButton, exitButton;

    private String curCard = "Circle";

    
    private void addComponentToPane(Container pane) {

        // Create a Drop Down Box to navigate through the different cards.
        JPanel comboBoxPane = new JPanel();
        JComboBox<String> cb = new JComboBox<String>(shapeOptions);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);

        // Create buttons for calculate and exit, used on all panels
        calculateButton = new JButton("Create Shape");
        calculateButton.addActionListener(new CalculateHandeler());

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ExitHandler());

        // Create all text fields and labels, for use on all panels

        radiusField = new JTextField(10);
        radiusFieldLabel = new JLabel("Radius: ");
        radiusFieldLabel.setVerticalAlignment(JLabel.CENTER);
        radiusFieldLabel.setHorizontalAlignment(JLabel.RIGHT);

        lengthField = new JTextField(10);
        lengthFieldLabel = new JLabel("Length: ");
        lengthFieldLabel.setVerticalAlignment(JLabel.CENTER);
        lengthFieldLabel.setHorizontalAlignment(JLabel.RIGHT);

        heightField = new JTextField(10);
        heightFieldLabel = new JLabel("Height: ");
        heightFieldLabel.setVerticalAlignment(JLabel.CENTER);
        heightFieldLabel.setHorizontalAlignment(JLabel.RIGHT);

        widthField = new JTextField(10);
        widthFieldLabel = new JLabel("Width: ");
        widthFieldLabel.setVerticalAlignment(JLabel.CENTER);
        widthFieldLabel.setHorizontalAlignment(JLabel.RIGHT);

        majorRadiusField = new JTextField(10);
        majorRadiusLabel = new JLabel("Major Radius: ");
        majorRadiusLabel.setVerticalAlignment(JLabel.CENTER);
        majorRadiusLabel.setHorizontalAlignment(JLabel.RIGHT);

        minorRadiusField = new JTextField(10);
        minorRadiusLabel = new JLabel("Minor Radius: ");
        minorRadiusLabel.setVerticalAlignment(JLabel.CENTER);
        minorRadiusLabel.setHorizontalAlignment(JLabel.RIGHT);

        shapeAttributeLabel = new JLabel("Click to display the shape!");
        shapeAttributeLabel.setVerticalAlignment(JLabel.CENTER);
        shapeAttributeLabel.setHorizontalAlignment(JLabel.RIGHT);

        // Create all the cards
        circleCard = new JPanel();
        circleCard.setLayout(new GridBagLayout());

        rectangleCard = new JPanel();
        rectangleCard.setLayout(new GridBagLayout());

        squareCard = new JPanel();
        squareCard.setLayout(new GridBagLayout());

        triangleCard = new JPanel();
        triangleCard.setLayout(new GridBagLayout());

        sphereCard = new JPanel();
        sphereCard.setLayout(new GridBagLayout());

        cubeCard = new JPanel();
        cubeCard.setLayout(new GridBagLayout());

        coneCard = new JPanel();
        coneCard.setLayout(new GridBagLayout());

        cylinderCard = new JPanel();
        cylinderCard.setLayout(new GridBagLayout());

        torusCard = new JPanel();
        torusCard.setLayout(new GridBagLayout());

        // Draw the default card
        drawCircleCard();

        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(circleCard, shapeOptions[0]);
        cards.add(rectangleCard, shapeOptions[1]);
        cards.add(squareCard, shapeOptions[2]);
        cards.add(triangleCard, shapeOptions[3]);
        cards.add(sphereCard, shapeOptions[4]);
        cards.add(cubeCard, shapeOptions[5]);
        cards.add(coneCard, shapeOptions[6]);
        cards.add(cylinderCard, shapeOptions[7]);
        cards.add(torusCard, shapeOptions[8]);

        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }

    @Override
    public void itemStateChanged(ItemEvent evt) {
        // Whenever the dropbox selection changes, show the selected card
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, (String) evt.getItem());

        // Determine the newly selected card...
        curCard = (String) evt.getItem();
        shapeAttributeLabel.setText("Click to display the shape!");
        int curShapeIndex = -1;
        for (int i = 0; i < shapeOptions.length; i++) {
            if (curCard.equalsIgnoreCase(shapeOptions[i])) {
                curShapeIndex = i;
                break;
            }
        }
        // And redraw the selected card
        switch (curShapeIndex) {
        case 0: // Circle
            drawCircleCard();
            break;
        case 1: // Rectangle
            drawRectangleCard();
            break;
        case 2: // Square
            DrawSquareCard();
            break;
        case 3: // Triangle
            drawTriangleCard();
            break;
        case 4: // Sphere
            drawSphereCard();
            break;
        case 5: // Cube
            drawCubecard();
            break;
        case 6: // Cone
            drawConeCard();
            break;
        case 7: // Cylinder
            drawCylinderCard();
            break;
        case 8: // Torus
            drawTorusCard();
            break;
        default: //Should never happen
            JOptionPane.showMessageDialog(window, "Shape selection error; please contact a developer.", "Type Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Resize the window to be the minimum possible size
        window.pack();
    }

    /**************************************REDRAW HELPER METHOD SECTION*************************************/
    /*****Methods here reposition the components for the card, in case they got stolen by another card.*****/
    private void drawCircleCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        circleCard.add(radiusFieldLabel, cons);

        cons.gridx = 1;
        circleCard.add(radiusField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 2;
        circleCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 2;
        circleCard.add(calculateButton, cons);

        cons.gridx = 1;
        circleCard.add(exitButton, cons);
    }

    private void drawRectangleCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        rectangleCard.add(lengthFieldLabel, cons);

        cons.gridx = 1;
        rectangleCard.add(lengthField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        rectangleCard.add(heightFieldLabel, cons);

        cons.gridx = 1;
        rectangleCard.add(heightField, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cons.gridwidth = 2;
        rectangleCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 3;
        rectangleCard.add(calculateButton, cons);

        cons.gridx = 1;
        rectangleCard.add(exitButton, cons);
    }

    private void DrawSquareCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        squareCard.add(lengthFieldLabel, cons);

        cons.gridx = 1;
        squareCard.add(lengthField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 2;
        squareCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 2;
        squareCard.add(calculateButton, cons);

        cons.gridx = 1;
        squareCard.add(exitButton, cons);
    }

    private void drawTriangleCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        triangleCard.add(lengthFieldLabel, cons);

        cons.gridx = 1;
        triangleCard.add(lengthField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        triangleCard.add(heightFieldLabel, cons);

        cons.gridx = 1;
        triangleCard.add(heightField, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cons.gridwidth = 2;
        triangleCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 3;
        triangleCard.add(calculateButton, cons);

        cons.gridx = 1;
        triangleCard.add(exitButton, cons);
    }

    private void drawSphereCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        sphereCard.add(radiusFieldLabel, cons);

        cons.gridx = 1;
        sphereCard.add(radiusField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 2;
        sphereCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 2;
        sphereCard.add(calculateButton, cons);

        cons.gridx = 1;
        sphereCard.add(exitButton, cons);
    }

    private void drawCubecard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        cubeCard.add(lengthFieldLabel, cons);

        cons.gridx = 1;
        cubeCard.add(lengthField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        cubeCard.add(heightFieldLabel, cons);

        cons.gridx = 1;
        cubeCard.add(heightField, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cubeCard.add(widthFieldLabel, cons);

        cons.gridx = 1;
        cubeCard.add(widthField, cons);

        cons.gridx = 0;
        cons.gridy = 3;
        cons.gridwidth = 2;
        cubeCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 4;
        cubeCard.add(calculateButton, cons);

        cons.gridx = 1;
        cubeCard.add(exitButton, cons);
    }

    private void drawConeCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        coneCard.add(radiusFieldLabel, cons);

        cons.gridx = 1;
        coneCard.add(radiusField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        coneCard.add(heightFieldLabel, cons);

        cons.gridx = 1;
        coneCard.add(heightField, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cons.gridwidth = 2;
        coneCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 3;
        coneCard.add(calculateButton, cons);

        cons.gridx = 1;
        coneCard.add(exitButton, cons);
    }

    private void drawCylinderCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;

        cons.gridx = 0;
        cons.gridy = 0;
        cylinderCard.add(radiusFieldLabel, cons);

        cons.gridx = 1;
        cylinderCard.add(radiusField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        cylinderCard.add(heightFieldLabel, cons);

        cons.gridx = 1;
        cylinderCard.add(heightField, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cons.gridwidth = 2;
        cylinderCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 3;
        cylinderCard.add(calculateButton, cons);

        cons.gridx = 1;
        cylinderCard.add(exitButton, cons);
    }

    private void drawTorusCard() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.insets = new Insets(5, 5, 5, 5);
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.BOTH;
        //***** Create the Torus Card *****//
        cons.gridx = 0;
        cons.gridy = 0;
        torusCard.add(minorRadiusLabel, cons);

        cons.gridx = 1;
        torusCard.add(minorRadiusField, cons);

        cons.gridx = 0;
        cons.gridy = 1;
        torusCard.add(majorRadiusLabel, cons);

        cons.gridx = 1;
        torusCard.add(majorRadiusField, cons);

        cons.gridx = 0;
        cons.gridy = 2;
        cons.gridwidth = 2;
        torusCard.add(shapeAttributeLabel, cons);
        cons.gridwidth = 1;

        cons.gridx = 0;
        cons.gridy = 3;
        torusCard.add(calculateButton, cons);

        cons.gridx = 1;
        torusCard.add(exitButton, cons);
    }

    private static void createAndShowGUI() {
        // Create the window
        window = new JFrame("Java Shapes GUI");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the CardLayout
        Project2Runner shapesLayout = new Project2Runner();
        shapesLayout.addComponentToPane(window.getContentPane());

        // Display the window
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private class CalculateHandeler implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            // Determine which shape is currently active
            int curShapeIndex = -1;
            for (int i = 0; i < shapeOptions.length; i++) {
                if (curCard.equalsIgnoreCase(shapeOptions[i])) {
                    curShapeIndex = i;
                    break;
                }
            }

            // Get the text from all field boxes.
            String radiusString = radiusField.getText();
            String lengthString = lengthField.getText();
            String heightString = heightField.getText();
            String widthString = widthField.getText();
            String majorRString = majorRadiusField.getText();
            String minorRString = minorRadiusField.getText();

            double radius = 0;
            double length = 0;
            double height = 0;
            double width = 0;
            double majorR = 0;
            double minorR = 0;
            boolean validFields = true;

            // Try/catch clause envelops entire switch case to avoid extra printing of status messages from within.
            try {

                /*****These conditionals use the flags set for each shape to determine  *****/
                /*****which fields are required by the shape.                           *****/
                if ((shapeFieldFlags[curShapeIndex] & 0b000001) == 0b000001) {
                    try {
                        radius = Double.parseDouble(radiusString);
                        if (radius < 0) {
                            throw new NegativeAttributeException("Radius cannot be negative.");
                        }
                    }
                    catch (NegativeAttributeException exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, "Radius must be a valid number.", "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                if ((shapeFieldFlags[curShapeIndex] & 0b000010) == 0b000010) {
                    try {
                        length = Double.parseDouble(lengthString);
                        if (length < 0) {
                            throw new NegativeAttributeException("Length cannot be negative.");
                        }
                    }
                    catch (NegativeAttributeException exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, "Length must be a valid number.", "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                if ((shapeFieldFlags[curShapeIndex] & 0b000100) == 0b000100) {
                    try {
                        height = Double.parseDouble(heightString);
                        if (height < 0) {
                            throw new NegativeAttributeException("Height cannot be negative.");
                        }
                    }
                    catch (NegativeAttributeException exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, "Height must be a valid number.", "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                if ((shapeFieldFlags[curShapeIndex] & 0b001000) == 0b001000) {
                    try {
                        width = Double.parseDouble(widthString);
                        if (width < 0) {
                            throw new NegativeAttributeException("Width cannot be negative.");
                        }
                    }
                    catch (NegativeAttributeException exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, "Width must be a valid number.", "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                if ((shapeFieldFlags[curShapeIndex] & 0b010000) == 0b010000) {
                    try {
                        majorR = Double.parseDouble(majorRString);
                        if (majorR < 0) {
                            throw new NegativeAttributeException("Major Radius cannot be negative.");
                        }
                    }
                    catch (NegativeAttributeException exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, "Major Radius must be a valid number.", "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

                if ((shapeFieldFlags[curShapeIndex] & 0b100000) == 0b100000) {
                    try {
                        minorR = Double.parseDouble(minorRString);
                        if (minorR < 0) {
                            throw new NegativeAttributeException("Minor Radius cannot be negative.");
                        }
                    }
                    catch (NegativeAttributeException exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    catch (Exception exception) {
                        validFields = false;
                        JOptionPane.showMessageDialog(window, "Minor Radius must be a valid number.", "Type Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                // If any fields were not valid, exit the calculation without doing anything.
                if (!validFields) {
                    return;
                }

                // Prepare a Dialog box to hold the shape rendering.
                JDialog shapeFrame = new JDialog(window, false);
                Shape curShape = null;

                // Render the required shape
                switch (curShapeIndex) {
                case 0: // Circle
                    curShape = new Circle(radius);
                    break;
                case 1: // Rectangle
                    curShape = new Rectangle(length, height);
                    break;
                case 2: // Square
                    curShape = new Square(length);
                    break;
                case 3: // Triangle
                    curShape = new Triangle(length, height);
                    break;
                case 4: // Sphere
                    curShape = new Sphere(radius);
                    break;
                case 5: // Cube
                    curShape = new Cube(length, width, height);
                    break;
                case 6: // Cone
                    curShape = new Cone(radius, height);
                    break;
                case 7: // Cylinder
                    curShape = new Cylinder(radius, height);
                    break;
                case 8: // Torus
                    curShape = new Torus(majorR, minorR);
                    break;
                default: //Should never happen
                    JOptionPane.showMessageDialog(window, "Shape selection error; please contact a developer.",
                            "Type Error", JOptionPane.ERROR_MESSAGE);
                }

                // Place the shape in the dialog box, size and position the box. 
                shapeAttributeLabel.setText(curShape.toString());
                shapeFrame.setTitle(curShape.getShapeName());
                shapeFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                shapeFrame.setSize(800, 800);
                shapeFrame.setLocationRelativeTo(window);
                shapeFrame.setLocation(shapeFrame.getX() + (int) (window.getSize().getWidth() / 2)
                        + (int) shapeFrame.getSize().getWidth() / 2, curShapeIndex);
                shapeFrame.add(curShape);
                shapeFrame.setVisible(true);
            }
            catch (InvalidTorusAttributesException exception) {
                JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException exception) {
                JOptionPane.showMessageDialog(window, exception.getMessage(), "Type Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class ExitHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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


/**
 * ProjectRunner.java 
 * Date: 08.29.2023
 * @author Charles Kresho
 * Purpose: To provide a command line interface for the user to calculate the area/volume of various shapes.
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Project1Runner {
    private static Scanner scan;
    private static String[] shapeOptions = { "Circle", "Rectangle", "Square", "Triangle", "Sphere", "Cube", "Cone",
            "Cylinder", "Torus" };

    public static void main(String[] args) {

        scan = new Scanner(System.in);

        System.out.println("*********Welcome to the Java OO Shapes Program **********");

        boolean keepRunning = true;
        while (keepRunning) {
            // Print the menu, get a menu selection, then create the shape selected.

            printMenu();
            System.out.println();
            int userChoice = getUserInt("Enter a menu choice: ");
            System.out.println();
            Shape curShape = null;

            // Try/catch clause envelops entire switch case to avoid extra printing of status messages from within.
            try {
                switch (userChoice) {
                case 1: // Circle
                    curShape = new Circle(getShapeAttribute("radius", "Circle"));
                    break;
                case 2: // Rectangle
                    curShape = new Rectangle(getShapeAttribute("length", "Rectangle"),
                            getShapeAttribute("height", "Rectangle"));
                    break;
                case 3: // Square
                    curShape = new Square(getShapeAttribute("length", "Square"));
                    break;
                case 4: // Triangle
                    curShape = new Triangle(getShapeAttribute("length", "Triangle"),
                            getShapeAttribute("height", "Triangle"));
                    break;
                case 5: // Sphere
                    curShape = new Sphere(getShapeAttribute("radius", "Sphere"));
                    break;
                case 6: // Cube
                    curShape = new Cube(getShapeAttribute("length", "Cube"), getShapeAttribute("width", "Cube"),
                            getShapeAttribute("height", "Cube"));
                    break;
                case 7: // Cone
                    curShape = new Cone(getShapeAttribute("radius", "Cone"), getShapeAttribute("height", "Cone"));
                    break;
                case 8: // Cylinder
                    curShape = new Cylinder(getShapeAttribute("radius", "Cylinder"),
                            getShapeAttribute("height", "Cylinder"));
                    break;
                case 9: // Torus
                    curShape = new Torus(getShapeAttribute("major radius", "Torus"),
                            getShapeAttribute("minor radius", "Torus"));
                    break;
                case 10: // Exit
                    keepRunning = false;
                    break;
                }

                // If a shape was created, print the information regarding its area/volume.
                if (curShape != null) {
                    System.out.println(curShape.toString());
                }
                // If an invalid selection was made, keepRunning == true && curShape == null
                else if (keepRunning) {
                    System.out.println("Not a menu selection, please try again.");
                }
            }
            catch (NegativeAttributeException e) {
                System.out.println(e.getMessage());
                curShape = null;
            }
            catch (InvalidTorusAttributesException e) {
                System.out.println(e.getMessage());
                curShape = null;
            }

            // Extra prompt for if the user wants to exit; changes prompt on invalid answer.
            if (keepRunning) {
                String prompt = "Would you like to continue? (Y or N): ";
                while (true) {

                    String userResponse = getUserString(prompt);
                    System.out.println();
                    if (userResponse.length() > 0) {
                        userResponse = userResponse.substring(0, 1);
                        if (userResponse.equalsIgnoreCase("Y")) {
                            break;
                        }

                        if (userResponse.equalsIgnoreCase("N")) {
                            keepRunning = false;
                            break;
                        }
                    }
                    prompt = "Sorry I don't understand. Enter Y or N: ";
                }
            }

            System.out.println();
        }

        // On exit, print message with time/date
        System.out.print("Thanks for using the program. ");
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter monthDay = DateTimeFormatter.ofPattern("MMMM dd");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("h:mm a");
        System.out.printf("Today is %s at %s.", date.format(monthDay), date.format(time));
        System.out.println();
        scan.close();
    }

    private static void printMenu() {
        System.out.println("Select from the menu below: ");
        for (int i = 0; i < shapeOptions.length; i++) {
            System.out.println(String.format("%d. Construct a %s", (i + 1), shapeOptions[i]));
        }
        System.out.println(String.format("%d. Exit the program", (shapeOptions.length + 1)));
    }

    /**
     * Get a String from the user input
     * @param prompt - The prompt to print to the user.
     * @return a valid String from the user
     */
    public static String getUserString(String prompt) {
        System.out.print(prompt);
        String userInput = scan.nextLine();

        return userInput;
    }

    /**
     * Get a valid int from the user input. Loops until success
     * 
     * @param prompt - The prompt to print to the user.
     * @return a valid int from the user
     */
    private static int getUserInt(String prompt) {
        boolean validInput = false;
        int userInteger = 0;
        while (!validInput) {
            try {
                System.out.print(prompt);
                String userInput = scan.nextLine();
                userInteger = Integer.parseInt(userInput);

                validInput = true;
            }
            catch (Exception NumberFormatException) {
                System.out.println("Not a valid integer, try again");
                validInput = false;
            }
        }

        return userInteger;
    }

    /**
     * Get a valid double from the user input. Loops until success
     * 
     * @param prompt - The prompt to print to the user.
     * @return a valid double from the user
     */
    private static double getUserDouble(String prompt) {
        boolean validInput = false;
        double userDouble = 0.0;
        while (!validInput) {
            try {
                System.out.print(prompt);
                String userInput = scan.nextLine();
                userDouble = Double.parseDouble(userInput);

                validInput = true;
            }
            catch (Exception e) {
                System.out.println("Not a valid number, try again");
                validInput = false;
            }
        }

        return userDouble;
    }

    /**
     * Allows for easy code reuse for getting the value of shape attributes.
     * @param attribute name of the attribute to get
     * @param shapeName name of the shape
     * @return value of the attribute from the user
     * @throws NegativeAttributeException if the attribute is neagative
     */
    private static double getShapeAttribute(String attribute, String shapeName) throws NegativeAttributeException {
        double attr = getUserDouble(String.format("What is the %s of the %s: ", attribute, shapeName));
        if (attr < 0) {
            throw new NegativeAttributeException(String.format("%s cannot be negative.", attribute));
        }
        return attr;
    }
}

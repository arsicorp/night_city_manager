import java.util.Scanner;

/**
 * Main class - Entry for Night City Manager application
 * This is where the program starts running
 *
 * @author YearUp Student Arslan
 * @version 1.0
 */
public class Main {

    /**
     * Main method - this is where Java starts executing
     */
    public static void main(String[] args) {
        // create Scanner for reading user input
        // using try with resources to ensure Scanner is closed properly
        try (Scanner scanner = new Scanner(System.in)) {

            // show the cool welcome screen
            DisplayHelper.showWelcomeScreen();

            // create and start the home screen
            // this will keep running until user chooses to exit
            HomeScreen homeScreen = new HomeScreen(scanner);
            homeScreen.show();

        } catch (Exception e) {
            // if anything goes wrong, show error and exit nicely
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║              CRITICAL SYSTEM ERROR                    ║");
            System.out.println("║  The application encountered an unexpected problem.   ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            System.out.println("\nError details: " + e.getMessage());
            System.out.println("\nIf this keeps happening, contact support or check your");
            System.out.println("transactions.csv file for corruption.");

            // TODO: Could add error logging to a file here for debugging

            e.printStackTrace();  // print full error for debugging
        }

        System.out.println("\n  Program terminated. Goodbye, choom!\n");
    }
}
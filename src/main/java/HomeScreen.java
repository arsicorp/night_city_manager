import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * HomeScreen handles the main menu and its operations
 * this is where users add deposits and payments
 */
public class HomeScreen {
    private final Scanner scanner;
    private boolean running;
    public HomeScreen(Scanner scanner) {
        this.scanner = scanner;
        this.running = true;
    }

    /**
     * main loop for the home screen
     * keeps showing menu until user exits
     */
    public void show() {
        while (running) {
            DisplayHelper.showHomeScreen();
            System.out.print("  Enter your choice: ");
            String choice = scanner.nextLine();

            handleHomeChoice(choice);
        }
    }

    /**
     * processes the user's menu choice
     */
    private void handleHomeChoice(String choice) {
        // validates the input first
        if (!InputValidator.isValidMenuChoice(choice, "D", "P", "L", "X")) {
            DisplayHelper.pressEnterToContinue();
            return;
        }
        // converts to uppercase to handle both lowercase and uppercase input
        String normalizedChoice = choice.trim().toUpperCase();

        switch (normalizedChoice) {
            case "D":
                addDeposit();
                break;
            case "P":
                makePayment();
                break;
            case "L":
                // goes to ledger screen (we will implement this in next module)
                LedgerScreen ledgerScreen = new LedgerScreen(scanner);
                ledgerScreen.show();
                break;
            case "X":
                exitApplication();
                break;
            default:
                DisplayHelper.showErrorMessage("Something went wrong!");
                break;
        }
    }

    /**
     * handles adding a deposit (money coming in)
     */
    private void addDeposit() {
        DisplayHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║            ADD DEPOSIT - LOG INCOMING EDDIES               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // gets date
        String dateInput;
        do {
            System.out.print("  Enter date (yyyy-MM-dd) or press ENTER for today: ");
            dateInput = scanner.nextLine().trim();
            // if empty, uses today's date
            if (dateInput.isEmpty()) {
                dateInput = LocalDate.now().toString();
                System.out.println("  Using today's date: " + dateInput);
                break;
            }
        } while (!InputValidator.isValidDate(dateInput));

        // gets time
        String timeInput;
        do {
            System.out.print("  Enter time (HH:mm:ss) or press ENTER for current time: ");
            timeInput = scanner.nextLine().trim();
            // if empty, uses current time
            if (timeInput.isEmpty()) {
                timeInput = LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
                System.out.println("  Using current time: " + timeInput);
                break;
            }
        } while (!InputValidator.isValidTime(timeInput));

        // gets description
        String description;
        do {
            System.out.print("  Enter description (what's this deposit for?): ");
            description = scanner.nextLine().trim();
        } while (!InputValidator.isValidDescription(description));

        // gets vendor (who is paying you?)
        String vendor;
        do {
            System.out.print("  Enter vendor/source (who's paying?): ");
            vendor = scanner.nextLine().trim();
        } while (!InputValidator.isValidVendor(vendor));

        // gets amount
        String amountInput;
        double amount;
        do {
            System.out.print("  Enter amount ($): ");
            amountInput = scanner.nextLine().trim();
        } while (!InputValidator.isValidDepositAmount(amountInput));
        amount = Double.parseDouble(amountInput);

        // shows summary and confirmation
        System.out.println("\n  ─────────────────────────────────────────────────────────");
        System.out.println("  DEPOSIT SUMMARY:");
        System.out.println("  Date:        " + dateInput);
        System.out.println("  Time:        " + timeInput);
        System.out.println("  Description: " + description);
        System.out.println("  Vendor:      " + vendor);
        System.out.println("  Amount:      $" + String.format("%.2f", amount));
        System.out.println("  ─────────────────────────────────────────────────────────");

        System.out.print("\n  Save this deposit? (Y/N): ");
        String confirm = scanner.nextLine();

        if (InputValidator.isYes(confirm)) {
            // creates the transaction object
            Transaction transaction = new Transaction(
                    LocalDate.parse(dateInput),
                    LocalTime.parse(timeInput),
                    description,
                    vendor,
                    amount  // deposit is positive
            );
            // saves to the file
            FileManager.writeTransaction(transaction);
            DisplayHelper.showSuccessMessage("Deposit recorded! Eddies added to your account.");
        } else {
            DisplayHelper.showWarningMessage("Deposit cancelled. No changes made.");
        }
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * handles making a payment (money going out)
     */
    private void makePayment() {
        DisplayHelper.clearScreen();
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           MAKE PAYMENT - LOG OUTGOING EDDIES               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // gets date
        String dateInput;
        do {
            System.out.print("  Enter date (yyyy-MM-dd) or press ENTER for today: ");
            dateInput = scanner.nextLine().trim();

            if (dateInput.isEmpty()) {
                dateInput = LocalDate.now().toString();
                System.out.println("  Using today's date: " + dateInput);
                break;
            }
        } while (!InputValidator.isValidDate(dateInput));

        // gets time
        String timeInput;
        do {
            System.out.print("  Enter time (HH:mm:ss) or press ENTER for current time: ");
            timeInput = scanner.nextLine().trim();

            if (timeInput.isEmpty()) {
                timeInput = LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
                System.out.println("  Using current time: " + timeInput);
                break;
            }
        } while (!InputValidator.isValidTime(timeInput));

        // gets description
        String description;
        do {
            System.out.print("  Enter description (what did you buy/pay for?): ");
            description = scanner.nextLine().trim();
        } while (!InputValidator.isValidDescription(description));

        // gets vendor (who are you paying?)
        String vendor;
        do {
            System.out.print("  Enter vendor (who are you paying?): ");
            vendor = scanner.nextLine().trim();
        } while (!InputValidator.isValidVendor(vendor));

        // gets amount
        String amountInput;
        double amount;
        do {
            System.out.print("  Enter amount ($): ");
            amountInput = scanner.nextLine().trim();
        } while (!InputValidator.isValidPaymentAmount(amountInput));
        amount = Double.parseDouble(amountInput);

        // shows summary and confirmation
        System.out.println("\n  ─────────────────────────────────────────────────────────");
        System.out.println("  PAYMENT SUMMARY:");
        System.out.println("  Date:        " + dateInput);
        System.out.println("  Time:        " + timeInput);
        System.out.println("  Description: " + description);
        System.out.println("  Vendor:      " + vendor);
        System.out.println("  Amount:      $" + String.format("%.2f", amount));
        System.out.println("  ─────────────────────────────────────────────────────────");

        System.out.print("\n  Confirm this payment? (Y/N): ");
        String confirm = scanner.nextLine();

        if (InputValidator.isYes(confirm)) {
            // creates the transaction object
            // make amount negative for payments!
            Transaction transaction = new Transaction(
                    LocalDate.parse(dateInput),
                    LocalTime.parse(timeInput),
                    description,
                    vendor,
                    -amount  // payment is negative (notice the minus sign)
            );
            // saves to file
            FileManager.writeTransaction(transaction);
            DisplayHelper.showSuccessMessage("Payment recorded! Eddies deducted from your account.");
        } else {
            DisplayHelper.showWarningMessage("Payment cancelled. No changes made.");
        }
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * exits the application
     */
    private void exitApplication() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║     Disconnecting from Night City Manager...         ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║        Thanks for using the system, choom!           ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ║              Stay safe in Night City!                ║");
        System.out.println("  ║                                                      ║");
        System.out.println("  ╚══════════════════════════════════════════════════════╝\n");

        // TODO: Maybe add option to create final backup before exiting?

        running = false;
    }
}
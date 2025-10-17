import java.util.List;
// DisplayHelper does all the visual output for my Night City Manager
public class DisplayHelper {
    // colors using ANSI escape codes (works in most terminals)
    // if your terminal doesn't support colors, these just won't show
    private static final String CYAN = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";

    // Shows the welcome screen when app starts
    public static void showWelcomeScreen() {
        clearScreen();

        System.out.println(CYAN + BOLD);
        System.out.println("╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                      ║");
        System.out.println("║                ███╗   ██╗██╗ ██████╗ ██╗  ██╗████████╗               ║");
        System.out.println("║                ████╗  ██║██║██╔════╝ ██║  ██║╚══██╔══╝               ║");
        System.out.println("║                ██╔██╗ ██║██║██║  ███╗███████║   ██║                  ║");
        System.out.println("║                ██║╚██╗██║██║██║   ██║██╔══██║   ██║                  ║");
        System.out.println("║                ██║ ╚████║██║╚██████╔╝██║  ██║   ██║                  ║");
        System.out.println("║                ╚═╝  ╚═══╝╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝                  ║");
        System.out.println("║                                                                      ║");
        System.out.println("║                     ██████╗██╗████████╗██╗   ██╗                     ║");
        System.out.println("║                    ██╔════╝██║╚══██╔══╝╚██╗ ██╔╝                     ║");
        System.out.println("║                    ██║     ██║   ██║    ╚████╔╝                      ║");
        System.out.println("║                    ██║     ██║   ██║     ╚██╔╝                       ║");
        System.out.println("║                    ╚██████╗██║   ██║      ██║                        ║");
        System.out.println("║                     ╚═════╝╚═╝   ╚═╝      ╚═╝                        ║");
        System.out.println("║                                                                      ║");
        System.out.println("║    ███╗   ███╗ █████╗ ███╗   ██╗ █████╗  ██████╗ ███████╗██████╗     ║");
        System.out.println("║    ████╗ ████║██╔══██╗████╗  ██║██╔══██╗██╔════╝ ██╔════╝██╔══██╗    ║");
        System.out.println("║    ██╔████╔██║███████║██╔██╗ ██║███████║██║  ███╗█████╗  ██████╔╝    ║");
        System.out.println("║    ██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║   ██║██╔══╝  ██╔══██╗    ║");
        System.out.println("║    ██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╔╝███████╗██║  ██║    ║");
        System.out.println("║    ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ║");
        System.out.println("║                                                                      ║");
        System.out.println("║                   ~ Financial Tracking System v1.0 ~                 ║");
        System.out.println("║                   ~ Wake the f*** up, Samurai!!! ~                   ║");
        System.out.println("║                                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝");
        System.out.println(RESET);

        pause(2000); // wait 2 seconds for effect
    }
    //displays the home screen menu
    public static void showHomeScreen() {
        clearScreen();
        printHeader("NIGHT CITY FINANCIAL TERMINAL - HOME");

        System.out.println(YELLOW + "\n  What would you like to do, choom?\n" + RESET);
        System.out.println("  [D] Add Deposit        - Log incoming eddies");
        System.out.println("  [P] Make Payment       - Record outgoing eddies");
        System.out.println("  [L] Ledger             - View transaction history");
        System.out.println("  [X] Exit               - Jack out of the system");
        printSeparator();
    }

    // displays the ledger menu
    public static void showLedgerScreen() {
        clearScreen();
        printHeader("LEDGER - TRANSACTION HISTORY");

        System.out.println(YELLOW + "\n  Select view mode:\n" + RESET);
        System.out.println("  [A] All Transactions   - Show everything");
        System.out.println("  [D] Deposits Only      - Show only credits");
        System.out.println("  [P] Payments Only      - Show only debits");
        System.out.println("  [R] Reports            - Run analysis reports");
        System.out.println("  [H] Home               - Return to main menu");
        printSeparator();
    }

    // displays the reports menu
    public static void showReportsScreen() {
        clearScreen();
        printHeader("REPORTS - DATA ANALYSIS");

        System.out.println(YELLOW + "\n  Available reports:\n" + RESET);
        System.out.println("  [1] Month To Date      - Current month transactions");
        System.out.println("  [2] Previous Month     - Last month's activity");
        System.out.println("  [3] Year To Date       - Current year summary");
        System.out.println("  [4] Previous Year      - Last year's data");
        System.out.println("  [5] Search by Vendor   - Find specific vendor transactions");
        System.out.println("  [6] Custom Search      - Advanced filtering options");
        System.out.println("  [0] Back               - Return to ledger");
        printSeparator();
    }

    // displays the list of transactions in a formatted table
    public static void displayTransactions(List<Transaction> transactions, String title) {
        clearScreen();
        printHeader(title);
        if (transactions.isEmpty()) {
            System.out.println(RED + "\n  >> No transactions found. The database is empty, choom.\n" + RESET);
            return;
        }
        // prints table header
        System.out.println(CYAN + "\n  " + String.format("%-12s %-10s %-8s %-35s %-25s %12s",
                "DATE", "TIME", "TYPE", "DESCRIPTION", "VENDOR", "AMOUNT") + RESET);
        printSeparator();
        // print each transaction
        for (Transaction t : transactions) {
            String type = t.isDeposit() ? GREEN + "[+]" + RESET : RED + "[-]" + RESET;
            String amountColor = t.isDeposit() ? GREEN : RED;

            System.out.println(String.format("  %-12s %-10s %s   %-35s %-25s " + amountColor + "$%,11.2f" + RESET,
                    t.getDate().toString(),
                    t.getTime().toString(),
                    type,
                    truncate(t.getDescription(), 35),
                    truncate(t.getVendor(), 25),
                    t.getAmount()));
        }
        printSeparator();
        System.out.println(YELLOW + "  Total transactions: " + transactions.size() + RESET);
    }

    /**
     * Displays transaction statistics
     */
    public static void displayStatistics(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return;
        }

        double totalDeposits = 0;
        double totalPayments = 0;
        int depositCount = 0;
        int paymentCount = 0;

        for (Transaction t : transactions) {
            if (t.isDeposit()) {
                totalDeposits += t.getAmount();
                depositCount++;
            } else {
                totalPayments += Math.abs(t.getAmount());
                paymentCount++;
            }
        }

        double balance = totalDeposits - totalPayments;
        String balanceColor = balance >= 0 ? GREEN : RED;

        System.out.println("\n" + CYAN + "  ╔═══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "  ║" + RESET + "                   STATISTICS                          " + CYAN + "║" + RESET);
        System.out.println(CYAN + "  ╚═══════════════════════════════════════════════════════╝" + RESET);
        System.out.println("  " + GREEN + "  Total Deposits:  " + String.format("$%,15.2f", totalDeposits) + RESET);
        System.out.println("  " + "  Deposit Count:   " + String.format("%5d transactions", depositCount));
        System.out.println();
        System.out.println("  " + RED + "  Total Payments:  " + String.format("$%,15.2f", totalPayments) + RESET);
        System.out.println("  " + "  Payment Count:   " + String.format("%5d transactions", paymentCount));
        System.out.println();
        System.out.println("  " + "  ═══════════════════════════════════════════════");
        System.out.println("  " + balanceColor + BOLD + "  Current Balance: " + String.format("$%,15.2f", balance) + RESET);
        System.out.println();
        System.out.println(CYAN + "  ═════════════════════════════════════════════════════════" + RESET);
    }

    // shows a success message
    public static void showSuccessMessage(String message) {
        System.out.println(GREEN + "\n  ✓ " + message + RESET);
    }

    // shows an error message
    public static void showErrorMessage(String message) {
        System.out.println(RED + "\n  ✗ ERROR: " + message + RESET);
    }

    // shows a warning message
    public static void showWarningMessage(String message) {
        System.out.println(YELLOW + "\n  ⚠ " + message + RESET);
    }

    // prints a cyberpunk style header
    private static void printHeader(String title) {
        System.out.println(CYAN + "\n  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                                                                    ║");
        System.out.println(String.format("  ║  %-66s║", centerText(title, 66)));
        System.out.println("  ║                                                                    ║");
        System.out.println("  ╚════════════════════════════════════════════════════════════════════╝" + RESET);
    }

    // prints a separator line
    private static void printSeparator() {
        System.out.println(CYAN + "  ────────────────────────────────────────────────────────────────────" + RESET);
    }

    // centers text within a given width
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        StringBuilder centered = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            centered.append(" ");
        }
        centered.append(text);
        return centered.toString();
    }

    // truncates text if it is too long
    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    // clears the screen (works on most terminals)
    public static void clearScreen() {
        // this works on most terminals
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // backup method: print empty lines
        // if the ANSI codes don't work, at least we get some space
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }

    // pauses execution for a bit (for effect)
    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // if interrupted, just continues
        }
    }

    // waits for user to press enter
    public static void pressEnterToContinue() {
        System.out.println(YELLOW + "\n  Press ENTER to continue..." + RESET);
        try {
            new java.util.Scanner(System.in).nextLine();
        } catch (Exception e) {
            // just continues if there is an issue
        }
    }
}
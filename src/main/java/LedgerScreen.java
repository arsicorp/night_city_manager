import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * LedgerScreen handles displaying transactions in different views
 * users can filter by type (all, deposits, payments) and access reports
 */
public class LedgerScreen {
    private Scanner scanner;
    private boolean backToHome;
    public LedgerScreen(Scanner scanner) {
        this.scanner = scanner;
        this.backToHome = false;
    }

    /**
     * main loop for the ledger screen
     */
    public void show() {
        while (!backToHome) {
            DisplayHelper.showLedgerScreen();
            System.out.print("  Enter your choice: ");
            String choice = scanner.nextLine();

            handleLedgerChoice(choice);
        }
    }

    /**
     * processes the user's menu choice
     */
    private void handleLedgerChoice(String choice) {
        // validates input
        if (!InputValidator.isValidMenuChoice(choice, "A", "D", "P", "R", "H")) {
            DisplayHelper.pressEnterToContinue();
            return;
        }

        String normalizedChoice = choice.trim().toUpperCase();

        switch (normalizedChoice) {
            case "A":
                displayAllTransactions();
                break;
            case "D":
                displayDeposits();
                break;
            case "P":
                displayPayments();
                break;
            case "R":
                // navigates to reports screen
                ReportsScreen reportsScreen = new ReportsScreen(scanner);
                reportsScreen.show();
                break;
            case "H":
                backToHome = true;  // exits loop and returns to home
                break;
            default:
                DisplayHelper.showErrorMessage("Something went wrong!");
                break;
        }
    }

    /**
     * displays all transactions, sorted by newest first
     */
    private void displayAllTransactions() {
        // load all transactions from file
        List<Transaction> transactions = FileManager.readTransactions();

        // sort by date and time, newest first (requirement!)
        sortTransactionsNewestFirst(transactions);

        // display the transactions
        DisplayHelper.displayTransactions(transactions, "ALL TRANSACTIONS");

        // show statistics (bonus feature!)
        DisplayHelper.displayStatistics(transactions);

        // asks if user wants to export
        System.out.print("\n  Export these transactions to a file? (Y/N): ");
        String exportChoice = scanner.nextLine();

        if (InputValidator.isYes(exportChoice)) {
            System.out.print("  Enter filename (e.g., all_transactions.csv): ");
            String filename = scanner.nextLine().trim();

            if (!filename.isEmpty()) {
                FileManager.exportTransactions(transactions, filename);
                DisplayHelper.showSuccessMessage("Transactions exported to " + filename);
            }
        }
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * displays only deposit transactions
     */
    private void displayDeposits() {
        // load all transactions
        List<Transaction> allTransactions = FileManager.readTransactions();

        // filters to only deposits (positive amounts)
        List<Transaction> deposits = new ArrayList<>();
        for (Transaction t : allTransactions) {
            if (t.isDeposit()) {
                deposits.add(t);
            }
        }

        // sort newest first
        sortTransactionsNewestFirst(deposits);

        // display
        DisplayHelper.displayTransactions(deposits, "DEPOSITS ONLY - INCOMING EDDIES");

        // calculate deposit statistics
        double totalDeposits = 0;
        for (Transaction t : deposits) {
            totalDeposits += t.getAmount();
        }

        System.out.println("\n  Total Deposits: $" + String.format("%,.2f", totalDeposits));
        System.out.println("  Number of Deposits: " + deposits.size());

        // export option
        System.out.print("\n  Export deposits to a file? (Y/N): ");
        String exportChoice = scanner.nextLine();

        if (InputValidator.isYes(exportChoice)) {
            System.out.print("  Enter filename (e.g., deposits_only.csv): ");
            String filename = scanner.nextLine().trim();

            if (!filename.isEmpty()) {
                FileManager.exportTransactions(deposits, filename);
                DisplayHelper.showSuccessMessage("Deposits exported to " + filename);
            }
        }
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * displays only payment transactions
     */
    private void displayPayments() {
        // loads all transactions
        List<Transaction> allTransactions = FileManager.readTransactions();

        // filters to only payments (negative amounts)
        List<Transaction> payments = new ArrayList<>();
        for (Transaction t : allTransactions) {
            if (t.isPayment()) {
                payments.add(t);
            }
        }

        // sort newest first
        sortTransactionsNewestFirst(payments);

        // display
        DisplayHelper.displayTransactions(payments, "PAYMENTS ONLY - OUTGOING EDDIES");

        // calculate payment statistics
        double totalPayments = 0;
        for (Transaction t : payments) {
            totalPayments += Math.abs(t.getAmount());  // use absolute value for display
        }

        System.out.println("\n  Total Payments: $" + String.format("%,.2f", totalPayments));
        System.out.println("  Number of Payments: " + payments.size());

        // TODO: Maybe add "average payment amount" calculation here

        // export option
        System.out.print("\n  Export payments to a file? (Y/N): ");
        String exportChoice = scanner.nextLine();

        if (InputValidator.isYes(exportChoice)) {
            System.out.print("  Enter filename (e.g., payments_only.csv): ");
            String filename = scanner.nextLine().trim();

            if (!filename.isEmpty()) {
                FileManager.exportTransactions(payments, filename);
                DisplayHelper.showSuccessMessage("Payments exported to " + filename);
            }
        }
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * sorts transactions by date and time, newest first
     * this is a requirement from the project pdf
     */
    private void sortTransactionsNewestFirst(List<Transaction> transactions) {
        transactions.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                // first compare by date
                int dateComparison = t2.getDate().compareTo(t1.getDate());

                if (dateComparison != 0) {
                    return dateComparison;  // different dates, return date comparison
                }

                // same date, compare by time
                return t2.getTime().compareTo(t1.getTime());
            }
        });

        // Alternative shorter syntax (if you want to show you know lambda expressions):
        // transactions.sort((t1, t2) -> {
        //     int dateComp = t2.getDate().compareTo(t1.getDate());
        //     return dateComp != 0 ? dateComp : t2.getTime().compareTo(t1.getTime());
        // });
    }
}

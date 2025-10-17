import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ReportsScreen handles all the reporting and filtering functionality
 * includes time-based reports and custom searches
 */
public class ReportsScreen {
    private Scanner scanner;
    private boolean backToLedger;

    public ReportsScreen(Scanner scanner) {
        this.scanner = scanner;
        this.backToLedger = false;
    }

    /**
     * main loop for reports screen
     */
    public void show() {
        while (!backToLedger) {
            DisplayHelper.showReportsScreen();
            System.out.print("  Enter your choice: ");
            String choice = scanner.nextLine();

            handleReportsChoice(choice);
        }
    }

    /**
     * processes the user's report choice
     */
    private void handleReportsChoice(String choice) {
        // validates input
        if (!InputValidator.isValidNumericChoice(choice, 0, 6)) {
            DisplayHelper.pressEnterToContinue();
            return;
        }

        int reportChoice = Integer.parseInt(choice.trim());

        switch (reportChoice) {
            case 1:
                showMonthToDate();
                break;
            case 2:
                showPreviousMonth();
                break;
            case 3:
                showYearToDate();
                break;
            case 4:
                showPreviousYear();
                break;
            case 5:
                searchByVendor();
                break;
            case 6:
                customSearch();
                break;
            case 0:
                backToLedger = true;
                break;
            default:
                DisplayHelper.showErrorMessage("Invalid choice!");
                break;
        }
    }

    /**
     * shows all transactions from the start of current month to today
     */
    private void showMonthToDate() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);  // First day of current month

        System.out.println("\n  Filtering from " + startOfMonth + " to " + today);

        // load and filter transactions
        List<Transaction> allTransactions = FileManager.readTransactions();
        List<Transaction> filtered = filterByDateRange(allTransactions, startOfMonth, today);

        // sort newest first
        sortTransactionsNewestFirst(filtered);

        // display
        DisplayHelper.displayTransactions(filtered,
                "MONTH TO DATE REPORT - " + today.getMonth() + " " + today.getYear());
        DisplayHelper.displayStatistics(filtered);

        offerExport(filtered, "month_to_date.csv");
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * Shows all transactions from the previous month
     */
    private void showPreviousMonth() {
        LocalDate today = LocalDate.now();

        // calculates previous month date range
        YearMonth previousMonth = YearMonth.from(today).minusMonths(1);
        LocalDate startOfPrevMonth = previousMonth.atDay(1);  // First day
        LocalDate endOfPrevMonth = previousMonth.atEndOfMonth();  // Last day

        System.out.println("\n  Filtering from " + startOfPrevMonth + " to " + endOfPrevMonth);

        // load and filter transactions
        List<Transaction> allTransactions = FileManager.readTransactions();
        List<Transaction> filtered = filterByDateRange(allTransactions, startOfPrevMonth, endOfPrevMonth);

        // sort newest first
        sortTransactionsNewestFirst(filtered);

        // display
        DisplayHelper.displayTransactions(filtered,
                "PREVIOUS MONTH REPORT - " + previousMonth.getMonth() + " " + previousMonth.getYear());
        DisplayHelper.displayStatistics(filtered);

        offerExport(filtered, "previous_month.csv");
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * shows all transactions from January 1st of this year to today
     */
    private void showYearToDate() {
        LocalDate today = LocalDate.now();
        LocalDate startOfYear = LocalDate.of(today.getYear(), 1, 1);  // January 1st

        System.out.println("\n  Filtering from " + startOfYear + " to " + today);

        // load and filter transactions
        List<Transaction> allTransactions = FileManager.readTransactions();
        List<Transaction> filtered = filterByDateRange(allTransactions, startOfYear, today);

        // sort newest first
        sortTransactionsNewestFirst(filtered);

        // display
        DisplayHelper.displayTransactions(filtered,
                "YEAR TO DATE REPORT - " + today.getYear());
        DisplayHelper.displayStatistics(filtered);

        offerExport(filtered, "year_to_date.csv");
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * shows all transactions from the previous year
     */
    private void showPreviousYear() {
        LocalDate today = LocalDate.now();
        int previousYear = today.getYear() - 1;

        LocalDate startOfPrevYear = LocalDate.of(previousYear, 1, 1);  // Jan 1 of prev year
        LocalDate endOfPrevYear = LocalDate.of(previousYear, 12, 31);  // Dec 31 of prev year

        System.out.println("\n  Filtering from " + startOfPrevYear + " to " + endOfPrevYear);

        // load and filter transactions
        List<Transaction> allTransactions = FileManager.readTransactions();
        List<Transaction> filtered = filterByDateRange(allTransactions, startOfPrevYear, endOfPrevYear);

        // sort newest first
        sortTransactionsNewestFirst(filtered);

        // display
        DisplayHelper.displayTransactions(filtered,
                "PREVIOUS YEAR REPORT - " + previousYear);
        DisplayHelper.displayStatistics(filtered);

        offerExport(filtered, "previous_year.csv");
        DisplayHelper.pressEnterToContinue();
    }

    /**
     * searches for all transactions with a specific vendor
     */
    private void searchByVendor() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              SEARCH BY VENDOR - FIND TRANSACTIONS          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        System.out.print("  Enter vendor name to search for: ");
        String searchVendor = scanner.nextLine().trim();

        if (searchVendor.isEmpty()) {
            DisplayHelper.showWarningMessage("Search cancelled - no vendor name entered.");
            DisplayHelper.pressEnterToContinue();
            return;
        }

        // load all transactions
        List<Transaction> allTransactions = FileManager.readTransactions();

        // filter by vendor (case-insensitive, partial match)
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction t : allTransactions) {
            // using toLowerCase() for case-insensitive search
            // using contains() for partial match (so "Arasaka" matches "Arasaka Corp")
            if (t.getVendor().toLowerCase().contains(searchVendor.toLowerCase())) {
                filtered.add(t);
            }
        }

        // sort newest first
        sortTransactionsNewestFirst(filtered);

        // display
        DisplayHelper.displayTransactions(filtered,
                "VENDOR SEARCH RESULTS - \"" + searchVendor + "\"");
        DisplayHelper.displayStatistics(filtered);

        if (!filtered.isEmpty()) {
            offerExport(filtered, "vendor_" + searchVendor.replaceAll(" ", "_") + ".csv");
        }

        DisplayHelper.pressEnterToContinue();
    }

    /**
     * CHALLENGE FEATURE: Custom search with multiple criteria
     * users can filter by any combination of: start date, end date, description, vendor, amount
     */
    private void customSearch() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          CUSTOM SEARCH - ADVANCED FILTERING                  ║");
        System.out.println("║  Leave any field empty to skip that filter                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Collect search criteria
        String startDateStr = null;
        String endDateStr = null;
        String descriptionFilter = null;
        String vendorFilter = null;
        String amountStr = null;

        // start date
        do {
            System.out.print("  Start date (yyyy-MM-dd) [press ENTER to skip]: ");
            startDateStr = scanner.nextLine().trim();
            if (startDateStr.isEmpty()) break;
        } while (!InputValidator.isValidSearchDate(startDateStr));

        // end date
        do {
            System.out.print("  End date (yyyy-MM-dd) [press ENTER to skip]: ");
            endDateStr = scanner.nextLine().trim();
            if (endDateStr.isEmpty()) break;
        } while (!InputValidator.isValidSearchDate(endDateStr));

        // description
        System.out.print("  Description contains [press ENTER to skip]: ");
        descriptionFilter = scanner.nextLine().trim();

        // vendor
        System.out.print("  Vendor name contains [press ENTER to skip]: ");
        vendorFilter = scanner.nextLine().trim();

        // amount
        do {
            System.out.print("  Amount (exact match) [press ENTER to skip]: ");
            amountStr = scanner.nextLine().trim();
            if (amountStr.isEmpty()) break;
        } while (!InputValidator.isValidSearchAmount(amountStr));

        // show what we're searching for
        System.out.println("\n  ─────────────────────────────────────────────────────────");
        System.out.println("  SEARCH CRITERIA:");
        if (!startDateStr.isEmpty()) System.out.println("  Start Date: " + startDateStr);
        if (!endDateStr.isEmpty()) System.out.println("  End Date: " + endDateStr);
        if (!descriptionFilter.isEmpty()) System.out.println("  Description: " + descriptionFilter);
        if (!vendorFilter.isEmpty()) System.out.println("  Vendor: " + vendorFilter);
        if (!amountStr.isEmpty()) System.out.println("  Amount: $" + amountStr);
        System.out.println("  ─────────────────────────────────────────────────────────");

        // load all transactions
        List<Transaction> allTransactions = FileManager.readTransactions();
        List<Transaction> filtered = new ArrayList<>();

        // apply filters
        for (Transaction t : allTransactions) {
            boolean matches = true;

            // check start date filter
            if (!startDateStr.isEmpty()) {
                LocalDate startDate = LocalDate.parse(startDateStr);
                if (t.getDate().isBefore(startDate)) {
                    matches = false;
                }
            }

            // check end date filter
            if (!endDateStr.isEmpty()) {
                LocalDate endDate = LocalDate.parse(endDateStr);
                if (t.getDate().isAfter(endDate)) {
                    matches = false;
                }
            }

            // check description filter (case-insensitive, partial match)
            if (!descriptionFilter.isEmpty()) {
                if (!t.getDescription().toLowerCase().contains(descriptionFilter.toLowerCase())) {
                    matches = false;
                }
            }

            // check vendor filter (case-insensitive, partial match)
            if (!vendorFilter.isEmpty()) {
                if (!t.getVendor().toLowerCase().contains(vendorFilter.toLowerCase())) {
                    matches = false;
                }
            }

            // check amount filter (exact match)
            if (!amountStr.isEmpty()) {
                double searchAmount = Double.parseDouble(amountStr);
                // use a small tolerance for floating point comparison
                if (Math.abs(t.getAmount() - searchAmount) > 0.001) {
                    matches = false;
                }
            }

            // if all filters passed, add to results
            if (matches) {
                filtered.add(t);
            }
        }

        // sort newest first
        sortTransactionsNewestFirst(filtered);

        // display
        DisplayHelper.displayTransactions(filtered, "CUSTOM SEARCH RESULTS");
        DisplayHelper.displayStatistics(filtered);

        // TODO: Could add option to save search criteria for later reuse

        if (!filtered.isEmpty()) {
            offerExport(filtered, "custom_search_results.csv");
        }

        DisplayHelper.pressEnterToContinue();
    }

    /**
     * helper method: Filters transactions by date range (inclusive)
     */
    private List<Transaction> filterByDateRange(List<Transaction> transactions,
                                                LocalDate startDate,
                                                LocalDate endDate) {
        List<Transaction> filtered = new ArrayList<>();

        for (Transaction t : transactions) {
            // check if transaction date is within range (inclusive)
            if ((t.getDate().isEqual(startDate) || t.getDate().isAfter(startDate)) &&
                    (t.getDate().isEqual(endDate) || t.getDate().isBefore(endDate))) {
                filtered.add(t);
            }
        }

        return filtered;
    }

    /**
     * helper method: Sorts transactions newest first
     * (same logic as in LedgerScreen - could be moved to a utility class)
     */
    private void sortTransactionsNewestFirst(List<Transaction> transactions) {
        transactions.sort((t1, t2) -> {
            int dateComp = t2.getDate().compareTo(t1.getDate());
            return dateComp != 0 ? dateComp : t2.getTime().compareTo(t1.getTime());
        });
    }

    /**
     * helper method: Offers to export filtered results
     */
    private void offerExport(List<Transaction> transactions, String suggestedFilename) {
        if (transactions.isEmpty()) {
            return;  // don't offer export if no results
        }

        System.out.print("\n  Export these results to a file? (Y/N): ");
        String exportChoice = scanner.nextLine();

        if (InputValidator.isYes(exportChoice)) {
            System.out.print("  Enter filename [" + suggestedFilename + "]: ");
            String filename = scanner.nextLine().trim();

            // use suggested filename if user just presses ENTER
            if (filename.isEmpty()) {
                filename = suggestedFilename;
            }

            FileManager.exportTransactions(transactions, filename);
            DisplayHelper.showSuccessMessage("Report exported to " + filename);
        }
    }
}

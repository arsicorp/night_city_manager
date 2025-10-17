import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * InputValidator checks user input to make sure it's valid
 * prevents bad data from getting into our system
 */
public class InputValidator {
    /**
     * validates a date string
     * format should be: yyyy-MM-dd (like 2023-04-15)
     * date should not be in the future
     */
    public static boolean isValidDate(String dateInput) {
        if (dateInput == null || dateInput.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Date cannot be empty!");
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(dateInput.trim());
            // checks if date is in the future
            if (date.isAfter(LocalDate.now())) {
                DisplayHelper.showErrorMessage("Date cannot be in the future! (Unless you have a time machine...)");
                return false;
            }
            return true;
        } catch (DateTimeParseException e) {
            DisplayHelper.showErrorMessage("Invalid date format! Use yyyy-MM-dd (example: 2023-04-15)");
            return false;
        }
    }

    /**
     * validates a time string
     * format should be: HH:mm:ss (like 14:30:00)
     */
    public static boolean isValidTime(String timeInput) {
        if (timeInput == null || timeInput.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Time cannot be empty!");
            return false;
        }
        try {
            LocalTime.parse(timeInput.trim());
            return true;
        } catch (DateTimeParseException e) {
            DisplayHelper.showErrorMessage("Invalid time format! Use HH:mm:ss (example: 14:30:00)");
            return false;
        }
    }

    /**
     * validates an amount for deposits
     * must be a valid number and positive
     */
    public static boolean isValidDepositAmount(String amountInput) {
        if (amountInput == null || amountInput.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Amount cannot be empty!");
            return false;
        }
        try {
            double amount = Double.parseDouble(amountInput.trim());
            // deposits must be positive
            if (amount <= 0) {
                DisplayHelper.showErrorMessage("Deposit amount must be positive! (You can't deposit negative eddies!)");
                return false;
            }
            // checks if amount is reasonable (not too large)
            // TODO: Maybe make this configurable based on user preference
            if (amount > 1000000000) {
                DisplayHelper.showWarningMessage("That's a LOT of eddies! Double-check the amount.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            DisplayHelper.showErrorMessage("Invalid amount! Please enter a valid number (example: 150.50)");
            return false;
        }
    }

    /**
     * validates an amount for payments
     * must be a valid number and positive (we will make it negative in the code)
     */
    public static boolean isValidPaymentAmount(String amountInput) {
        if (amountInput == null || amountInput.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Amount cannot be empty!");
            return false;
        }
        try {
            double amount = Double.parseDouble(amountInput.trim());
            // payment amount should be entered as positive (we will negate it later)
            if (amount <= 0) {
                DisplayHelper.showErrorMessage("Payment amount must be positive! (Just enter the number, we'll handle the negative)");
                return false;
            }
            // checks if amount is reasonable
            if (amount > 1000000000) {
                DisplayHelper.showWarningMessage("That's a huge payment! Double-check before proceeding.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            DisplayHelper.showErrorMessage("Invalid amount! Please enter a valid number (example: 89.50)");
            return false;
        }
    }

    /**
     * validates a description field
     * cannot be empty or just whitespace
     */
    public static boolean isValidDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Description cannot be empty! Tell us what this transaction is for.");
            return false;
        }
        // checks minimum length
        if (description.trim().length() < 3) {
            DisplayHelper.showErrorMessage("Description too short! Please be more specific (at least 3 characters).");
            return false;
        }
        return true;
    }

    /**
     * validates a vendor name
     * cannot be empty or just whitespace
     */
    public static boolean isValidVendor(String vendor) {
        if (vendor == null || vendor.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Vendor name cannot be empty! Who are you dealing with?");
            return false;
        }
        // checks minimum length
        if (vendor.trim().length() < 2) {
            DisplayHelper.showErrorMessage("Vendor name too short! Please enter a valid vendor name.");
            return false;
        }
        return true;
    }

    /**
     * validates menu choice against a list of valid options
     */
    public static boolean isValidMenuChoice(String input, String... validChoices) {
        if (input == null || input.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Please enter a choice!");
            return false;
        }
        String choice = input.trim().toUpperCase();
        for (String validChoice : validChoices) {
            if (choice.equals(validChoice.toUpperCase())) {
                return true;
            }
        }
        DisplayHelper.showErrorMessage("Invalid choice! Please select from the available options.");
        return false;
    }

    /**
     * validates a numeric choice (for reports menu)
     */
    public static boolean isValidNumericChoice(String input, int min, int max) {
        if (input == null || input.trim().isEmpty()) {
            DisplayHelper.showErrorMessage("Please enter a number!");
            return false;
        }
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice < min || choice > max) {
                DisplayHelper.showErrorMessage("Please enter a number between " + min + " and " + max + ".");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            DisplayHelper.showErrorMessage("Invalid input! Please enter a number.");
            return false;
        }
    }

    /**
     * validates yes/no input
     * accepts: Y, YES, N, NO (case-insensitive)
     */
    public static boolean isValidYesNo(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        String normalized = input.trim().toUpperCase();
        return normalized.equals("Y") || normalized.equals("YES") ||
                normalized.equals("N") || normalized.equals("NO");
    }

    /**
     * checks if input is "yes"
     */
    public static boolean isYes(String input) {
        if (input == null) {
            return false;
        }
        String normalized = input.trim().toUpperCase();
        return normalized.equals("Y") || normalized.equals("YES");
    }

    /**
     * validates an amount for custom search (can be empty for "no filter")
     * if not empty, must be a valid number
     */
    public static boolean isValidSearchAmount(String amountInput) {
        // empty is OK for search (means no filter)
        if (amountInput == null || amountInput.trim().isEmpty()) {
            return true;
        }
        try {
            Double.parseDouble(amountInput.trim());
            return true;
        } catch (NumberFormatException e) {
            DisplayHelper.showErrorMessage("Invalid amount format! Please enter a valid number or leave empty.");
            return false;
        }
    }

    /**
     * validates a date for custom search (can be empty for "no filter")
     * if not empty, must be valid date format
     */
    public static boolean isValidSearchDate(String dateInput) {
        // empty is OK for search (means no filter)
        if (dateInput == null || dateInput.trim().isEmpty()) {
            return true;
        }
        try {
            LocalDate.parse(dateInput.trim());
            return true;
        } catch (DateTimeParseException e) {
            DisplayHelper.showErrorMessage("Invalid date format! Use yyyy-MM-dd or leave empty.");
            return false;
        }
    }
}
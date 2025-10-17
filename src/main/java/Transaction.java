import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    // properties, the data each transaction stores
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;  // positive = deposit, negative = payment

    // constructor, how we create a new transaction
    public Transaction(LocalDate date, LocalTime time, String description,
                       String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // getters let us read the transaction data
    public LocalDate getDate() {
        return date;
    }
    public LocalTime getTime() {
        return time;
    }
    public String getDescription() {
        return description;
    }
    public String getVendor() {
        return vendor;
    }
    public double getAmount() {
        return amount;
    }

    // helper method to check if it is a deposit or payment
    public boolean isDeposit() {
        return amount > 0;
    }
    public boolean isPayment() {
        return amount < 0;
    }

    // here we format the transactions for CSV file (pipe-separated values)
    // our format: date|time|description|vendor|amount
    public String toCSVFormat() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return date.format(dateFormatter) + "|" +
                time.format(timeFormatter) + "|" +
                description + "|" +
                vendor + "|" +
                String.format("%.2f", amount);
    }

    // we display transaction in a readable format
    public String toDisplayFormat() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // TODO: Maybe I will add color coding or symbols for deposit and payment
        String type = amount > 0 ? "[CREDIT]" : "[DEBIT]";

        return String.format("%s %s | %s | %-30s | %-20s | $%,.2f",
                date.format(dateFormatter),
                time.format(timeFormatter),
                type,
                description,
                vendor,
                amount);
    }

    // this helps us create a transaction from a line in the CSV file
    public static Transaction fromCSVLine(String csvLine) {
        // split the line by the pipe character
        String[] parts = csvLine.split("\\|");

        // parse each part into the right data type
        LocalDate date = LocalDate.parse(parts[0].trim());
        LocalTime time = LocalTime.parse(parts[1].trim());
        String description = parts[2].trim();
        String vendor = parts[3].trim();
        double amount = Double.parseDouble(parts[4].trim());

        return new Transaction(date, time, description, vendor, amount);
    }
}
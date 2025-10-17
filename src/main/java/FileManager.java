import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * FileManager does all operations for the Night City Manager
 * reads and writes transactions to/from the CSV file
 */
public class FileManager {
    private static final String FILE_NAME = "transactions.csv";
    private static final String BACKUP_FOLDER = "backups";

    // this returns them as a list of Transaction objects
    public static List<Transaction> readTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        // checks if file exists, if not creates it
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No transaction file found. Creating new one...");
            createNewFile();
            return transactions; // return empty list for new file
        }

        // reads the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // skips empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    // converts the CSV line to a Transaction object
                    Transaction transaction = Transaction.fromCSVLine(line);
                    transactions.add(transaction);
                } catch (Exception e) {
                    // if a line is corrupted, skips it but keeps going
                    System.out.println("Warning: Skipped corrupted line: " + line);
                }
            }
            System.out.println("Loaded " + transactions.size() + " transactions from file.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }
    /**
     * writes a single new transaction to the CSV file
     * appends it to the end of the file
     */
    public static void writeTransaction(Transaction transaction) {
        // creates backup before modifying file
        createBackup();
        try (BufferedWriter writer = new BufferedWriter(
                // true = append mode
                new FileWriter(FILE_NAME, true))) {
            writer.write(transaction.toCSVFormat());
            writer.newLine();
            System.out.println("Transaction saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    /**
     * writes all transactions back to the CSV file
     * this is used when we need to update the entire file (like after sorting or deleting)
     */
    public static void writeAllTransactions(List<Transaction> transactions) {
        // Create backup before overwriting file
        createBackup();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toCSVFormat());
                writer.newLine();
            }
            System.out.println("All transactions saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    /**
     * creates a new empty CSV file
     */
    private static void createNewFile() {
        try {
            File file = new File(FILE_NAME);
            file.createNewFile();
            System.out.println("Created new transactions file: " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }
    /**
     * creates a backup of the current transactions file
     * backup filename includes timestamp
     */
    private static void createBackup() {
        File sourceFile = new File(FILE_NAME);
        // Only backups if file exists and has content
        if (!sourceFile.exists() || sourceFile.length() == 0) {
            return;
        }
        // creates backups folder if it doesn't exist
        File backupDir = new File(BACKUP_FOLDER);
        if (!backupDir.exists()) {
            backupDir.mkdir();
        }
        // creates backup filename with timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String backupFileName = BACKUP_FOLDER + "/transactions_backup_" + timestamp + ".csv";
        try {
            // Copy the file
            Files.copy(sourceFile.toPath(),
                    Paths.get(backupFileName),
                    StandardCopyOption.REPLACE_EXISTING);

            // TODO: Maybe add option to auto delete old backups after 30 days
        } catch (IOException e) {
            // ff backup fails, it is not critical, just log it
            System.out.println("Note: Could not create backup: " + e.getMessage());
        }
    }
    /**
     * exports filtered transactions to a new CSV file
     * it is useful for reports
     */
    public static void exportTransactions(List<Transaction> transactions, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // writes header line
            writer.write("Date|Time|Description|Vendor|Amount");
            writer.newLine();
            for (Transaction transaction : transactions) {
                writer.write(transaction.toCSVFormat());
                writer.newLine();
            }
            System.out.println("Exported " + transactions.size() + " transactions to " + fileName);
        } catch (IOException e) {
            System.out.println("Error exporting file: " + e.getMessage());
        }
    }
}
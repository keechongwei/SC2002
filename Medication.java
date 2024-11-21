import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a medication in the inventory system, including its name, stock level,
 * low stock threshold, and a low stock alert status. Provides methods to manage
 * stock updates and interact with a CSV file for persistence.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class Medication {

    /**
     * The file path to the CSV file containing the medication inventory data.
     */
    static String filePath = "Medicine_List.csv";

    /**
     * The name of the medication.
     */
    private String medicationName;

    /**
     * The current stock level of the medication.
     */
    private int stock;

    /**
     * Indicates whether the medication's stock is below the low stock threshold.
     */
    private boolean lowStockAlert;

    /**
     * The stock threshold below which a low stock alert is triggered.
     */
    private int lowStockValue;

    /**
     * Updates the stock of a medication by adding or removing the specified amount.
     * Updates are persisted to the CSV file.
     *
     * @param medicationName the name of the medication to update
     * @param amount         the amount to add or remove
     * @param isAddition     {@code true} to add stock, {@code false} to remove stock
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public static boolean updateStock(String medicationName, int amount, boolean isAddition) {
        List<Medication> medications = new ArrayList<>();
        boolean success = false;
        
        // Read current medications
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] values = line.split(";");
                if (values.length >= 3) {
                    String name = values[0].trim();
                    int stock = Integer.parseInt(values[1].trim());
                    int lowStockValue = Integer.parseInt(values[2].trim());
                    Medication med = new Medication(name, stock, lowStockValue);
                    
                    if (name.equalsIgnoreCase(medicationName)) {
                        if (isAddition) {
                            med.addStock(amount);
                            success = true;
                        } else {
                            if (med.getStock() >= amount) {
                                med.removeStock(amount);
                                success = true;
                            } else {
                                System.out.println("Insufficient stock. Current stock: " + med.getStock() + ", Requested: " + amount);
                                return false;
                            }
                        }
                    }
                    medications.add(med);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading medication file: " + e.getMessage());
            return false;
        }
        
        if (!success) {
            System.out.println("Medication '" + medicationName + "' not found in inventory.");
            return false;
        }
        
        // Write back all medications with updated stock
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("MedicationName;Stock;LowStockValue");
            for (Medication med : medications) {
                writer.println(med.getMedicationName() + ";" + med.getStock() + ";" + med.getLowStockValue());
            }
        } catch (IOException e) {
            System.out.println("Error writing to medication file: " + e.getMessage());
            return false;
        }
        
        return success;
    }

    /**
     * Constructs a new {@code Medication} instance.
     *
     * @param _medicationName the name of the medication
     * @param _stock          the initial stock level
     * @param _lowStockValue  the stock threshold for triggering a low stock alert
     */
    public Medication(String medicationName, int stock, int lowStockValue) {
        this.medicationName = medicationName;
        this.stock = stock;
        this.lowStockValue = lowStockValue;
        this.lowStockAlert = stock <= lowStockValue;
    }

    /**
     * Gets the name of the medication.
     *
     * @return the name of the medication
     */
    public String getMedicationName() {
        return medicationName;
    }

    /**
     * Gets the current stock level of the medication.
     *
     * @return the current stock level
     */
    public int getStock() {
        return stock;
    }

    /**
     * Checks whether the medication is under a low stock alert.
     *
     * @return {@code true} if the medication stock is below or equal to the low stock threshold,
     *         {@code false} otherwise
     */
    public boolean isLowStockAlert() {
        return lowStockAlert;
    }

    /**
     * Gets the stock threshold that triggers a low stock alert.
     *
     * @return the low stock threshold
     */
    public int getLowStockValue() {
        return lowStockValue;
    }

    /**
     * Adds stock to the medication and updates the low stock alert status.
     *
     * @param amount the amount of stock to add
     */
    public void addStock(int amount) {
        stock += amount;
        updateLowStockAlert();
    }

    /**
     * Removes stock from the medication and updates the low stock alert status.
     * Ensures the stock does not drop below zero.
     *
     * @param amount the amount of stock to remove
     */
    public void removeStock(int amount) {
        if (stock >= amount) {
            stock -= amount;
            updateLowStockAlert();
        } else {
            System.out.println("Not enough stock!");
        }
    }

    /**
     * Updates the low stock threshold and recalculates the low stock alert status.
     *
     * @param newLimit the new low stock threshold
     */
    public void updateLowStockLevel(int newLimit) {
        lowStockValue = newLimit;
        updateLowStockAlert();
    }

    /**
     * Updates the low stock alert status based on the current stock and low stock threshold.
     */
    public void updateLowStockAlert() {
        lowStockAlert = stock <= lowStockValue;
    }

    /**
     * Returns a string representation of the medication in CSV format.
     *
     * @return a string containing the medication name, stock, and low stock value separated by semicolons
     */
    public String toString() {
        return String.join(";", medicationName, String.valueOf(stock), String.valueOf(lowStockValue));
    }
}

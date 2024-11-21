package managers.csvhandlers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utility.Medication;
import managers.InventoryManager;

/**
 * Handles CSV file operations for managing inventory data, including reading
 * and writing medication data and replenishment requests.
 * Implements CSV Handler Interface
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class InventoryCSVHandler implements CSVHandler {
    /**
     * The file where medication data is stored.
     */
    public static final File csvFile = new File("Medicine_List.csv");
    /**
     * The file where replenishment request data is stored.
     */
    public static final File replenishFile = new File("src/Replenish_Request_List.csv");

    /**
     * Loads replenishment requests from the replenishment request CSV file.
     * Updates the last request ID in {@link InventoryManager}.
     *
     * @return a list of replenishment requests, where each request is represented
     *         as an array of strings.
     */
    public static List<String[]> loadReplenishRequests() {
        List<String[]> requests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(replenishFile))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(";");
                    requests.add(parts);
                    int requestId = Integer.parseInt(parts[0]);
                    if (requestId > InventoryManager.getLastRequestID()) {
                        InventoryManager.setLastRequestID(requestId);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading replenishment requests: " + e.getMessage());
        }
        return requests;
    }

    /**
     * Loads medication data from the medication CSV file into the inventory manager.
     * Clears the existing list of medications in {@link InventoryManager} before loading.
     * Ensures that duplicate medications are not added to the list.
     */
    public static void loadCSV() {
    String line;
    String csvSplitBy = ";";
    InventoryManager.listOfMedications.clear(); // Clear existing list before loading

    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        br.readLine(); // Skip the header line
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] data = line.split(csvSplitBy);
                if (data.length >= 3) {
                    String medicationName = data[0].trim();
                    int initialStock = Integer.parseInt(data[1].trim());
                    int lowStockValue = Integer.parseInt(data[2].trim());
                    
                    // Check if medication already exists
                    boolean exists = false;
                    for (Medication med : InventoryManager.listOfMedications) {
                        if (med.getMedicationName().equalsIgnoreCase(medicationName)) {
                            exists = true;
                            break;
                        }
                    }
                    
                    if (!exists) {
                        Medication medication = new Medication(medicationName, initialStock, lowStockValue);
                        InventoryManager.listOfMedications.add(medication);
                    }
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading CSV file: " + e.getMessage());
    } catch (NumberFormatException e) {
        System.out.println("Error parsing number from CSV: " + e.getMessage());
    }
}

    /**
     * Writes the current list of medications from {@link InventoryManager} to the medication CSV file.
     * Ensures that only unique medications are written to the file.
     */
    public static void writeCSV() {
        // First, create a temporary list to store unique medications
        List<Medication> uniqueMedications = new ArrayList<>();
        for (Medication med : InventoryManager.listOfMedications) {
            boolean exists = false;
            for (Medication uniqueMed : uniqueMedications) {
                if (uniqueMed.getMedicationName().equalsIgnoreCase(med.getMedicationName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                uniqueMedications.add(med);
            }
        }
        
        // Now write only the unique medications to the file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            bw.write("MedicationName;Stock;LowStockValue\n"); // Write header
            for (Medication medication : uniqueMedications) {
                bw.write(medication.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }

}

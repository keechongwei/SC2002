import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryCSVHandler {
    static String csvFilePath = "Medicine_List.csv"; // Replace with your actual CSV file path
    static String replenishRequestFile = "Replenish_Request_List.csv"; // Replace with your actual CSV file path

    public static List<String[]> loadReplenishRequests() {
        List<String[]> requests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(replenishRequestFile))) {
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
                    if (requestId > Inventory.getLastRequestID()) {
                        Inventory.setLastRequestID(requestId);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading replenishment requests: " + e.getMessage());
        }
        return requests;
    }

    public static void loadMedicationsFromCSV(String filePath) {
    String line;
    String csvSplitBy = ";";
    Inventory.listOfMedications.clear(); // Clear existing list before loading

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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
                    for (Medication med : Inventory.listOfMedications) {
                        if (med.getMedicationName().equalsIgnoreCase(medicationName)) {
                            exists = true;
                            break;
                        }
                    }
                    
                    if (!exists) {
                        Medication medication = new Medication(medicationName, initialStock, lowStockValue);
                        Inventory.listOfMedications.add(medication);
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

    public static void writeCSVFile() {
        // First, create a temporary list to store unique medications
        List<Medication> uniqueMedications = new ArrayList<>();
        for (Medication med : Inventory.listOfMedications) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
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

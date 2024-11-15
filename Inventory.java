import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory {
    private List<Medication> listOfMedications;
	static String csvFilePath = "Medicine_List.csv"; // Replace with your actual CSV file path

	public static void main(String[] args) {		
	
		// Create an Inventory object
		Inventory inventory = new Inventory(csvFilePath);
	
		// View the loaded inventory
		System.out.println("=== View Initial Inventory ===");
		inventory.viewInventory();
	
		// Search for a specific medication
		System.out.println("\n=== Search for a Medication ===");
		String searchMedication = "Paracetamol"; // Replace with an actual medication name in your CSV
		inventory.getMedication(searchMedication).ifPresentOrElse(
			medication -> System.out.println("Found: " + medication.getMedicationName() + " with stock: " + medication.getStock()),
			() -> System.out.println("Medication " + searchMedication + " not found.")
		);
	
		// Display the full inventory again to verify no changes
		System.out.println("\n=== Full Inventory List ===");
		inventory.viewInventory();
	}


    public Inventory(String csvFilePath) {
        listOfMedications = new ArrayList<>();
        loadMedicationsFromCSV(csvFilePath);
    }

    private void loadMedicationsFromCSV(String filePath) {
        String line;
        String csvSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSplitBy);

                String medicationName = data[0];
                int initialStock = Integer.parseInt(data[1]);
                int lowStockValue = Integer.parseInt(data[2]);

                Medication medication = new Medication(medicationName, initialStock, lowStockValue);
                listOfMedications.add(medication);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from CSV: " + e.getMessage());
        }
    }

    public Optional<Medication> getMedication(String medicationName) {
        return listOfMedications.stream()
                .filter(medication -> medication.getMedicationName().equalsIgnoreCase(medicationName))
                .findFirst();
    }

    public List<Medication> getInventory() {
        return listOfMedications;
    }

    public void viewInventory() {
        System.out.println("Inventory:");
        for (Medication medication : listOfMedications) {
            System.out.println(medication.getMedicationName() + ": " + medication.getStock() + " units in stock, Low stock alert: " + medication.isLowStockAlert());
        }
    }

    public void writeCSVFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            bw.write("MedicationName;Stock;LowStockValue\n"); // Write header
            for (Medication medication : this.listOfMedications) {
                bw.write(medication.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }

}

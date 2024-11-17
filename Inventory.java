import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
		Medication search_med = inventory.getMedication(searchMedication);
		System.out.println("Found: " + search_med.getMedicationName() + " with stock: " + search_med.getStock());
	
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

    public Medication getMedication(String medicationName) {
		for(Medication med : listOfMedications) {
			if (med.getMedicationName().equalsIgnoreCase(medicationName)) {
				return med;
			}
		}
		
		System.out.println("Unable to retrieve medication, medication not found");
		return null;
    }

    public List<Medication> getInventory() {
        return listOfMedications;
    }

    public void viewInventory() {
        loadMedicationsFromCSV("Medicine_List.csv");
        
        System.out.println("Inventory:");
        for (Medication medication : listOfMedications) {
            System.out.println(medication.getMedicationName() + ": " + medication.getStock() + " units in stock, Low stock alert: " + medication.isLowStockAlert());
        }
    }

	public void addNewMedication(String medName, int stock, int lowStockValue) {
		Medication medication = new Medication(medName, stock, lowStockValue);
		listOfMedications.add(medication);
		writeCSVFile();
	}

	public void removeMedication(String medName) {
		List<Medication> temp = new ArrayList<>();
		for(Medication medication: listOfMedications) {
			System.out.println(medication.getMedicationName());
			if (medication.getMedicationName().equals(medName)) {
				continue;
			} else {
				temp.add(medication);
			}
		}
		this.listOfMedications = temp;
		writeCSVFile();
	}

	// add is true, remove is false
	public boolean updateMedication(String medicationName, int amount, boolean add_or_remove) {

		boolean checker = false;
		Medication med = this.getMedication(medicationName);
		if (add_or_remove == true) {
			med.addStock(amount);
		} else {
			// Check curr amt vs stock amount, separate fn
			if (med.getStock() < amount) {
				return checker; // Return false to indicate cant remove stock
			}
			med.removeStock(amount);
		}

		System.out.println("Updated: " + med.getMedicationName() + " with stock: " + med.getStock());
		checker = true;
		writeCSVFile();
		return checker;
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

	public List<Medication> updateAllAlertLevels() {
		List<Medication> temp = new ArrayList<>();
		for (Medication medication : listOfMedications) {
			medication.updateLowStockAlert();
			if (medication.isLowStockAlert() == true) {
				temp.add(medication);
				System.out.println("Inventory low for "+ medication.getMedicationName + "...");
			}
		}
		writeCSVFile();
		return temp;
	}	

}

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    public static List<Medication> listOfMedications;
    public static int lastRequestId = 0;

    public static int getLastRequestID(){
        return Inventory.lastRequestId;
    }

    public static void setLastRequestID(int ID){
         Inventory.lastRequestId = ID;
    }

	// public static void main(String[] args) {		
	
	// 	// Create an Inventory object
	// 	Inventory inventory = new Inventory(csvFilePath);
	
	// 	// View the loaded inventory
	// 	System.out.println("=== View Initial Inventory ===");
	// 	inventory.viewInventory();
	
	// 	// Search for a specific medication
	// 	System.out.println("\n=== Search for a Medication ===");
	// 	String searchMedication = "Paracetamol"; // Replace with an actual medication name in your CSV
	// 	Medication search_med = inventory.getMedication(searchMedication);
	// 	System.out.println("Found: " + search_med.getMedicationName() + " with stock: " + search_med.getStock());
	
	// 	// Display the full inventory again to verify no changes
	// 	System.out.println("\n=== Full Inventory List ===");
	// 	inventory.viewInventory();
	// }


    public Inventory(String csvFilePath) {
        listOfMedications = new ArrayList<>();
        InventoryCSVHandler.loadMedicationsFromCSV(csvFilePath);
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
        
        for (Medication medication : listOfMedications) {
            System.out.println(medication.getMedicationName() + ": " + medication.getStock() + " units in stock, Low stock alert: " + medication.isLowStockAlert());
        }
    }

	public void addNewMedication(String medName, int stock, int lowStockValue) {
		Medication medication = new Medication(medName, stock, lowStockValue);
		listOfMedications.add(medication);
		InventoryCSVHandler.writeCSVFile();
	}

	public void removeMedication(String medName) {
		List<Medication> temp = new ArrayList<>();
		for(Medication medication: listOfMedications) {
			if (medication.getMedicationName().equals(medName)) {
				continue;
			} else {
				temp.add(medication);
			}
		}
		this.listOfMedications = temp;
		InventoryCSVHandler.writeCSVFile();
	}

	// add is true, remove is false
	public boolean updateMedication(String medicationName, int amount, boolean add_or_remove) {

		boolean checker = false;
		Medication med = this.getMedication(medicationName);
		if (med == null) {
			return false;
		}

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
		InventoryCSVHandler.writeCSVFile();
		return checker;
	}


	public List<Medication> updateAllAlertLevels() {
        List<Medication> lowStockMeds = new ArrayList<>();
        for (Medication medication : listOfMedications) {
            medication.updateLowStockAlert();
            if (medication.isLowStockAlert() == true) {
                lowStockMeds.add(medication);
                System.out.println("WARNING: Inventory low for " + medication.getMedicationName() + "...");
                
                // Calculate suggested replenishment amount
                int currentStock = medication.getStock();
                int lowStockValue = medication.getLowStockValue();
                int suggestedAmount = (lowStockValue * 2) - currentStock;
                
                System.out.println("Current stock: " + currentStock);
                System.out.println("Low stock threshold: " + lowStockValue);
                System.out.println("Suggested replenishment amount: " + suggestedAmount + " units");
                System.out.println("Please submit a replenishment request!");
                System.out.println(); // Add blank line for readability
            }
        }
        InventoryCSVHandler.writeCSVFile();
        return lowStockMeds;
    }

    public boolean checkReplenishRequestExists(String medicationName) {
        List<String[]> requests = InventoryCSVHandler.loadReplenishRequests();
        return requests.stream()
                      .anyMatch(request -> request[1].equals(medicationName) && 
                                        request[3].equals("Pending"));
    }

    public void submitReplenishRequest(String medicationName, int amount) {
        List<String[]> existingRequests = InventoryCSVHandler.loadReplenishRequests();
        int newRequestId = lastRequestId + 1;
        
        try (FileWriter fw = new FileWriter(InventoryCSVHandler.replenishRequestFile);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            bw.write("RequestID;MedicationName;AddedAmount;Status\n");
            bw.write(newRequestId + ";" + medicationName + ";" + amount + ";Pending\n");
            
            for (String[] request : existingRequests) {
                bw.write(String.join(";", request) + "\n");
            }
            
            System.out.println("Replenishment request submitted successfully.");
            lastRequestId = newRequestId;
            
        } catch (IOException e) {
            System.out.println("Error submitting replenishment request: " + e.getMessage());
        }
    }

    public void displayInventoryForReplenishment() {
        viewInventory();
    }

    // public static void initialiseMedicine() {
    //     if (!((InventoryCSVHandler.csvFilePath).exists()) || (InventoryCSVHandler.cs).length() == 0) {
    //         // File doesn't exist or is empty, create daily appointments
    //         System.out.println("Medicine_List.csv is empty or missing.");
    //     } else {
    //         System.out.println("Loading Medicine from Medicine_List.csv...");
    //         InventoryCSVHandler.loadRecordsCSV();
    //     }
    // }
}

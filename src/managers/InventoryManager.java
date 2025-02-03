package managers;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import managers.csvhandlers.InventoryCSVHandler;
import utility.Medication;

/**
 * Manages the inventory of medications by performing various operations such as 
 * adding, removing, updating, and viewing medications. It also handles 
 * replenishment requests and tracks low stock alerts.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class InventoryManager implements Manager {
    /**
     * List containing all the medications in the inventory.
     */
    public static List<Medication> listOfMedications;
    /**
     * Tracks the ID of the last replenishment request.
     */
    public static int lastRequestId = 0;

    /**
     * Gets the ID of the last replenishment request.
     *
     * @return the last replenishment request ID
     */
    public static int getLastRequestID(){
        return InventoryManager.lastRequestId;
    }

    /**
     * Sets the ID of the last replenishment request.
     *
     * @param ID the new last request ID
     */
    public static void setLastRequestID(int ID){
         InventoryManager.lastRequestId = ID;
    }

    /**
     * Constructs an instance of {@code InventoryManager} and initializes the list of 
     * medications by loading data from the CSV file.
     *
     * @param csvFilePath the path to the CSV file containing medication data
     */
    public InventoryManager(String csvFilePath) {
        listOfMedications = new ArrayList<>();
        InventoryCSVHandler.loadCSV();
    }

    /**
     * Initializes the inventory by loading medication data from the CSV file.
     * If the file is missing or empty, a message is displayed.
     */
    public void initialise() {
        if (!((InventoryCSVHandler.csvFile).exists()) || (InventoryCSVHandler.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("Medicine_List.csv is empty or missing.");
        } else {
            System.out.println("Loading Medicine from Medicine_List.csv...");
            InventoryCSVHandler.loadCSV();
        }
    }

    /**
     * Retrieves a medication by its name from the inventory.
     *
     * @param medicationName the name of the medication
     * @return the {@code Medication} object if found, otherwise {@code null}
     */
    public Medication getMedication(String medicationName) {
		for(Medication med : listOfMedications) {
			if (med.getMedicationName().equalsIgnoreCase(medicationName)) {
				return med;
			}
		}
		
		System.out.println("Unable to retrieve medication, medication not found");
		return null;
    }

    /**
     * Retrieves the entire inventory of medications.
     *
     * @return a list of {@code Medication} objects
     */
    public List<Medication> getInventory() {
        return listOfMedications;
    }

    /**
     * Displays the inventory of medications, including stock levels and low stock alerts.
     */
    public void viewInventory() {
        
        for (Medication medication : listOfMedications) {
            System.out.println(medication.getMedicationName() + ": " + medication.getStock() + " units in stock, Low stock alert: " + medication.isLowStockAlert());
        }
    }

    /**
     * Adds a new medication to the inventory.
     *
     * @param medName        the name of the medication
     * @param stock          the initial stock level
     * @param lowStockValue  the threshold for triggering low stock alerts
     */
	public void addNewMedication(String medName, int stock, int lowStockValue) {
		Medication medication = new Medication(medName, stock, lowStockValue);
		listOfMedications.add(medication);
		InventoryCSVHandler.writeCSV();
	}

    /**
     * Removes a medication from the inventory by its name.
     *
     * @param medName the name of the medication to remove
     */
	public void removeMedication(String medName) {
		List<Medication> temp = new ArrayList<>();
		for(Medication medication: listOfMedications) {
			if (medication.getMedicationName().equals(medName)) {
				continue;
			} else {
				temp.add(medication);
			}
		}
		InventoryManager.listOfMedications = temp;
		InventoryCSVHandler.writeCSV();
	}


    /**
     * Updates the stock level of a medication in the inventory.
     *
     * @param medicationName the name of the medication
     * @param amount         the amount to add or remove
     * @param add_or_remove  {@code true} to add stock, {@code false} to remove stock
     * @return {@code true} if the stock was successfully updated, otherwise {@code false}
     */
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
		InventoryCSVHandler.writeCSV();
		return checker;
	}


    /**
     * Updates low stock alerts for all medications in the inventory.
     *
     * @return a list of medications that are low in stock
     */
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
        InventoryCSVHandler.writeCSV();
        return lowStockMeds;
    }

    /**
     * Checks if a replenishment request exists for a specific medication.
     *
     * @param medicationName the name of the medication
     * @return {@code true} if a pending replenishment request exists, otherwise {@code false}
     */
    public boolean checkReplenishRequestExists(String medicationName) {
        List<String[]> requests = InventoryCSVHandler.loadReplenishRequests();
        return requests.stream()
                      .anyMatch(request -> request[1].equals(medicationName) && 
                                        request[3].equals("Pending"));
    }

    /**
     * Submits a replenishment request for a medication.
     *
     * @param medicationName the name of the medication
     * @param amount         the quantity to replenish
     */
    public void submitReplenishRequest(String medicationName, int amount) {
        List<String[]> existingRequests = InventoryCSVHandler.loadReplenishRequests();
        int newRequestId = lastRequestId + 1;
        
        try (FileWriter fw = new FileWriter(InventoryCSVHandler.replenishFile);
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

}

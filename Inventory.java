import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory {
    private List<Medication> listOfMedications;

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
}

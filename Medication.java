import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.List;
// import java.util.Scanner;


// Should consider changing Medication to only get and set methods for each medicine. (updating is considered set method for alert)
// All writing to csv, adding, subtracting, remove medication/stock done inventory 

public class Medication {
    static String filePath = "Medicine_List.csv";
    private String _medicationName;
    private int _stock;
    private boolean _lowStockAlert;
    private int _lowStockValue;

	// public static void main(String[] args) {
    //     List<Medication> medications = Medication.readCSVFile();
    //     Scanner scanner = new Scanner(System.in);

    //     while (true) {
    //         System.out.println("\n=== Medication Inventory Management ===");
    //         System.out.println("1. View All Medications");
    //         System.out.println("2. Add Stock");
    //         System.out.println("3. Remove Stock");
    //         System.out.println("4. Update Low Stock Value");
    //         System.out.println("5. Exit and Save");
    //         System.out.print("Choose an option: ");
    //         int choice = scanner.nextInt();
    //         scanner.nextLine(); // Consume newline

    //         switch (choice) {
    //             case 1:
    //                 System.out.println("\nCurrent Inventory:");
    //                 for (Medication medication : medications) {
    //                     System.out.println(
    //                         medication.getMedicationName() + 
    //                         " - Stock: " + medication.getStock() + 
    //                         ", Low Stock Value: " + medication.getLowStockValue() + 
    //                         ", Low Stock Alert: " + medication.isLowStockAlert());
    //                 }
    //                 break;

    //             case 2:
    //                 System.out.print("\nEnter Medication Name: ");
    //                 String addName = scanner.nextLine();
    //                 System.out.print("Enter Amount to Add: ");
    //                 int addAmount = scanner.nextInt();
    //                 scanner.nextLine(); // Consume newline

    //                 medications.stream()
    //                            .filter(m -> m.getMedicationName().equalsIgnoreCase(addName))
    //                            .findFirst()
    //                            .ifPresentOrElse(
    //                                m -> {
    //                                    m.addStock(addAmount);
    //                                    System.out.println("Stock updated.");
    //                                },
    //                                () -> System.out.println("Medication not found.")
    //                            );
    //                 break;

    //             case 3:
    //                 System.out.print("\nEnter Medication Name: ");
    //                 String removeName = scanner.nextLine();
    //                 System.out.print("Enter Amount to Remove: ");
    //                 int removeAmount = scanner.nextInt();
    //                 scanner.nextLine(); // Consume newline

    //                 medications.stream()
    //                            .filter(m -> m.getMedicationName().equalsIgnoreCase(removeName))
    //                            .findFirst()
    //                            .ifPresentOrElse(
    //                                m -> {
    //                                    m.removeStock(removeAmount);
    //                                    System.out.println("Stock updated.");
    //                                },
    //                                () -> System.out.println("Medication not found.")
    //                            );
    //                 break;

    //             case 4:
    //                 System.out.print("\nEnter Medication Name: ");
    //                 String updateName = scanner.nextLine();
    //                 System.out.print("Enter New Low Stock Value: ");
    //                 int newLowStockValue = scanner.nextInt();
    //                 scanner.nextLine(); // Consume newline

    //                 medications.stream()
    //                            .filter(m -> m.getMedicationName().equalsIgnoreCase(updateName))
    //                            .findFirst()
    //                            .ifPresentOrElse(
    //                                m -> {
    //                                    m.updateLowStockLevel(newLowStockValue);
    //                                    System.out.println("Low stock value updated.");
    //                                },
    //                                () -> System.out.println("Medication not found.")
    //                            );
    //                 break;

    //             case 5:
    //                 Medication.writeCSVFile(medications);
    //                 System.out.println("Changes saved to CSV file. Exiting...");
    //                 return;

    //             default:
    //                 System.out.println("Invalid choice. Please try again.");
    //         }
    //     }
    // }

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

    public Medication(String _medicationName, int _stock, int _lowStockValue) {
        this._medicationName = _medicationName;
        this._stock = _stock;
        this._lowStockValue = _lowStockValue;
        this._lowStockAlert = _stock <= _lowStockValue;
    }

    public String getMedicationName() {
        return _medicationName;
    }

    public int getStock() {
        return _stock;
    }

    public boolean isLowStockAlert() {
        return _lowStockAlert;
    }

    public int getLowStockValue() {
        return _lowStockValue;
    }

    public void addStock(int amount) {
        _stock += amount;
        updateLowStockAlert();
    }

    public void removeStock(int amount) {
        if (_stock >= amount) {
            _stock -= amount;
            updateLowStockAlert();
        } else {
            System.out.println("Not enough stock!");
        }
    }

    public void updateLowStockLevel(int newLimit) {
        _lowStockValue = newLimit;
        updateLowStockAlert();
    }

    public void updateLowStockAlert() {
        _lowStockAlert = _stock <= _lowStockValue;
    }

    @Override
    public String toString() {
        return String.join(";", _medicationName, String.valueOf(_stock), String.valueOf(_lowStockValue));
    }
}

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.util.List;
import java.util.Scanner;

public class Medication {
    private String _medicationName;
    private int _stock;
    private boolean _lowStockAlert;
    private int _lowStockValue;

	public static void main(String[] args) {
        String filePath = "Medicine_List.csv"; // Ensure this file exists for testing
        List<Medication> medications = Medication.readCSVFile(filePath);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Medication Inventory Management ===");
            System.out.println("1. View All Medications");
            System.out.println("2. Add Stock");
            System.out.println("3. Remove Stock");
            System.out.println("4. Update Low Stock Value");
            System.out.println("5. Exit and Save");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("\nCurrent Inventory:");
                    for (Medication medication : medications) {
                        System.out.println(
                            medication.getMedicationName() + 
                            " - Stock: " + medication.getStock() + 
                            ", Low Stock Value: " + medication.getLowStockValue() + 
                            ", Low Stock Alert: " + medication.isLowStockAlert());
                    }
                    break;

                case 2:
                    System.out.print("\nEnter Medication Name: ");
                    String addName = scanner.nextLine();
                    System.out.print("Enter Amount to Add: ");
                    int addAmount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    medications.stream()
                               .filter(m -> m.getMedicationName().equalsIgnoreCase(addName))
                               .findFirst()
                               .ifPresentOrElse(
                                   m -> {
                                       m.addStock(addAmount);
                                       System.out.println("Stock updated.");
                                   },
                                   () -> System.out.println("Medication not found.")
                               );
                    break;

                case 3:
                    System.out.print("\nEnter Medication Name: ");
                    String removeName = scanner.nextLine();
                    System.out.print("Enter Amount to Remove: ");
                    int removeAmount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    medications.stream()
                               .filter(m -> m.getMedicationName().equalsIgnoreCase(removeName))
                               .findFirst()
                               .ifPresentOrElse(
                                   m -> {
                                       m.removeStock(removeAmount);
                                       System.out.println("Stock updated.");
                                   },
                                   () -> System.out.println("Medication not found.")
                               );
                    break;

                case 4:
                    System.out.print("\nEnter Medication Name: ");
                    String updateName = scanner.nextLine();
                    System.out.print("Enter New Low Stock Value: ");
                    int newLowStockValue = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    medications.stream()
                               .filter(m -> m.getMedicationName().equalsIgnoreCase(updateName))
                               .findFirst()
                               .ifPresentOrElse(
                                   m -> {
                                       m.updateLowStockLevel(newLowStockValue);
                                       System.out.println("Low stock value updated.");
                                   },
                                   () -> System.out.println("Medication not found.")
                               );
                    break;

                case 5:
                    Medication.writeCSVFile(filePath, medications);
                    System.out.println("Changes saved to CSV file. Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
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

    private void updateLowStockAlert() {
        _lowStockAlert = _stock <= _lowStockValue;
    }

    @Override
    public String toString() {
        return String.join(";", _medicationName, String.valueOf(_stock), String.valueOf(_lowStockValue));
    }

    public static List<Medication> readCSVFile(String filePath) {
        List<Medication> medications = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                String name = data[0];
                int stock = Integer.parseInt(data[1]);
                int lowStockValue = Integer.parseInt(data[2]);
                medications.add(new Medication(name, stock, lowStockValue));
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return medications;
    }

    public static void writeCSVFile(String filePath, List<Medication> medications) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("MedicationName;Stock;LowStockValue\n"); // Write header
            for (Medication medication : medications) {
                bw.write(medication.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }
}

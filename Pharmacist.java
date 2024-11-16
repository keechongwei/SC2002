import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;
import java.util.Scanner;

public class Pharmacist extends User {
    private List<Prescription> pendingPrescriptions;
    private String gender;
    private int age;
    private String password;
    private String name;

    public Pharmacist(String HospitalID, String password) {
        super(HospitalID, password);
        this.pendingPrescriptions = new ArrayList<>();
        loadPendingPrescriptionsFromCSV("Pending_Prescriptions.csv");
    }

    public Pharmacist(String HospitalID, String name, String gender, String age) {
        super(HospitalID, "password");
        this.name = name;
        this.gender = gender;
        this.age = Integer.valueOf(age);
    }

    public void setPassword(String password){
        this.password = password;
    }

    // Load pending prescriptions from CSV file
    private void loadPendingPrescriptionsFromCSV(String fileName) {
        pendingPrescriptions.clear();
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 3) {  // Changed to check for 3 values
                    String medicationName = values[0].trim();
                    try {
                        int dosage = Integer.parseInt(values[1].trim());
                        PrescriptionStatus status = PrescriptionStatus.valueOf(values[2].trim());
                        Prescription prescription = new Prescription(medicationName, status, dosage);
                        pendingPrescriptions.add(prescription);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid dosage format for medication: " + medicationName);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading prescriptions: " + e.getMessage());
        }
    }


    // Update CSV file when prescription status changes
    // private void updatePendingPrescriptionsToCSV(String fileName) {
    //     try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
    //         for (Prescription prescription : pendingPrescriptions) {
    //             writer.println(prescription.getMedicationName() + "," + prescription.getDosage());
    //         }
    //     } catch (IOException e) {
    //         System.out.println("Error updating CSV: " + e.getMessage());
    //     }
    // }

    public List<Prescription> viewPendingPrescriptions() {
        loadPendingPrescriptionsFromCSV("Pending_Prescriptions.csv");
        
        List<Prescription> pending = new ArrayList<>();
        for (Prescription prescription : pendingPrescriptions) {
            if (prescription.getStatus() == PrescriptionStatus.PENDING) {
                pending.add(prescription);
            }
        }
        return pending;
    }

    public boolean updatePrescriptionStatus(String medicationName) {
        List<Prescription> remainingPrescriptions = new ArrayList<>();
        boolean prescriptionFound = false;
        int dosageToDispense = 0;
        
        // Read the current file content
        try (BufferedReader br = new BufferedReader(new FileReader("Pending_Prescriptions.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    // Write the header to remaining prescriptions
                    remainingPrescriptions.add(new Prescription("MedicationName", PrescriptionStatus.PENDING, 0) {
                        @Override
                        public String toString() {
                            return "MedicationName;Dosage;Status";
                        }
                    });
                    firstLine = false;
                    continue;
                }
                
                String[] values = line.split(",");
                if (values.length >= 3) {
                    String medName = values[0].trim();
                    int dosage = Integer.parseInt(values[1].trim());
                    PrescriptionStatus status = PrescriptionStatus.valueOf(values[2].trim());
                    
                    if (medName.equalsIgnoreCase(medicationName)) {
                        prescriptionFound = true;
                        dosageToDispense = dosage;
                        // Don't add this prescription to remaining prescriptions
                    } else {
                        remainingPrescriptions.add(new Prescription(medName, status, dosage));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading prescriptions: " + e.getMessage());
            return false;
        }
        
        if (!prescriptionFound) {
            System.out.println("Prescription not found.");
            return false;
        }
        
        // Update medication stock first
        Inventory inventory = new Inventory("Medicine_List.csv");
        if (!inventory.updateMedication(medicationName, dosageToDispense, false)) {
            System.out.println("Failed to update medication stock - insufficient quantity.");
            return false;
        }
        
        // Write back the remaining prescriptions
        try (PrintWriter writer = new PrintWriter(new FileWriter("Pending_Prescriptions.csv"))) {
            // Write header
            writer.println("MedicationName;Dosage;Status");
            
            // Write remaining prescriptions
            for (Prescription p : remainingPrescriptions) {
                if (!p.toString().equals("MedicationName;Dosage;Status")) {  // Skip header prescription
                    writer.println(p.getMedicationName() + "," + p.getDosage() + "," + p.getStatus());
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating prescriptions file: " + e.getMessage());
            return false;
        }
        
        return true;
    }

    public void viewInventory(Inventory inventory) {
        inventory.viewInventory();
    }

    public void addPendingPrescription(String MedicationName, int dosage) {
        System.out.println("Pending Prescription request submitted for: " + MedicationName + " with quantity " + dosage);

        try (PrintWriter writer = new PrintWriter(new FileWriter("Pending_Prescriptions.csv", true))) {
            writer.println(MedicationName + "," + dosage + ", PENDING");
        } catch (IOException e) {
            System.out.println("Error writing pending prescription request: " + e.getMessage());
        }
    }

    public void submitReplenishRequest(String medicationName, int stockAmount) {
        System.out.println("Replenishment request submitted for: " + medicationName + " with quantity " + stockAmount);

        try (PrintWriter writer = new PrintWriter(new FileWriter("ReplenishRequestsList.csv", true))) {
            writer.println(medicationName + "," + stockAmount + ", PENDING");
        } catch (IOException e) {
            System.out.println("Error writing replenish request: " + e.getMessage());
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        Inventory inventory = new Inventory("Medicine_List.csv");
        Pharmacist pharmacist = new Pharmacist("Pharm123", "securepass");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n==== Pharmacist Menu ====");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Prescription");
            System.out.println("3. View Pending Prescriptions");
            System.out.println("4. Dispense Prescription");
            System.out.println("5. Submit Replenish Request");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nInventory:");
                    pharmacist.viewInventory(inventory);
                    break;
                case 2: 
                    System.out.println("Enter medication name to prescribe: ");
                    String medicinename = scanner.nextLine();
                    System.out.println("Enter dosage: ");
                    int dosageAmount = scanner.nextInt();
                    scanner.nextLine();
                    pharmacist.addPendingPrescription(medicinename, dosageAmount);
                    break;
                case 3:
                    System.out.println("\nPending Prescriptions:");
                    List<Prescription> pending = pharmacist.viewPendingPrescriptions();
                    if (pending.isEmpty()) {
                        System.out.println("No pending prescriptions.");
                    } else {
                        for (int i = 0; i < pending.size(); i++) {
                            Prescription p = pending.get(i);
                            System.out.println((i + 1) + ". Medication: " + p.getMedicationName() + ", Dosage: " + p.getDosage());
                        }
                    }
                    break;
                case 4:
                System.out.println("\nPending Prescriptions:");
                List<Prescription> pendingForDispense = pharmacist.viewPendingPrescriptions();
                if (pendingForDispense.isEmpty()) {
                    System.out.println("No pending prescriptions to dispense.");
                } else {
                    for (Prescription p : pendingForDispense) {
                        System.out.println("Medication: " + p.getMedicationName() + 
                                         ", Dosage: " + p.getDosage());
                    }
                    System.out.print("\nEnter medication name to dispense: ");
                    String medToDispense = scanner.nextLine();
                    
                    // Try to dispense the medication
                    if (pharmacist.updatePrescriptionStatus(medToDispense)) {
                        System.out.println("Prescription successfully dispensed.");
                        
                        // Check for low stock alert after dispensing
                        Medication updatedMed = inventory.getMedication(medToDispense);
                        if (updatedMed != null && updatedMed.isLowStockAlert()) {
                            System.out.println("\nLOW STOCK ALERT");
                            System.out.println("Medication: " + updatedMed.getMedicationName());
                            System.out.println("Current Stock: " + updatedMed.getStock());
                            System.out.println("Low Stock Threshold: " + updatedMed.getLowStockValue());
                            System.out.println("Please consider submitting a replenishment request.");
                        }
                    } else {
                        System.out.println("Failed to dispense prescription.");
                    }
                }
                break;
                case 5:
                    System.out.print("Enter medication name to replenish: ");
                    String medName = scanner.nextLine();
                    System.out.print("Enter stock amount: ");
                    int stockAmount = scanner.nextInt();
                    scanner.nextLine();
                    pharmacist.submitReplenishRequest(medName, stockAmount);
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting Pharmacist system.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }
}

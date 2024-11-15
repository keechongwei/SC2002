import java.io.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.Optional;
import java.util.Scanner;

public class Pharmacist extends User {
    private List<Prescription> pendingPrescriptions;
    private String gender;
    private int age;

    public Pharmacist(String HospitalID, String password) {
        super(HospitalID, password);
        this.pendingPrescriptions = new ArrayList<>();
        loadPendingPrescriptionsFromCSV("pendingprescriptions.csv");
    }

    // Load pending prescriptions from CSV file
    private void loadPendingPrescriptionsFromCSV(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String medicationName = values[0];
                int dosage = Integer.parseInt(values[1]);
                Prescription prescription = new Prescription(medicationName, PrescriptionStatus.PENDING, dosage);
                pendingPrescriptions.add(prescription);
            }
        } catch (IOException e) {
            System.out.println("Error loading prescriptions: " + e.getMessage());
        }
    }

    // Update CSV file when prescription status changes
    private void updatePendingPrescriptionsToCSV(String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Prescription prescription : pendingPrescriptions) {
                writer.println(prescription.getMedicationName() + "," + prescription.getDosage());
            }
        } catch (IOException e) {
            System.out.println("Error updating CSV: " + e.getMessage());
        }
    }

    public List<Prescription> viewPendingPrescriptions() {
        List<Prescription> pending = new ArrayList<>();
        for (Prescription prescription : pendingPrescriptions) {
            if (prescription.getStatus() == PrescriptionStatus.PENDING) {
                pending.add(prescription);
            }
        }
        return pending;
    }

    public boolean updatePrescriptionStatus(Prescription prescription, Medication medication) {
        if (prescription.getStatus() == PrescriptionStatus.PENDING) {
            if (medication.removeStock(prescription.getDosage())) {
                prescription.setStatus(PrescriptionStatus.DISPENSED);
                pendingPrescriptions.remove(prescription);
                updatePendingPrescriptionsToCSV("pendingprescriptions.csv"); // Update CSV after dispensing
                return true;
            } else {
                System.out.println("Insufficient stock for " + medication.getMedicationName());
                return false;
            }
        }
        return false;
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

        try (PrintWriter writer = new PrintWriter(new FileWriter("Replenish_Request.csv", true))) {
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
            // System.out.println("3. Dispense Prescription");
            System.out.println("4. Submit Replenish Request");
            System.out.println("5. Exit");
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
                // case 3:
                //     System.out.print("Enter medication name to dispense: ");
                //     String medToDispense = scanner.nextLine();
                //     Optional<Prescription> prescriptionOpt = pharmacist.pendingPrescriptions.stream()
                //             .filter(p -> p.getMedicationName().equalsIgnoreCase(medToDispense))
                //             .findFirst();
                //     if (prescriptionOpt.isPresent()) {
                //         Prescription prescription = prescriptionOpt.get();
                //         Medication medication = inventory.getMedication(medToDispense); // Assuming `getMedication` is defined in Inventory
                //         if (medication != null) {
                //             pharmacist.updatePrescriptionStatus(prescription, medication);
                //         } else {
                //             System.out.println("Medication not found in inventory.");
                //         }
                //     } else {
                //         System.out.println("Prescription not found.");
                //     }
                //     break;
                case 4:
                    System.out.print("Enter medication name to replenish: ");
                    String medName = scanner.nextLine();
                    System.out.print("Enter stock amount: ");
                    int stockAmount = scanner.nextInt();
                    scanner.nextLine();
                    pharmacist.submitReplenishRequest(medName, stockAmount);
                    break;
                case 5:
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

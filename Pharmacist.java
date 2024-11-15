import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;

public class Pharmacist extends User{
    private List <Prescription> pendingPrescriptions;
    private String gender;
    private int age;

    public Pharmacist(String HospitalID, String password) {
        super(HospitalID, password);
        this.pendingPrescriptions = new ArrayList<>();
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
    
    public void submitReplenishRequest(String medicationName, int stockAmount) {
        System.out.println("Replenishment request submitted for: " + medicationName + " with quantity " + stockAmount);
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

    public static void main (String[] args) {
        Inventory inventory = new Inventory("Medicine_List.csv");
        Pharmacist pharmacist = new Pharmacist("Pharm123", "securepass");

        pharmacist.pendingPrescriptions.add(new Prescription("Paracetemol", PrescriptionStatus.PENDING, 10));
        pharmacist.pendingPrescriptions.add(new Prescription("Ibuprofen", PrescriptionStatus.PENDING, 5));
        
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n==== Pharmacist Menu ====");
            System.out.println("1. View Inventory");
            System.out.println("2. View Pending Prescriptions");
            System.out.println("3. Dispense Prescription");
            System.out.println("4. Submit Replenish Request");
            System.out.println("5. Exit");
            System.out.println("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nInventory:");
                    pharmacist.viewInventory(inventory);
                    break;
                case 2:
                    System.out.println("\nPending Prescriptions:");
                    List<Prescription> pending = pharmacist.viewPendingPrescriptions();
                    if (pending.isEmpty()) {
                        System.out.println("No pending prescriptions.");

                    } else {
                        for (int i = 0; i < pending.size(); i++) {
                            Prescription p = pending.get(i);
                            System.out.println((i + 1) + ". Medication" + p.getMedicationName() + ", Dosage: " + p.getDosage());
                        }
                    }
                    for (Prescription p : pharmacist.viewPendingPrescriptions()) {
                        System.out.println("Medication: " + p.getMedicationName() + ", Dosage: " + p.getDosage());
                    }
                    break;
                case 3:
                    System.out.print("Enter medication name to dispense: ");
                    String medToDispense = scanner.nextLine();

            }
        }

    }
}


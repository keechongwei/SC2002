import java.util.ArrayList;
import java.util.List;

public class Pharmacist extends User{
    private List <Prescription> pendingPrescriptions;
    private String gender;
    private int age;

    public Pharmacist(String gender, int age) {
        this.pendingPrescriptions = new ArrayList<>();
        this.gender = gender;
        this.age = age;
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
    
    // public boolean updatePrescriptionStatus(Prescription prescription, Medication medication) {
    //     if (prescription.getStatus() == PrescriptionStatus.PENDING) {
    //         if (medication.removeStock(prescription.getDosage())) {
    //             prescription.setStatus(PrescriptionStatus.DISPENSED);
    //             pendingPrescriptions.remove(prescription);
    //             return true;
    //         } else {
    //             System.out.println("Insufficient stock for " + medication.getMedicationName());
    //             return false;
    //         }
    //     }
    //     return false;
    // }
    
    // public void viewInventory() {
    //     // Implement viewInventory logic
    //     System.out.println("Displaying Inventory: ");
    // }
    
    // public void submitReplenishRequest(String medicationName, int stockAmount) {
    //     // Implement submitReplenishRequest logic
    //     System.out.println("Replenishment request submitted for: " + medicationName + " with quantity " + stockAmount);
    // }
    
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
}


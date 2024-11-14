public class Prescription {
    private String medicationName;
    private PrescriptionStatus status;
    private int dosage;

    public Prescription(String medicationName, PrescriptionStatus status, int dosage) {
        this.medicationName = medicationName;
        this.status = status;
        this.dosage = dosage;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
    
    public void getDosage(int dosage){
        this.dosage = dosage;
    }
}
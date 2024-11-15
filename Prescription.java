public class Prescription {
    private String medicationName;
    private PrescriptionStatus status;
    private int dosage;
    // private Medication medication;

    public Prescription(String medicationName, PrescriptionStatus status, int dosage) {
        this.medicationName = medicationName;
        this.status = status;
        this.dosage = dosage;
        // this.medication = medication;
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
    
    public int getDosage(){
        return dosage;
    }

    // public Medication getMedication() {
    //     return medication;
    // }

    public static Prescription fromCSV(String csvString) { 
        String[] fields = csvString.split("\\^"); 
        String medicationName = fields[0]; 
        PrescriptionStatus status = PrescriptionStatus.valueOf(fields[1]); 
        int dosage = Integer.parseInt(fields[2]); 
        return new Prescription(medicationName, status, dosage); 
    }

    public String toCSV() {
        // Use ^ as the delimiter
        return (medicationName != null ? medicationName : "null") + "^" +
               (status != null ? status.name() : "null") + "^" +
               dosage;
    }


}
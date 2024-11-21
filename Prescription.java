/** 
 * The {@code Prescription} class represents a prescription for a specific medication.
 * A prescription includes details such as the medication name, its status, and the required dosage.
 * It provides methods for accessing and modifying these details, as well as for converting
 * prescriptions to and from CSV format.
 */
public class Prescription {

    /**
     * The name of the medication prescribed.
     */
    private String medicationName;

    /**
     * The status of the prescription (e.g., PENDING, DISPENSED).
     */
    private PrescriptionStatus status;

    /**
     * The dosage of the medication prescribed.
     */
    private int dosage;

    /**
     * Constructs a new {@code Prescription} with the specified details.
     *
     * @param medicationName the name of the medication.
     * @param status         the current status of the prescription.
     * @param dosage         the dosage of the medication prescribed.
     */
    public Prescription(String medicationName, PrescriptionStatus status, int dosage) {
        this.medicationName = medicationName;
        this.status = status;
        this.dosage = dosage;
        // this.medication = medication;
    }

    /**
     * Returns the name of the medication prescribed.
     *
     * @return the medication name.
     */
    public String getMedicationName() {
        return medicationName;
    }

    /**
     * Returns the current status of the prescription.
     *
     * @return the prescription status.
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the prescription.
     *
     * @param status the new status to set for the prescription.
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
    
    /**
     * Returns the dosage of the medication prescribed.
     *
     * @return the dosage.
     */
    public int getDosage(){
        return dosage;
    }


    /**
     * Creates a {@code Prescription} instance from a CSV-formatted string.
     *
     * @param csvString a string containing prescription details in CSV format, using `^` as the delimiter.
     *                  The format is: `medicationName^status^dosage`.
     * @return a new {@code Prescription} instance based on the parsed CSV data.
     */
    public static Prescription fromCSV(String csvString) { 
        String[] fields = csvString.split("\\^"); 
        String medicationName = fields[0]; 
        PrescriptionStatus status = PrescriptionStatus.valueOf(fields[1]); 
        int dosage = Integer.parseInt(fields[2]); 
        return new Prescription(medicationName, status, dosage); 
    }

    /**
     * Converts the prescription details to a CSV-formatted string.
     * Uses `^` as the delimiter between fields.
     *
     * @return a CSV representation of the prescription.
     */
    public String toCSV() {
        // Use ^ as the delimiter
        return (medicationName != null ? medicationName : "null") + "^" +
               (status != null ? status.name() : "null") + "^" +
               dosage;
    }
}
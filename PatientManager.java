import java.io.*;
import java.util.*;

/**
 * Manages operations related to patients in the hospital management system, including
 * adding new patients, updating patient records, and interacting with the CSV file for
 * data persistence. Provides functionalities to manage diagnoses, treatments, and
 * retrieve patient information.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class PatientManager implements Manager {
    /**
     * List containing all the patients in the system.
     */
    public static List<Patient> allPatients = new ArrayList<>();

    /**
     * Tracks the next available patient number for assigning unique patient IDs.
     */
    public static int nextPatientNumber = 1;

    /**
     * Adds a new patient to the system and appends the patient record to the CSV file.
     *
     * @param patient the patient to be added
     */
    public static void addPatient(Patient patient) {
        allPatients.add(patient);
        appendPatientToCSV(patient);
    }

    /**
     * Appends a single patient record to the CSV file.
     *
     * @param patient the patient to append to the CSV
     */
    private static void appendPatientToCSV(Patient patient) {
        try (FileWriter writer = new FileWriter(PatientManagerCSVHandler.csvFile, true)) {
            writer.write(PatientManagerCSVHandler.convertToCSV(patient) + "\n");
        } catch (IOException e) {
            System.err.println("Error appending to CSV file: " + e.getMessage());
        }
    }

    /**
     * Updates an existing patient record in the system and rewrites the CSV file
     * with the updated information.
     *
     * @param patient the patient with updated details
     */
    public static void updatePatient(Patient patient) {
        // Update in memory
        for (int i = 0; i < allPatients.size(); i++) {
            if (allPatients.get(i).getMedicalRecord().getPatientID().equals(patient.getMedicalRecord().getPatientID())) {
                allPatients.set(i, patient);
                break;
            }
        }
        // Rewrite file with updates
        PatientManagerCSVHandler.writeCSV();
    }

    /**
     * Adds a new diagnosis to a patient's medical record and updates the CSV file.
     *
     * @param patientID the ID of the patient
     * @param diagnosis the diagnosis to add
     */
    public static void addDiagnosis(String patientID, String diagnosis) {
        Patient patient = findPatient(patientID);
        if (patient != null) {
            patient.getMedicalRecord().getPastDiagnoses().add(diagnosis);
            updatePatient(patient);
        }
    }

    /**
     * Adds a new treatment to a patient's medical record and updates the CSV file.
     *
     * @param patientID the ID of the patient
     * @param treatment the treatment to add
     */
    public static void addTreatment(String patientID, String treatment) {
        Patient patient = findPatient(patientID);
        if (patient != null) {
            patient.getMedicalRecord().getPastTreatments().add(treatment);
            updatePatient(patient);
        }
    }

    /**
     * Finds a patient by their unique patient ID.
     *
     * @param patientID the unique ID of the patient
     * @return the {@code Patient} object if found, or {@code null} if not found
     */
    public static Patient findPatient(String patientID) {
        for (Patient patient : allPatients) {
            if (patient.getMedicalRecord().getPatientID().equals(patientID)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Initializes the patient manager by loading patient data from the CSV file.
     * If the file is missing or empty, displays a message indicating the issue.
     */
    public static void initialise() {
        if (!((PatientManagerCSVHandler.csvFile).exists()) || (PatientManagerCSVHandler.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("Patient_List.csv is empty or missing.");
        } else {
            System.out.println("Loading Patients from Patient_List.csv...");
            PatientManagerCSVHandler.loadCSV();
        }
    }
}
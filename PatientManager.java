import java.io.*;
import java.util.*;

public class PatientManager {
    public static List<Patient> allPatients= new ArrayList<>();
    public static int nextPatientNumber = 1;

    // Add new patient
    public static void addPatient(Patient patient) {
        allPatients.add(patient);
        appendPatientToCSV(patient);
    }

    // Append single patient to CSV
    private static void appendPatientToCSV(Patient patient) {
        try (FileWriter writer = new FileWriter(PatientManagerCSVHandler.csvFile, true)) {
            writer.write(PatientManagerCSVHandler.convertToCSV(patient) + "\n");
        } catch (IOException e) {
            System.err.println("Error appending to CSV file: " + e.getMessage());
        }
    }

    // Update existing patient
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

    // Add diagnosis to patient
    public static void addDiagnosis(String patientID, String diagnosis) {
        Patient patient = findPatient(patientID);
        if (patient != null) {
            patient.getMedicalRecord().getPastDiagnoses().add(diagnosis);
            updatePatient(patient);
        }
    }

    // Add treatment to patient
    public static void addTreatment(String patientID, String treatment) {
        Patient patient = findPatient(patientID);
        if (patient != null) {
            patient.getMedicalRecord().getPastTreatments().add(treatment);
            updatePatient(patient);
        }
    }

    // Find patient by ID
    public static Patient findPatient(String patientID) {
        for (Patient patient : allPatients) {
            if (patient.getMedicalRecord().getPatientID().equals(patientID)) {
                return patient;
            }
        }
        return null;
    }

    public static void initialisePatients() {
        if (!((PatientManagerCSVHandler.csvFile).exists()) || (PatientManagerCSVHandler.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("Patient_List.csv is empty or missing.");
        } else {
            System.out.println("Loading Patients from Patient_List.csv...");
            PatientManagerCSVHandler.loadCSV();
        }
    }
}
package managers.csvhandlers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Users.Patient;
import managers.PatientManager;


/**
 * The {@code PatientManagerCSVHandler} class handles the reading and writing of patient
 * data from and to a CSV file. It uses the {@code PatientManager} class to store 
 * patient records in memory and manages data consistency between the CSV file and memory.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class PatientManagerCSVHandler implements CSVHandler{

    /**
     * The CSV file where patient data is stored.
     */
    public static final File csvFile = new File("Patient_List copy.csv");

    /**
     * The delimiter used to separate fields in the CSV file.
     */
    private static final String CSV_DELIMITER = ";";

    /**
     * The delimiter used to separate items within lists (e.g., past diagnoses, past treatments).
     */
    private static final String LIST_DELIMITER = ",";


    /**
     * Loads patient data from the CSV file into memory. If the {@code PatientManager.allPatients}
     * list is not empty, it is cleared before loading new records.
     * 
     * This method skips the header row in the CSV file and reads each subsequent line
     * to create {@code Patient} objects, which are added to the {@code PatientManager.allPatients} list.
     */
    public static void loadCSV() {
       
        if(!(PatientManager.allPatients.isEmpty())) {
            PatientManager.allPatients.clear();
        }
        
        try (Scanner sc = new Scanner(csvFile)) {
            
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] patientFields = line.split(CSV_DELIMITER);



                String patientID = patientFields[0];
                String name = patientFields[1];
                String dateofbirth = patientFields[2];
                String gender = patientFields[3];
                String bloodType = patientFields[4];
                String emailAddress = patientFields[5];
                String phoneNumber = (patientFields.length > 6) ? patientFields[6] : "";
                String password = (patientFields.length > 7) ? patientFields[9] : patientFields[7];
                ArrayList<String> pastDiagnoses = new ArrayList<>();
                    if (patientFields.length > 7 && !patientFields[7].isEmpty()) {
                        pastDiagnoses.addAll(Arrays.asList(patientFields[7].split(LIST_DELIMITER)));
                    }

                    ArrayList<String> pastTreatments = new ArrayList<>();
                    if (patientFields.length > 8 && !patientFields[8].isEmpty()) {
                        pastTreatments.addAll(Arrays.asList(patientFields[8].split(LIST_DELIMITER)));
                    }

                Patient p = new Patient(patientID, name, dateofbirth, gender, bloodType, 
                                     phoneNumber, emailAddress, pastDiagnoses, pastTreatments,password);
                PatientManager.allPatients.add(p);
                PatientManager.nextPatientNumber++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Patient list file not found: " + e.getMessage());
        }
    }

    /**
     * Writes all patient data from memory to the CSV file. Existing data in the file
     * will be overwritten.
     * 
     * This method first writes a header row and then iterates over the 
     * {@code PatientManager.allPatients} list to write each patient's data as a line in the file.
     */
    public static void writeCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // Write header
            writer.write("Patient ID;Name;Date of Birth;Gender;Blood Type;Email;Phone;Past Diagnoses;Past Treatments;Password");
            writer.newLine();

            // Write each patient record
            for (Patient patient : PatientManager.allPatients) {
                writer.write(convertToCSV(patient));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * Converts a {@code Patient} object to a CSV-formatted string.
     * 
     * This method serializes all patient fields, including lists (e.g., past diagnoses
     * and past treatments), using the specified delimiters.
     * 
     * @param patient the {@code Patient} object to be converted.
     * @return a string representing the patient in CSV format.
     */
    public static String convertToCSV(Patient patient) {
        StringBuilder sb = new StringBuilder();
        try {
            // Append all fields with CSV_DELIMITER between them
            sb.append(patient.getMedicalRecord().getPatientID()).append(CSV_DELIMITER)
              .append(patient.getMedicalRecord().getName()).append(CSV_DELIMITER)
              .append(patient.getMedicalRecord().getDateOfBirth()).append(CSV_DELIMITER)
              .append(patient.getMedicalRecord().getGender()).append(CSV_DELIMITER)
              .append(patient.getMedicalRecord().getBloodType()).append(CSV_DELIMITER)
              .append(patient.getMedicalRecord().getEmailAddress()).append(CSV_DELIMITER)
              .append(patient.getMedicalRecord().getPhoneNumber()).append(CSV_DELIMITER);
    
            // Handle past diagnoses 
            ArrayList<String> diagnoses = patient.getMedicalRecord().getPastDiagnoses();
            sb.append(diagnoses != null && !diagnoses.isEmpty() 
                     ? String.join(LIST_DELIMITER, diagnoses) 
                     : "").append(CSV_DELIMITER);
    
            // Handle past treatments
            ArrayList<String> treatments = patient.getMedicalRecord().getPastTreatments();
            sb.append(treatments != null && !treatments.isEmpty() 
                     ? String.join(LIST_DELIMITER, treatments) 
                     : "").append(CSV_DELIMITER);
            
            sb.append(patient.getPassword());
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error converting patient to CSV: " + e.getMessage(), e);
        }
    
    }
}

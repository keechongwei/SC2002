import java.io.*;
import java.util.*;

public class PatientManager {
    public static List<Patient> allPatients= new ArrayList<>();
    public static final File csvFile = new File("Patient_List copy.csv");
    private static final String CSV_DELIMITER = ";";
    private static final String LIST_DELIMITER = ",";  // For separating items within ArrayLists

    // Load records from CSV
    public static void loadRecordsCSV() {
        //File file = new File(CSV_FILE_PATH);
        try (Scanner sc = new Scanner(csvFile)) {
            
            if (sc.hasNextLine()) {
                sc.nextLine();
            }

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] patientFields = line.split(CSV_DELIMITER);

                if (patientFields.length < 7) {
                    System.err.println("Skipping invalid record: " + line);
                    continue;
                }

                String patientID = patientFields[0];
                String name = patientFields[1];
                String dateofbirth = patientFields[2];
                String gender = patientFields[3];
                String bloodType = patientFields[4];
                String emailAddress = patientFields[5];
                String phoneNumber = (patientFields.length > 6) ? patientFields[6] : "";

                // Convert string representations back to ArrayLists
                ArrayList<String> pastDiagnoses = new ArrayList<>();
                if (patientFields.length > 7 && !patientFields[7].isEmpty()) {
                    pastDiagnoses.addAll(Arrays.asList(patientFields[7].split(LIST_DELIMITER)));
                }

                ArrayList<String> pastTreatments = new ArrayList<>();
                if (patientFields.length > 8 && !patientFields[8].isEmpty()) {
                    pastTreatments.addAll(Arrays.asList(patientFields[8].split(LIST_DELIMITER)));
                }

                Patient p = new Patient(patientID, name, dateofbirth, gender, bloodType, 
                                     phoneNumber, emailAddress, pastDiagnoses, pastTreatments);
                allPatients.add(p);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Patient list file not found: " + e.getMessage());
        }
    }

    // Write all records to CSV
    public static void writeAllRecords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            // Write header
            writer.write("Patient ID;Name;Date of Birth;Gender;Blood Type;Email;Phone;Past Diagnoses;Past Treatments");
            writer.newLine();

            // Write each patient record
            for (Patient patient : allPatients) {
                writer.write(convertToCSV(patient));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Convert Patient object to CSV line
    private static String convertToCSV(Patient patient) {
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
    
            // Handle past diagnoses - join with LIST_DELIMITER or empty string if null/empty
            ArrayList<String> diagnoses = patient.getMedicalRecord().getPastDiagnoses();
            sb.append(diagnoses != null && !diagnoses.isEmpty() 
                     ? String.join(LIST_DELIMITER, diagnoses) 
                     : "").append(CSV_DELIMITER);
    
            // Handle past treatments - join with LIST_DELIMITER or empty string if null/empty
            ArrayList<String> treatments = patient.getMedicalRecord().getPastTreatments();
            sb.append(treatments != null && !treatments.isEmpty() 
                     ? String.join(LIST_DELIMITER, treatments) 
                     : "");
    
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error converting patient to CSV: " + e.getMessage(), e);
        }
    
    }

    // Add new patient
    public static void addPatient(Patient patient) {
        allPatients.add(patient);
        appendPatientToCSV(patient);
    }

    // Append single patient to CSV
    private static void appendPatientToCSV(Patient patient) {
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.write(convertToCSV(patient) + "\n");
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
        writeAllRecords();
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

    // Example usage in main method
    public static void main(String[] args) {
        // Load existing records
        loadRecordsCSV();

        // Example of adding a diagnosis
        addDiagnosis("P1001", "Hypertension");

        // Example of adding a treatment
        addTreatment("P1001", "Prescribed ACE inhibitors");

        // Example of creating a new patient
        ArrayList<String> diagnoses = new ArrayList<>();
        ArrayList<String> treatments = new ArrayList<>();
        diagnoses.add("Initial checkup");
        treatments.add("Vitamin D supplements");

        Patient newPatient = new Patient("P1004", "John Doe", "1990-05-15", "Male", 
                                       "O+", "john.doe@email.com", "123-456-7890", 
                                       diagnoses, treatments);
        addPatient(newPatient);
    }
}
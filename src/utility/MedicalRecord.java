package utility;
import java.util.ArrayList;

import Users.Patient;
import managers.PatientManager;
import managers.csvhandlers.PatientManagerCSVHandler;

/**
 * Represents the medical record of a patient, containing details such as personal information,
 * contact details, blood type, and history of diagnoses and treatments.
 * Provides methods to manage and update the patient's medical information.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class MedicalRecord {
    /**
     * The unique ID of the patient associated with this medical record.
     */
    private String patientID;

    /**
     * The full name of the patient.
     */
    private String name;

    /**
     * The date of birth of the patient.
     */
    private String dateOfBirth;

    /**
     * The gender of the patient.
     */
    private String gender;

    /**
     * The phone number of the patient.
     */
    private String phoneNumber;

    /**
     * The email address of the patient.
     */
    private String emailAddress;

    /**
     * The blood type of the patient (e.g., A+, B-).
     */
    private String bloodType;

    /**
     * A list of past diagnoses of the patient.
     */
    private ArrayList<String> pastDiagnoses;

    /**
     * A list of past treatments of the patient.
     */
    private ArrayList<String> pastTreatments;

    /**
     * The patient associated with this medical record.
     */
    private Patient patient;

    /**
     * Constructs a {@code MedicalRecord} with the specified patient information and initializes
     * empty lists for past diagnoses and treatments.
     *
     * @param patientID    the unique ID of the patient
     * @param name         the full name of the patient
     * @param dateOfBirth  the date of birth of the patient
     * @param gender       the gender of the patient
     * @param phoneNumber  the phone number of the patient
     * @param emailAddress the email address of the patient
     * @param bloodType    the blood type of the patient
     */
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String phoneNumber, String emailAddress, String bloodType) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.bloodType = bloodType;
        this.pastDiagnoses = new ArrayList<>();
        this.pastTreatments = new ArrayList<>();
    }

    /**
     * Constructs a {@code MedicalRecord} with the specified patient information, past diagnoses, and treatments.
     *
     * @param patientID      the unique ID of the patient
     * @param name           the full name of the patient
     * @param dateOfBirth    the date of birth of the patient
     * @param gender         the gender of the patient
     * @param phoneNumber    the phone number of the patient
     * @param emailAddress   the email address of the patient
     * @param bloodType      the blood type of the patient
     * @param pastDiagnoses  a list of the patient's past diagnoses
     * @param pastTreatments a list of the patient's past treatments
     */
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String phoneNumber, String emailAddress, String bloodType, ArrayList<String> pastDiagnoses, ArrayList<String> pastTreatments) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
    }

    /**
     * Sets the patient associated with this medical record.
     *
     * @param patient the patient to associate
     */
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    /**
     * Retrieves the latest version of the medical record from the CSV file.
     *
     * @return the latest medical record matching this patient's ID, or the current instance if no update is found
     */
    private MedicalRecord getLatestFromCSV() {
        PatientManagerCSVHandler.loadCSV();
        for(Patient p : PatientManager.allPatients) {
            if(p.getHospitalID().equals(this.patientID)) {
                return p.getMedicalRecord();
            }
        }
        return this;
    }

    /**
     * Updates the CSV data with the current state of the medical record.
     * Refreshes the local fields with the latest data from the CSV file.
     */
    private void updateCSV() {
        if(patient != null) {
            PatientManager.updatePatient(patient);
        
            //refresh 
            MedicalRecord latest = getLatestFromCSV();
            this.name = latest.name;
            this.dateOfBirth = latest.dateOfBirth;
            this.gender = latest.gender;
            this.bloodType = latest.bloodType;
            this.emailAddress = latest.emailAddress;
            this.phoneNumber = latest.phoneNumber;
            this.pastDiagnoses.clear();
            this.pastDiagnoses.addAll(latest.pastDiagnoses);
            this.pastTreatments.clear();
            this.pastTreatments.addAll(latest.pastTreatments);

        }   
        
    }

    /**
     * Gets the patient ID.
     *
     * @return the patient ID
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Sets the patient ID and updates the CSV.
     *
     * @param patientID the new patient ID
     */
    public void setPatientID(String patientID) {
        this.patientID = patientID;
        updateCSV();
    }

    /**
     * Gets the name of the patient.
     *
     * @return the name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the patient and updates the CSV.
     *
     * @param name the new name of the patient
     */
    public void setName(String name) {
        this.name = name;
        updateCSV();
    }

    /**
     * Gets the date of birth of the patient.
     *
     * @return the date of birth of the patient
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the patient and updates the CSV.
     *
     * @param dateOfBirth the new date of birth of the patient
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        updateCSV();
    }

    /**
     * Gets the gender of the patient.
     *
     * @return the gender of the patient
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the patient and updates the CSV.
     *
     * @param gender the new gender of the patient
     */
    public void setGender(String gender) {
        this.gender = gender;
        updateCSV();
    }

    /**
     * Gets the phone number of the patient.
     *
     * @return the phone number of the patient
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the patient and updates the CSV.
     *
     * @param phoneNumber the new phone number of the patient
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateCSV();
    }

    /**
     * Gets the email address of the patient.
     *
     * @return the email address of the patient
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the patient and updates the CSV.
     *
     * @param emailAddress the new email address of the patient
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        updateCSV();
    }

    /**
     * Gets the blood type of the patient.
     *
     * @return the blood type of the patient
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Sets the blood type of the patient and updates the CSV.
     *
     * @param bloodType the new blood type of the patient
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
        updateCSV();
    }

    /**
     * Gets the list of past diagnoses of the patient.
     *
     * @return the list of past diagnoses
     */
    public ArrayList<String> getPastDiagnoses() {
        return pastDiagnoses;
    }

    /**
     * Adds a diagnosis to the patient's medical record and updates the CSV.
     *
     * @param diagnosis the new diagnosis to add
     */
    public void addDiagnosis(String diagnosis) {
        PatientManager.addDiagnosis(this.patientID, diagnosis);
        MedicalRecord latest = getLatestFromCSV();
        this.pastDiagnoses.clear();
        this.pastDiagnoses.addAll(latest.getPastDiagnoses());
    }

    /**
     * Gets the list of past treatments of the patient.
     *
     * @return the list of past treatments
     */
    public ArrayList<String> getPastTreatments() {
        return pastTreatments;
    }

    /**
     * Adds a treatment to the patient's medical record and updates the CSV.
     *
     * @param treatment the new treatment to add
     */
    public void addTreatment(String treatment) {
        PatientManager.addTreatment(this.patientID, treatment);
        MedicalRecord latest = getLatestFromCSV();
        this.pastTreatments.clear();
        this.pastTreatments.addAll(latest.getPastTreatments());
    }
}
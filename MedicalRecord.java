import java.util.ArrayList;
import java.util.Date;

public class MedicalRecord {
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String emailAddress;
    private String bloodType;
    private ArrayList<String> pastDiagnoses;
    private ArrayList<String> pastTreatments;
    private Patient patient;

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

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    private MedicalRecord getLatestFromCSV() {
        PatientManagerCSVHandler.loadCSV();
        for(Patient p : PatientManager.allPatients) {
            if(p.getHospitalID().equals(this.patientID)) {
                return p.getMedicalRecord();
            }
        }
        return this;
    }

    //update to csv, then update local info with csv info
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

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
        updateCSV();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateCSV();
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        updateCSV();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        updateCSV();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        updateCSV();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        updateCSV();

    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
        updateCSV();
    }

    public ArrayList<String> getPastDiagnoses() {
        return pastDiagnoses;
    }

    public void addDiagnosis(String diagnosis) {
        PatientManager.addDiagnosis(this.patientID, diagnosis);
        MedicalRecord latest = getLatestFromCSV();
        this.pastDiagnoses.clear();
        this.pastDiagnoses.addAll(latest.getPastDiagnoses());
    }

    public ArrayList<String> getPastTreatments() {
        return pastTreatments;
    }

    public void addTreatment(String treatment) {
        PatientManager.addTreatment(this.patientID, treatment);
        MedicalRecord latest = getLatestFromCSV();
        this.pastTreatments.clear();
        this.pastTreatments.addAll(latest.getPastTreatments());
    }
}
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

    //update to csv, then update local info with csv info
    private void updateCSV() {
        if(patient != null) {
            PatientManager.updatePatient(patient);
        
            PatientManager.loadRecordsCSV();
            for(Patient p : PatientManager.allPatients) {
                if(p.getHospitalID().equals(this.patientID)) {
                    this.name = p.getMedicalRecord().getName();
                    this.dateOfBirth = p.getMedicalRecord().getDateOfBirth();
                    this.gender = p.getMedicalRecord().getGender();
                    this.bloodType = p.getMedicalRecord().getBloodType();
                    this.emailAddress = p.getMedicalRecord().getEmailAddress();
                    this.phoneNumber = p.getMedicalRecord().getPhoneNumber();
                    
                    // Update lists
                    this.pastDiagnoses.clear();
                    this.pastDiagnoses.addAll(p.getMedicalRecord().getPastDiagnoses());
                    
                    this.pastTreatments.clear();
                    this.pastTreatments.addAll(p.getMedicalRecord().getPastTreatments());
                    
                    break;
                }
            }
        }   
        
    }

    public String getPatientID() {
        return patientID;
    }

    // public void setPatientID(String patientID) {
    //     this.patientID = patientID;
    //     updateCSV();
    // }

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
    }

    public ArrayList<String> getPastTreatments() {
        return pastTreatments;
    }

    public void addTreatment(String treatment) {
        PatientManager.addTreatment(this.patientID, treatment);
    }
}
import java.util.ArrayList;
import java.util.Date;

public class MedicalRecord {
    private String patientID;
    private String name;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String emailAddress;
    private String bloodType;
    private ArrayList<String> pastDiagnoses;
    private ArrayList<String> pastTreatments;

    public MedicalRecord(String patientID, String name, Date dateOfBirth, String gender, String phoneNumber, String emailAddress, String bloodType) {
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

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public ArrayList<String> getPastDiagnoses() {
        return pastDiagnoses;
    }

    public void addDiagnosis(String diagnosis) {
        pastDiagnoses.add(diagnosis);
    }

    public ArrayList<String> getPastTreatments() {
        return pastTreatments;
    }

    public void addTreatment(String treatment) {
        pastTreatments.add(treatment);
    }
}
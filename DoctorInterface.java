import java.util.List;

public interface DoctorInterface {
    void setPassword(String password);
    String getGender();
    void setGender(String gender);
    String getAge();
    void setAge(String age);
    String getDoctorID();
    String getDoctorName();
    void setName(String doctorName);
    void addPatient(Patient patient);
    void viewPatientRecords();
    Patient findPatientByID(String patientID);
    void setAvailabilityForAppointments();
    void viewPersonalSchedule();
    void updatePatientRecord();
    void viewUpcomingAppointment();
    void acceptOrDeclineAppointments();
    void makeAppointmentOutcomeRecord();
    String toCSV();
}
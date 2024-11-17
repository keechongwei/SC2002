import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class AppointmentSlot {
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private String appointmentID;
    private String doctorID;
    private String patientID;
    private AppointmentOutcomeRecord outcome;

    //  for constructing appointments from scratch?
    public AppointmentSlot(String date, String time, String status, String doctorID, String patientID) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        this.status = AppointmentStatus.valueOf(status);
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.outcome = null;
        this.appointmentID = "APT" + String.valueOf(AppointmentManager.nextAppointmentID);
        AppointmentManager.addAppointment(this); 
    }

    // for constructing appointments from CSV
    public AppointmentSlot(String date, String time, AppointmentStatus status, String doctorID, String patientID, AppointmentOutcomeRecord outcome, String appointmentID) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time,  DateTimeFormatter.ofPattern("HH:mm"));
        this.status = status;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.outcome = outcome;
    }
    public void setPatientID(String patientID){
        this.patientID = patientID;
    }

    public String getPatientID() {
        return this.patientID;
    }

    public String getAppointmentID(){
        return this.appointmentID;
    }
    
    public void setDoctorID(String doctorID){
        this.doctorID = doctorID;
    }

    public String getDoctorID() {
        return this.doctorID;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public AppointmentStatus getStatus() {
        return this.status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentOutcomeRecord getAppointmentOutcomeRecord() {
        return this.outcome;
    }

    public void updateAppointmentOutcomeRecord(LocalDate date,LocalTime time, String serviceType, Prescription pres, String consult) {
        TypeOfService typeOfServiceEnum;
    try {
        typeOfServiceEnum = TypeOfService.valueOf(serviceType.toUpperCase());
    } catch (IllegalArgumentException e) {
        System.out.println("Invalid service type: " + serviceType);
        return; // Exit if the service type is invalid
    }

    if (this.outcome == null) {
        System.out.println("Initializing outcome for the first time...");
        // Properly initialize with the correct enum type
        this.outcome = new AppointmentOutcomeRecord(
            date.toString(),
            time.toString(),
            typeOfServiceEnum, // Pass enum instead of string
            pres,
            consult
        );
    } else {
        // Update existing outcome
        this.outcome.setDate(date);
        this.outcome.setTime(time);
        this.outcome.setTypeOfService(typeOfServiceEnum); // Pass enum instead of string
        this.outcome.setPrescribedMedication(pres);
        this.outcome.setConsultationNotes(consult);
    }
    }

    // public Patient getPatient() {
    //     return Patient.getPatientByID(patientID);
    // }

    public String toCSV() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
        String dateString = date.format(dateFormatter);
        String timeString = time.format(timeFormatter);
    
        // Serialize the outcome if it exists
        String outcomeString = (outcome != null) ? outcome.toCSV() : "null";
    
        // Combine all attributes into a CSV string
        return dateString + ";" + timeString + ";" + appointmentID + ";" + doctorID + ";" + patientID + ";" + status + ";" + outcomeString;
    }
}
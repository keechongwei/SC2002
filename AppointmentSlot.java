import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class AppointmentSlot {
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private String doctorID;
    private String patientID;
    private AppointmentOutcomeRecord outcome;

    public AppointmentSlot(String date, String time, String status, String doctorID, String patientID) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        this.status = AppointmentStatus.valueOf(status);
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.outcome = null;
        AppointmentManager.addAppointment(this); 
    }

    public AppointmentSlot(String date, String time, AppointmentStatus status, String doctorID, String patientID, AppointmentOutcomeRecord outcome) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time,  DateTimeFormatter.ofPattern("HH:mm"));
        this.status = status;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.outcome = outcome;
    }
    public String getPatientID() {
        return this.patientID;
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
        this.outcome.setDate(date);
        this.outcome.setTime(time);
        this.outcome.setTypeOfService(serviceType);
        this.outcome.setPrescribedMedication(pres);
        this.outcome.setConsultationNotes(consult);
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
        return dateString + ";" + timeString + ";" + doctorID + ";" + patientID + ";" + status + ";" + outcomeString;
    }

    public static void main(String[] args){
 //       AppointmentSlot test = new AppointmentSlot("2002-11-07", "07:00", "CONFIRMED","A001","P001");
    //    test.outcome = new AppointmentOutcomeRecord("2002-11-07","09:00","CONSULTATION", new Prescription("Panadol",PrescriptionStatus.valueOf("PENDING"),5), null);
     //   AppointmentManager.appendAppointmentToCSV(test);
        AppointmentManager.loadAppointmentsFromCSV(AppointmentManager.csvFile);
        for(AppointmentSlot appt : AppointmentManager.appointmentSlotArray){
            System.out.println(appt.getDoctorID());
        }
    }
}
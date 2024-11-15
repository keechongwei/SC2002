import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentSlot {
    enum AppointmentStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        COMPLETED,
        AVAILABLE
    }
    private Date date;
    private String time;
    private AppointmentStatus status;
    private String doctorID;
    private String patientID;
    private AppointmentOutcomeRecord outcome;

    public AppointmentSlot(Date date, String time, AppointmentStatus status, String doctorID, String patientID) {
        this.date = date;
        this.time = time;
        this.status = status;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.outcome = null;
        AppointmentManager.addAppointment(this); 
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentOutcomeRecord getAppointmentOutcomeRecord() {
        return outcome;
    }

    public void updateAppointmentOutcomeRecord(Date date, String serviceType, String medicineType, String consultationNote) {
        this.outcome = new AppointmentOutcomeRecord(date, serviceType, medicineType, consultationNote);
    }

    public Patient getPatient() {
        return Patient.getPatientByID(patientID);
    }
}
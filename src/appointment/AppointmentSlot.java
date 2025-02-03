package appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import enums.AppointmentStatus;
import enums.TypeOfService;
import managers.AppointmentManager;
import utility.Prescription;

/**
 * AppointmentSlot meant to contain important information of every appointment
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class AppointmentSlot {
    /*
     * Date of Appointment
     */
    private LocalDate date;
    /*
     * Time of Appointment
     */
    private LocalTime time;
    /*
     * Status of AppointmentSlot
     */
    private AppointmentStatus status;
    /*
     * Unique appointment ID
     */
    private String appointmentID;
    /*
     * Unique doctor ID
     */
    private String doctorID;
    /*
     * Unique patient ID
     */
    private String patientID;
    /*
     * AppointmentOutcomeRecord object
     */
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
    }

    /**
     * Constructor for AppointmentSlot
     * @param date String representing date in format "YYYY-MM-DD"
     * @param time String representing time in format "HH:mm"
     * @param status AppointmentStatus representing one of the Appointment Statuses
     * @param doctorID String representing doctorID
     * @param patientID String representing patientID
     * @param outcome AppointmentOutcomeRecord object
     * @param appointmentID String representing appointmentID
     * @see AppointmentOutcomeRecord
     * @see AppointmentStatus
     */
    public AppointmentSlot(String date, String time, AppointmentStatus status, String doctorID, String patientID, AppointmentOutcomeRecord outcome, String appointmentID) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time,  DateTimeFormatter.ofPattern("HH:mm"));
        this.status = status;
        this.doctorID = doctorID;
        this.patientID = patientID;
        this.appointmentID = appointmentID;
        this.outcome = outcome;
    }
    
    /**
     * Set method for changing patientID
     * @param patientID String representing new patientID
     */
    public void setPatientID(String patientID){
        this.patientID = patientID;
    }

    /**
     * Get method for retrieving patientID
     * @return String containing information of patientID
     */
    public String getPatientID() {
        return this.patientID;
    }

    /**
     * Get method for retrieving appointmentID
     * @return String containing information of appointmentID
     */
    public String getAppointmentID(){
        return this.appointmentID;
    }

    /**
     * Set method for changing doctorID
     * @param doctorID String representing new doctorID
     */
    public void setDoctorID(String doctorID){
        this.doctorID = doctorID;
    }
    /**
     * Get method for retrieving doctorID
     * @return String containing information of doctorID
     */
    public String getDoctorID() {
        return this.doctorID;
    }

    /**
     * Get method for retrieving date
     * @return LocalDate containing information of date
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Get method for retrieving time
     * @return LocalTime containing information of time
     */
    public LocalTime getTime() {
        return this.time;
    }

    /**
     * Get method for retrieving status
     * @return AppointmentStatus containing appointment status
     * @see AppointmentStatus
     */
    public AppointmentStatus getStatus() {
        return this.status;
    }

    /**
     * Set method for changing AppointmentStatus
     * @param status AppointmentStatus representing new status
     * @see AppointmentStatus
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Get method for retrieving AppointmentOutcomeRecord
     * @return AppointmentOutcomeRecord containing information in AppointmentOutcomeRecord
     * @see AppointmentOutcomeRecord
     */
    public AppointmentOutcomeRecord getAppointmentOutcomeRecord() {
        return this.outcome;
    }

    /**
     * Updates information in AppointmentOutcomeRecord attribute
     * @param date LocalDate object containing information on date
     * @param time LocalTime object containing information on time
     * @param serviceType String object containing information on the TypeOfService
     * @param pres Prescription object containing information on prescribed medication and dosage
     * @param consult String object containing information on consultationNotes
     */
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


    /**
     * Returns AppointmentSlot as a String to be added to appointments.csv
     * @return String object containing of AppointmentSlot as a String delimited appropriately
     */
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
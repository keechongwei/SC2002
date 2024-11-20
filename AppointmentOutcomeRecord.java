import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AppointmentOutcomeRecord {
    private LocalDate date;
    private LocalTime time;
    private TypeOfService typeOfService;
    private Prescription prescribedMedication;
    private String consultationNotes;

    // public AppointmentOutcomeRecord(String date, String time,String typeOfService, Prescription prescribedMedication, String consultationNotes) {
    //     this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    //     this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
    //     this.typeOfService = TypeOfService.valueOf(typeOfService);
    //     this.prescribedMedication = prescribedMedication;
    //     this.consultationNotes = consultationNotes;
    // }

    /**
     * Constructor 
     * Creates an AppointmentOutcomeRecord object with the input parameters
     * @param date String containing information of date in "YYYY-MM-DD"
     * @param time String containing information of date in "HH:mm"
     * @param typeOfService TypeOfService type 
     * @param prescribedMedication Prescription type 
     * @param consultationNotes String containing consultation notes for AppointmentSlot
     * @see TypeOfService
     * @see Prescription
     */
    public AppointmentOutcomeRecord(String date, String time,TypeOfService typeOfService, Prescription prescribedMedication, String consultationNotes) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        this.typeOfService = typeOfService;
        this.prescribedMedication = prescribedMedication;
        this.consultationNotes = consultationNotes;
    }

    /**
     * Returns AppointmentOutcomeRecord as a String with Delimiters 
     * Utilised by AppointmentSlot
     * @param void
     * @return AppointmentOutcomeRecord as a String with Delimiters to store in AppointmentSlot
     */
    public String toCSV() {
        return date + "|" + time + "|" + typeOfService + "|" + 
               (prescribedMedication != null ? prescribedMedication.toCSV() : "null") + "|" + 
               consultationNotes;
    }

    /**
     * Creates AppointmentOutcomeRecord from information in appointments.csv
     * @param csvString String with information necessary to create AppointmentOutcomeRecord
     * @return AppointmentOutcomeRecord object created with information from input String
     */
    public static AppointmentOutcomeRecord fromCSV(String csvString) {
        String[] fields = csvString.split("\\|");
        String date = fields[0];
        String time = fields[1];
        TypeOfService typeOfService = TypeOfService.valueOf(fields[2]);
        Prescription prescribedMedication = fields[3].equals("null") ? null : Prescription.fromCSV(fields[3]);
        String consultationNotes = fields[4];
        return new AppointmentOutcomeRecord(date,time,typeOfService, prescribedMedication, consultationNotes);
    }

    /**
     * Get method to return date attribute 
     * @param void
     * @return LocalDate object with information from date attribute
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Set  method to change date attribute 
     * @param date LocalDate object to be set as new date of AppointmentOutcomeRecord
     * @return void
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Get method to return time attribute 
     * @param void
     * @return LocalTime object with information from time attribute
     */
    public LocalTime getTime() {
        return this.time;
    }

    /**
     * Set method to change time attribute 
     * @param time LocalTime object to be set as new time of AppointmentOutcomeRecord
     * @return void
     */
    public void setTime(LocalTime time) {
        this.time = time;
    } 

    /**
     * Get method to return TypeOfService attribute 
     * @param void
     * @return TypeOfService object with information from typeOfService attribute
     */
    public TypeOfService getTypeOfService() {
        return this.typeOfService;
    }

    /**
     * Set  method to change typeOfService attribute 
     * @param tos String to be set as new typeOfService of AppointmentOutcomeRecord
     * @return void
     */
    public void setTypeOfService(TypeOfService tos) {
        this.typeOfService = tos;
    }

    /**
     * Get method to return prescribedMedication attribute 
     * @param void
     * @return Prescription object with information from prescribedMedication attribute
     */
    public Prescription getPrescribedMedication() {
        return this.prescribedMedication;
    }

    /**
     * Set  method to change prescribedMedication attribute 
     * @param pres Prescription to be set as new prescribedMedication of AppointmentOutcomeRecord
     * @return void
     */
    public void setPrescribedMedication(Prescription pres){
        this.prescribedMedication = pres;
    }

    /**
     * Get method to return consultationNotes attribute 
     * @param void
     * @return String object with information from consultationNotes attribute
     */
    public String getConsultationNotes() {
        return this.consultationNotes;
    }

    /**
     * Set method to change consultationNotes attribute 
     * @param notes String to be set as new consultationNotes of AppointmentOutcomeRecord
     * @return void
     */
    public void setConsultationNotes(String notes){
        this.consultationNotes = notes;
    }
}
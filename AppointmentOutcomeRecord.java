import java.lang.ProcessBuilder.Redirect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AppointmentOutcomeRecord {
    private LocalDate date;
    private LocalTime time;
    private TypeOfService typeOfService;
    private Prescription prescribedMedication;
    private String consultationNotes;

    public String toCSV() {
        return date + "|" + time + "|" + typeOfService + "|" + 
               (prescribedMedication != null ? prescribedMedication.toCSV() : "null") + "|" + 
               consultationNotes;
    }

    public static AppointmentOutcomeRecord fromCSV(String csvString) {
        String[] fields = csvString.split("\\|");
        String date = fields[0];
        String time = fields[1];
        TypeOfService typeOfService = TypeOfService.valueOf(fields[2]);
        Prescription prescribedMedication = fields[3].equals("null") ? null : Prescription.fromCSV(fields[3]);
        String consultationNotes = fields[4];
        return new AppointmentOutcomeRecord(date,time,typeOfService, prescribedMedication, consultationNotes);
    }
    
    public AppointmentOutcomeRecord(String date, String time,String typeOfService, Prescription prescribedMedication, String consultationNotes) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        this.typeOfService = TypeOfService.valueOf(typeOfService);
        this.prescribedMedication = prescribedMedication;
        this.consultationNotes = consultationNotes;
    }

    public AppointmentOutcomeRecord(String date, String time,TypeOfService typeOfService, Prescription prescribedMedication, String consultationNotes) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.time = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        this.typeOfService = typeOfService;
        this.prescribedMedication = prescribedMedication;
        this.consultationNotes = consultationNotes;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    } 

    public TypeOfService getTypeOfService() {
        return this.typeOfService;
    }

    public void setTypeOfService(String tos) {
        this.typeOfService = TypeOfService.valueOf(tos.toUpperCase());
    }

    public Prescription getPrescribedMedication() {
        return this.prescribedMedication;
    }

    public void setPrescribedMedication(Prescription pres){
        this.prescribedMedication = pres;
    }

    public String getConsultationNotes() {
        return this.consultationNotes;
    }

    public void setConsultationNotes(String notes){
        this.consultationNotes = notes;
    }
}
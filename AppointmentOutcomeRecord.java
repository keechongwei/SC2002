import java.util.Date;

public class AppointmentOutcomeRecord {
    private Date date;
    private TypeOfService typeOfService;
    private Prescription prescribedMedication;
    private String consultationNotes;

    public AppointmentOutcomeRecord(Date date, TypeOfService typeOfService, Prescription prescribedMedication, String consultationNotes) {
        this.date = date;
        this.typeOfService = typeOfService;
        this.prescribedMedication = prescribedMedication;
        this.consultationNotes = consultationNotes;
    }

    public Date getDate() {
        return date;
    }

    public TypeOfService getTypeOfService() {
        return typeOfService;
    }

    public Prescription getPrescribedMedication() {
        return prescribedMedication;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }
}
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

public class Patient extends User{
    //under medical record:
    // protected String patientID;
    // protected String patientName;
    // protected int dateofBirth;
    // protected String gender;
    // protected String BloodType;
    
    private MedicalRecord medicalRecord;
    //private list of appointmentslots
    
    private static Scanner sc = new Scanner(System.in);

    //patient Constructor
    public Patient(String patientID, String patientName, String dateofBirth, String gender, String bloodType, String phoneNumber, String emailAddress) {
        super(patientID, "password");
        this.medicalRecord = new MedicalRecord(patientID, patientName, dateofBirth, gender, phoneNumber, emailAddress, bloodType);
        this.appointments = new ArrayList<>();
    }

    //creating patient object from csv
    //Patient ID;Name;Date of Birth;Gender;Blood Type;Contact Information
    public static Patient fromCSV(String csvLine) {
        String[] fields = csvLine.split(";");
        if (fields.length < 6) {
            throw new IllegalArgumentException("Invalid CSV line format");
        }

        String patientID = fields[0];
        String name = fields[1];
        String dateOfBirth = fields[2];
        String gender = fields[3]
        String bloodType = fields[4];
        String contactInformation = fields[5];
        String phoneNumber = "";
        String emailAddress = contactInformation;

    return new Patient(patientID, name, dateOfBirth, gender, bloodType, phoneNumber, emailAddress);
    }

    private void viewMedicalRecord() {
    // private String patientID;
    // private String name;
    // private String dateOfBirth;
    // private String gender;
    // private String phoneNumber;
    // private String emailAddress;
    // private String bloodType;
    // private ArrayList<String> pastDiagnoses;
    // private ArrayList<String> pastTreatments;

        //title
        System.out.println("\n" + "=".repeat(50));
        System.out.println("               MEDICAL RECORD");
        System.out.println("=".repeat(50));

        //personal info
        System.out.println("\nPERSONAL INFORMATION:");
        System.out.println("-".repeat(30));
        System.out.printf("%-15s: %s%n", "Patient ID", medicalRecord.getPatientID());
        System.out.printf("%-15s: %s%n", "Name", medicalRecord.getName());
        System.out.printf("%-15s: %s%n", "Date of Birth", medicalRecord.getDateOfBirth());
        System.out.printf("%-15s: %s%n", "Gender", medicalRecord.getGender());
        System.out.printf("%-15s: %s%n", "Blood Type", medicalRecord.getBloodType());

        //contact info
        System.out.println("\nCONTACT INFORMATION:");
        System.out.println("-".repeat(30));
        System.out.printf("%-15s: %s%n", "Phone", medicalRecord.getPhoneNumber());
        System.out.printf("%-15s: %s%n", "Email", medicalRecord.getEmailAddress());

        //past diagnosis
        System.out.println("\nPAST DIAGNOSES AND TREATMENT:");
        System.out.println("-".repeat(30));
        List<String> diagnoses = medicalRecord.getPastDiagnoses();
        List<String> treatments = medicalRecord.getPastTreatments();
        if (diagnoses.isEmpty()) {
            System.out.println("No past diagnoses recorded");
        } else {
            for (int i = 0; i < diagnoses.size(); i++) {
                System.out.printf("%2d. %s%n", (i + 1), diagnoses.get(i));
                System.out.printf("%2d. %s%n", (i + 1), treatments.get(i));
            }
        }
    }

    private void viewAppointmentSlot() {
        ArrayList<AppointmentSlot> as = AppointmentManager.getAppointmentSlots();
        //to be done
    }

    private void scheduleAppointments() {
        viewAppointmentSlot();
        //to be done

        System.out.println("\n=== Schedule New Appointment ===");

        //select doctor
        
        //show avail appt slots

        //create and save the appt

    }

    private void rescheduleAppointment() {

        //to be done
        System.out.println("\n=== Reschedule Appointment ===");

        //show current appts, check if empty 
        List<Appointment> currentAppointments = getCurrentAppointments();

        //select appts to reschedule

        //check new avail appointmentslots for the doc

        //select new slot

        //update appt
    }

    private void cancelAppointment() {
        //to be done

        System.out.println("\n=== Cancel Appointment ===");

        //show current appt

        //select appt to cancel

        //update appt and free appt slot
    }

    private void viewAppointmentStatus() {
        //to be done

        System.out.println("\n=== Current Appointments Status ===");

        List<Appointment> currentAppointments = getCurrentAppointments();
        if (currentAppointments.isEmpty()) {
            System.out.println("No current appointments!");
            return;
        }

        for (Appointment appointment : currentAppointments) {
            System.out.printf("\nAppointment with Dr. %s%n", appointment.getDoctor().getName());
            System.out.printf("Date/Time: %s%n", appointment.getSlot().getDateTime());
            System.out.printf("Status: %s%n", appointment.getStatus());
        }

    }

    private void viewAppointmentOutcomeRecord() {
        //to be done
        System.out.println("\n=== Appointment Outcome Record ===");
    }


    

}


   

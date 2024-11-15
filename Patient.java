import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


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
        //this.appointments = new ArrayList<>();
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
        String gender = fields[3];
        String bloodType = fields[4];
        String contactInformation = fields[5];
        String phoneNumber = "";
        String emailAddress = contactInformation;

    return new Patient(patientID, name, dateOfBirth, gender, bloodType, phoneNumber, emailAddress);
    }

    public void viewMedicalRecord() {
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

    public void viewAvailAppointmentSlot() {//make it repeatedly ask user to select aft not able to select avail slots
        //title
        System.out.println("\n=== Available Appointment Slots ===");

        //select avail slots
        List<AppointmentSlot> availSlots = AppointmentManager.getAvailableAppointments();

        if(availSlots.isEmpty()) {
            System.out.println("No appointment slots available");
            return;
        }

        System.out.printf("%-5s %-12s %-8s %-10s %-10s%n", "Appointment ID.", "Date", "Time", "Doctor", "Status");
        System.out.println("-".repeat(50));

        //show avail appt slots
        for(AppointmentSlot slot : availSlots) {
            System.out.printf("%-5s %-12s %-8s %-10s %-10s%n", slot.getAppointmentID(), 
            slot.getDate(), slot.getTime(), slot.getDoctorID(), slot.getStatus());
        }
    }

    public void scheduleAppointments() {
        //title
        System.out.println("\n=== Schedule New Appointment ===");

        //select doctor buy ID
        System.out.println("Enter Doctor ID to schedule appointment (e.g D001): ");
        String selectedDoctorID = sc.nextLine().trim().toUpperCase();

        //get avail slots for selected doctor
        List<AppointmentSlot> availSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAvailableAppointments()) {
            if(slot.getDoctorID() == selectedDoctorID) {
                availSlots.add(slot);   
            }
        }

        if(availSlots.isEmpty()) {
            System.out.println("No available appointment slots.");
            return;
        }
        
        //show avail appt slots
        System.out.println("\nAvailable Appointment Slots: ");
        for (int i=0; i<availSlots.size(); i++) {
            AppointmentSlot slot = availSlots.get(i);
            System.out.printf("%d. Date: %s, Time: %s, Doctor: %s, Status: %s%n", i+1, slot.getDate(), slot.getTime(), slot.getDoctorID(), slot.getStatus());
        }

        //create and save the appt
        System.out.print("\nSelect appointment slot (enter number): ");
        int choice = sc.nextInt();
        sc.nextLine();

        if(choice<1 || choice>availSlots.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        //update selected slot
        AppointmentSlot selectedSlot = availSlots.get(choice-1);

        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getAppointmentID() == selectedSlot.getAppointmentID()) {
                slot.setStatus(AppointmentStatus.PENDING);
                slot.setPatientID(medicalRecord.getPatientID());
                System.out.println("Appointment scheduled, status: pending.");
            }
        }
     }

    public void rescheduleAppointment() { // need to clear patientID

        //title
        System.out.println("\n=== Reschedule Appointment ===");

        //show current appts, check if empty 
        List<AppointmentSlot> curSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAppointmentsByPatient(medicalRecord.getPatientID())) {
            if(slot.getStatus() == AppointmentStatus.PENDING || slot.getStatus() == AppointmentStatus.CONFIRMED) {
                curSlots.add(slot);   
            }
        }

        if(curSlots.isEmpty()) {
            System.out.println("No scheduled appointment slots.");
            return;
        }

        //show cur appts
        System.out.println("\nYour Current Appointments:");
        for (int i = 0; i < curSlots.size(); i++) {
            AppointmentSlot slot = curSlots.get(i);
            System.out.printf("%d. Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }

        //select appts to reschedule
        System.out.print("\nSelect appointment to reschedule (enter number): ");
        int choice = sc.nextInt();
        sc.nextLine();

        if(choice<1 || choice>curSlots.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        AppointmentSlot oldSlot = curSlots.get(choice - 1);

        //check new avail appointmentslots for the doc
        List<AppointmentSlot> availSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAvailableAppointments()) {
            if(slot.getDoctorID() == oldSlot.getDoctorID()) {
                availSlots.add(slot);   
            }
        }

        if (availSlots.isEmpty()) {
            System.out.println("No available slots for rescheduling.");
            return;
        }

        System.out.println("\nAvailable Slots:");
        for (int i = 0; i < availSlots.size(); i++) {
            AppointmentSlot slot = availSlots.get(i);
            System.out.printf("%d. Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }

        //select new slot
        System.out.print("\nSelect new appointment slot (enter number): ");
        choice = sc.nextInt();
        sc.nextLine(); // Clear buffer

        if (choice < 1 || choice > availSlots.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        AppointmentSlot newSlot = availSlots.get(choice-1);

        //update appt
        //reset oldslot
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getAppointmentID() == oldSlot.getAppointmentID()) {
                slot.setStatus(AppointmentStatus.AVAILABLE);
                slot.setPatientID("");
                System.out.println("Current appointment cancelled, rescheduling to new appointment...");
            }
        }

        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getAppointmentID() == newSlot.getAppointmentID()) {
                slot.setStatus(AppointmentStatus.PENDING);
                slot.setPatientID(medicalRecord.getPatientID());
                System.out.println("Appointment rescheduled successfully!");
                System.out.printf("%s. Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                "New appointment:",
                slot.getDate(),
                slot.getTime(), 
                slot.getDoctorID(),
                slot.getStatus()
                );
            }
        }

    }

    public void cancelAppointment() {// might have to improve choice section to go back and reprompt response

        //title
        System.out.println("\n=== Cancel Appointment ===");

        //show current appt
        List<AppointmentSlot> curSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAppointmentsByPatient(medicalRecord.getPatientID())) {
            if(slot.getStatus() == AppointmentStatus.PENDING || slot.getStatus() == AppointmentStatus.CONFIRMED) {
                curSlots.add(slot);   
            }
        }

        if(curSlots.isEmpty()) {
            System.out.println("No current appointments to cancel.");
            return;
        }

        System.out.println("\nYour Current Appointments:");
        for (int i = 0; i < curSlots.size(); i++) {
            AppointmentSlot slot = curSlots.get(i);
            System.out.printf("%d. Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }

        //select appt to cancel
        System.out.print("\nSelect appointment to cancel (enter number): ");
        int choice = sc.nextInt();
        sc.nextLine();

        if(choice<1 || choice>curSlots.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        AppointmentSlot selectedSlot = curSlots.get(choice - 1);

        //update appt and free appt slot
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getAppointmentID() == selectedSlot.getAppointmentID()) {
                slot.setStatus(AppointmentStatus.AVAILABLE);
                slot.setPatientID("");
                System.out.println("Appointment cancelled successfully!");
            }
        }
    }

    public void viewAppointmentStatus() {
        //to be done

        //title
        System.out.println("\n=== Current Appointments Status ===");

        //show cur appts
        List<AppointmentSlot> curSlots = AppointmentManager.getAppointmentsByPatient(medicalRecord.getPatientID());

        if(curSlots.isEmpty()) {
            System.out.println("No current appointments.");
            return;
        }

        for (int i = 0; i < curSlots.size(); i++) {
            AppointmentSlot slot = curSlots.get(i);
            System.out.printf("%d. Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }
    }

    public void viewAppointmentOutcomeRecord() {
        //to be done

        //title
        System.out.println("\n=== Appointment Outcome Record ===");

        //get completed slots
        List<AppointmentSlot> curSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAppointmentsByPatient(medicalRecord.getPatientID())) {
            if(slot.getStatus() == AppointmentStatus.COMPLETED) {
                curSlots.add(slot);   
            }
        }

        if(curSlots.isEmpty()) {
            System.out.println("No appointment outcome records.");
            return;
        }

        for(AppointmentSlot slot : curSlots) {
            AppointmentOutcomeRecord record = slot.getAppointmentOutcomeRecord();
            System.out.printf("Date: %s%n", record.getDate());
            System.out.printf("Time: %s%n", record.getTime());
            System.out.printf("Service Type: %s%n", record.getTypeOfService());
            
            
            System.out.println("\nPrescribed Medication:");
            System.out.printf("- %s (Status: %s)%n", 
                record.getPrescribedMedication().getMedicationName(),
                record.getPrescribedMedication().getStatus());
            
        
            System.out.println("\nConsultation Notes:");
            System.out.println(record.getConsultationNotes());
            
        }
    }


    public static void main(String[] args) {
        // Create a patient from CSV data
        Patient patient = Patient.fromCSV("P001;John Doe;1990-05-15;Male;AB+;123-456-7890;johndoe@email.com");
    
        // View medical record
        patient.viewMedicalRecord();
    
        // View available appointment slots
        patient.viewAvailAppointmentSlot();
    
        // Schedule an appointment
        patient.scheduleAppointments();
    
        // Reschedule an appointment
        patient.rescheduleAppointment();
    
        // Cancel an appointment
        patient.cancelAppointment();
    
        // View appointment status
        patient.viewAppointmentStatus();
    
        // View appointment outcome record
        patient.viewAppointmentOutcomeRecord();
    }

}


   

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;



public class Patient extends User{
    //under medical record:
    // protected String patientID;
    // protected String patientName;
    // protected int dateofBirth;
    // protected String gender;
    // protected String BloodType;
    private MedicalRecord medicalRecord;
    
    private static Scanner sc = new Scanner(System.in);

    //patient Constructor
    public Patient(String patientID, String patientName, String dateofBirth, String gender, String bloodType, String phoneNumber, String emailAddress) {
        super(patientID, "password");
        this.medicalRecord = new MedicalRecord(patientID, patientName, dateofBirth, gender, phoneNumber, emailAddress, bloodType);
        this.medicalRecord.setPatient(this); 
    }

    public Patient(String patientID, String patientName, String dateofBirth, String gender, String bloodType, String phoneNumber, String emailAddress, ArrayList<String> pastDiagnoses, ArrayList<String> pastTreatments) {
        super(patientID, "password");
        this.medicalRecord = new MedicalRecord(patientID, patientName, dateofBirth, gender, phoneNumber, emailAddress, bloodType, pastDiagnoses, pastTreatments);
        this.medicalRecord.setPatient(this); 
    }

    public void printMenu(){
        int choice = 0;
        while(choice != 10){
            System.out.println("=== PATIENT MENU, ENTER CHOICE ===");
            System.out.println("(1) View Medical Record");
            System.out.println("(2) Update Personal Information");
            System.out.println("(3) View Available Appointment Slots");
            System.out.println("(4) Schedule An Appointment");
            System.out.println("(5) Reschedule An Appointment");
            System.out.println("(6) Cancel An Appointment");
            System.out.println("(7) View Scheduled Appointments");
            System.out.println("(8) View Past Appointment Outcome Records");
            System.out.println("(9) View and Process Bills");
            System.out.println("(10) Logout");
            choice = InputValidator.getIntegerInput("\nEnter selection from 1-10: ", 1, 10);

            switch(choice){
                case 1:
                this.viewMedicalRecord();
                break;
                case 2:
                this.updatePersonalInfo();// Update Personal Information
                break;
                case 3:
                this.viewAvailAppointmentSlot();// View Available Appointment Slots
                break;
                case 4:
                this.scheduleAppointments();// Schedule An Appointment
                break;
                case 5:
                this.rescheduleAppointment();// Reschedule An Appointment
                break;
                case 6:
                this.cancelAppointment();// Cancel An Appointment
                break;
                case 7:
                this.viewAppointmentStatus();// View Scheduled Appointments
                break;
                case 8:
                this.viewAppointmentOutcomeRecord();// View Past Appointments Outcome Record
                break;
                case 9:
                BillingSystem.BillingMenu();// view bills, process bills
                break;
                case 10:
                System.out.println("Logging out...");
                break;
            }
        }
    }

    public void setPassword(String password){
        super.setPassword(password);
    }

    public MedicalRecord getMedicalRecord() {
        return this.medicalRecord;
    }

    //update contact info in medical record
    public void updatePersonalInfo() {
        int choice;

        do{
            System.out.println("\n=== Update Personal Information ===");
            System.out.println("1. Update phone number");
            System.out.println("2. Update email address");
            System.out.println("3. Exit");
            System.out.println("=".repeat(35));
            choice = InputValidator.getIntegerInput("\nEnter your choice (1-3): ", 1, 3);

            switch (choice) {
                case 1: //update phone number
                    String newPhoneNumber = InputValidator.getPhoneNumber("Enter new phone number (8-12 digits): ");
                    this.medicalRecord.setPhoneNumber(newPhoneNumber);
                    System.out.println("Phone number updated. New phone number: "  + this.medicalRecord.getPhoneNumber());
                    break;

                case 2:
                    String newEmail = InputValidator.getEmailAddress("Enter new email address (eg. john.doe@example.com): ");
                    this.medicalRecord.setEmailAddress(newEmail);
                    System.out.println("Email address updated. New email address: "  + this.medicalRecord.getEmailAddress());
                    break;

                case 3:
                    System.out.println("Exiting personal information update...\n");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number from 1-3\n");
                    break;

            }

        } while(choice <= 3);

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
        System.out.printf("%-15s: %s%n", "Patient ID", this.medicalRecord.getPatientID());
        System.out.printf("%-15s: %s%n", "Name", this.medicalRecord.getName());
        System.out.printf("%-15s: %s%n", "Date of Birth", this.medicalRecord.getDateOfBirth());
        System.out.printf("%-15s: %s%n", "Gender", this.medicalRecord.getGender());
        System.out.printf("%-15s: %s%n", "Blood Type", this.medicalRecord.getBloodType());

        //contact info
        System.out.println("\nCONTACT INFORMATION:");
        System.out.println("-".repeat(30));
        System.out.printf("%-15s: %s%n", "Phone Number", this.medicalRecord.getPhoneNumber());
        System.out.printf("%-15s: %s%n", "Email Address", this.medicalRecord.getEmailAddress());

        //past diagnoses and treatment
        System.out.println("\nPAST DIAGNOSES AND TREATMENT:");
        System.out.println("-".repeat(30));
        List<String> diagnoses = this.medicalRecord.getPastDiagnoses();
        List<String> treatments = this.medicalRecord.getPastTreatments();
        if (diagnoses.isEmpty()) {
            System.out.println("No past diagnoses recorded\n");
        } else {
            for (int i = 0; i < diagnoses.size(); i++) {
                System.out.printf("%-15s: %s%n", "Past Diagnoses", diagnoses.get(i));
                System.out.printf("%-15s: %s%n", "Past Treatments", treatments.get(i));
                System.out.println(".".repeat(30));
            }
            System.out.println();
        }
    }

    public void viewAvailAppointmentSlot() {
        //title
        System.out.println("\n=== Available Appointment Slots ===");

        //select avail slots
        List<AppointmentSlot> availSlots = AppointmentManager.getAvailableAppointments();

        if(availSlots.isEmpty()) {
            System.out.println("No appointment slots available");
            return;
        }

        System.out.printf("%-15s %-12s %-8s %-10s %-10s%n", "Appointment ID.", "Date", "Time", "Doctor", "Status");
        System.out.println("-".repeat(50));

        //show avail appt slots
        for(AppointmentSlot slot : availSlots) {
            System.out.printf("%-15s %-12s %-8s %-10s %-10s%n", slot.getAppointmentID(), 
            slot.getDate(), slot.getTime(), slot.getDoctorID(), slot.getStatus());
        }
        System.out.println();
    }

    public void scheduleAppointments() {
        //title
        System.out.println("\n=== Schedule New Appointment ===");

        //select doctor buy ID
        String selectedDoctorID = InputValidator.getDoctorId("\nEnter Doctor ID (eg. D001) to schedule appointment: ");

        //get avail slots for selected doctor
        List<AppointmentSlot> availSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAvailableAppointments()) {
            if(slot.getDoctorID().equals(selectedDoctorID)) {
                availSlots.add(slot);   
            }
        }

        if(availSlots.isEmpty()) {
            System.out.println("No available appointment slots.\n");
            return;
        }
        
        //show avail appt slots
        System.out.println("\nAvailable Appointment Slots: ");
        for (int i=0; i<availSlots.size(); i++) {
            AppointmentSlot slot = availSlots.get(i);
            System.out.printf("%d. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n", i+1, slot.getAppointmentID(), slot.getDate(), slot.getTime(), slot.getDoctorID(), slot.getStatus());
        }

        //create and save the appt
        int choice = InputValidator.getIntegerInput("\nSelect appointment slot (enter number): ", 1, availSlots.size());

        //update selected slot
        AppointmentSlot selectedSlot = availSlots.get(choice-1);

        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            //find the slot by Appt ID
            if(slot.getAppointmentID().equals(selectedSlot.getAppointmentID())) {
                //update the details
                slot.setStatus(AppointmentStatus.PENDING);
                slot.setPatientID(this.medicalRecord.getPatientID());
                System.out.println("Appointment scheduled, status: pending.");
                //print the new details to user
                System.out.printf("Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n", slot.getAppointmentID(), slot.getDate(), slot.getTime(), slot.getDoctorID(), slot.getStatus());
                //update csv
                AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
            }
        }
        System.out.println();
     }

    public void rescheduleAppointment() { 

        //title
        System.out.println("\n=== Reschedule Appointment ===");

        //show current appts, check if empty 
        List<AppointmentSlot> curSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAppointmentsByPatient(this.medicalRecord.getPatientID())) {
            if(slot.getStatus().equals(AppointmentStatus.PENDING) || slot.getStatus().equals(AppointmentStatus.CONFIRMED)) {
                curSlots.add(slot);   
            }
        }
        //error checking
        if(curSlots.isEmpty()) {
            System.out.println("No scheduled appointment slots.\n");
            return;
        }

        //print cur appts
        System.out.println("\nYour Current Appointments:");
        for (int i = 0; i < curSlots.size(); i++) {
            AppointmentSlot slot = curSlots.get(i);
            System.out.printf("%d. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getAppointmentID(),
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }

        //select appts to reschedule
        int choice = InputValidator.getIntegerInput("\nSelect appointment to reschedule (enter number): ", 1, curSlots.size());

        AppointmentSlot oldSlot = curSlots.get(choice - 1);

        //check new avail appointmentslots for the doc
        List<AppointmentSlot> availSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAvailableAppointments()) {
            if(slot.getDoctorID().equals(oldSlot.getDoctorID())) {
                availSlots.add(slot);   
            }
        }

        if (availSlots.isEmpty()) {
            System.out.println("No available slots for rescheduling.\n");
            return;
        }

        System.out.println("\nAvailable Slots:");
        for (int i = 0; i < availSlots.size(); i++) {
            AppointmentSlot slot = availSlots.get(i);
            System.out.printf("%d. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getAppointmentID(),
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }

        //select new slot
        choice = InputValidator.getIntegerInput("\nSelect new appointment slot (enter number): ", 1, availSlots.size());

        AppointmentSlot newSlot = availSlots.get(choice-1);

        //update appt
        //reset oldslot
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getAppointmentID().equals(oldSlot.getAppointmentID())) {
                slot.setStatus(AppointmentStatus.AVAILABLE);
                slot.setPatientID("");
                System.out.println("Current appointment cancelled, rescheduling to new appointment...");
                System.out.printf("%s. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n", "Cancelled appointment: ", slot.getAppointmentID(), slot.getDate(), slot.getTime(), slot.getDoctorID(), "CANCELLED");
            }
        }

        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getAppointmentID().equals(newSlot.getAppointmentID())) {
                slot.setStatus(AppointmentStatus.PENDING);
                slot.setPatientID(this.medicalRecord.getPatientID());
                System.out.println("Appointment rescheduled successfully!");
                System.out.printf("%s. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                "New appointment:",
                slot.getAppointmentID(),
                slot.getDate(),
                slot.getTime(), 
                slot.getDoctorID(),
                slot.getStatus()
                );
            }
        }
        System.out.println();

    }

    public void cancelAppointment() {

        //title
        System.out.println("\n=== Cancel Appointment ===");

        //show current appt
        List<AppointmentSlot> curSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAppointmentsByPatient(this.medicalRecord.getPatientID())) {
            if(slot.getStatus().equals(AppointmentStatus.PENDING) || slot.getStatus().equals(AppointmentStatus.CONFIRMED)) {
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
            System.out.printf("%d. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getAppointmentID(),
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }

        //select appt to cancel
        int choice = InputValidator.getIntegerInput("\nSelect appointment to cancel (enter number): ", 1, curSlots.size());

        AppointmentSlot selectedSlot = curSlots.get(choice - 1);

        //update appt and free appt slot
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getAppointmentID().equals(selectedSlot.getAppointmentID())) {
                slot.setStatus(AppointmentStatus.AVAILABLE);
                slot.setPatientID("");
                System.out.println("Appointment cancelled successfully!");
                System.out.printf("%s. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n", "Cancelled appointment: ", slot.getAppointmentID(), slot.getDate(), slot.getTime(), slot.getDoctorID(), "CANCELLED");
            }
        }
        System.out.println();
    }

    public void viewAppointmentStatus() {
        //to be done

        //title
        System.out.println("\n=== Current Appointments Status ===");

        //show cur appts
        List<AppointmentSlot> curSlots = AppointmentManager.getAppointmentsByPatient(this.medicalRecord.getPatientID());

        if(curSlots.isEmpty()) {
            System.out.println("No current appointments.");
            return;
        }

        for (int i = 0; i < curSlots.size(); i++) {
            AppointmentSlot slot = curSlots.get(i);
            System.out.printf("%d. Appointment ID: %s, Date: %s, Time: %s, Doctor: %s, Status: %s%n",
                i + 1,
                slot.getAppointmentID(),
                slot.getDate(),
                slot.getTime(),
                slot.getDoctorID(),
                slot.getStatus());
        }
        System.out.println();
    }

    public void viewAppointmentOutcomeRecord() {

        //title
        System.out.println("\n=== Appointment Outcome Record ===");

        //get completed slots
        List<AppointmentSlot> curSlots = new ArrayList<>();
        for (AppointmentSlot slot :  AppointmentManager.getAppointmentsByPatient(this.medicalRecord.getPatientID())) {
            if(slot.getStatus().equals(AppointmentStatus.COMPLETED)) {
                curSlots.add(slot);   
            }
        }

        if(curSlots.isEmpty()) {
            System.out.println("No appointment outcome records.\n");
            return;
        }

        for(AppointmentSlot slot : curSlots) {
            AppointmentOutcomeRecord record = slot.getAppointmentOutcomeRecord();
            System.out.printf("Appointment ID: %s%n", slot.getAppointmentID());
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

}


   

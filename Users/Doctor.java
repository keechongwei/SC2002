package Users;
import java.util.Scanner;

import appointment.AppointmentOutcomeRecord;
import appointment.AppointmentSlot;
import enums.AppointmentStatus;
import enums.PrescriptionStatus;
import enums.TypeOfService;
import managers.AppointmentManager;
import managers.InventoryManager;
import managers.PatientManager;
import managers.csvhandlers.AppointmentCSVHandler;
import utility.InputValidator;
import utility.MedicalRecord;
import utility.Medication;
import utility.Prescription;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A Doctor class, a subclass of Staff
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class Doctor extends Staff{
    /**
     * List of patients under the doctor's care
     */
    private List<Patient> patientList = new ArrayList<>();
    
    /**
     * Scanner object to read inputs
     */
    private static Scanner sc = new Scanner(System.in);
    
    /**
     * Constructor for Doctor object
     * @param HospitalID Unique HospitalID
     * @param doctorName Doctor's Name
     * @param gender Doctor's Gender
     * @param age Doctor's 
     */
    public Doctor(String HospitalID, String doctorName, String gender, String age) {
        super(HospitalID,"password");
        this.name = doctorName;
        this.gender = gender;
        this.age = age;
    }

    /**
     * Function to add Patient with confirmed appointments with Doctor to the List of Patients under Doctor's care
     */
    public void addPatientsUnderCare(){
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray){
            if (slot.getDoctorID().equals(this.getHospitalID()) && slot.getStatus().equals(AppointmentStatus.CONFIRMED)){
                this.patientList.add(PatientManager.findPatient(slot.getPatientID()));
            }
        }
    }

    /**
     * Function to print Menu for Doctor
     */
    public void printMenu(){
        int choice = 0;
        while(choice != 8){
            System.out.println("=== DOCTOR MENU, ENTER CHOICE ===");
            System.out.println("(1) View Patient Medical Record");
            System.out.println("(2) Update Patient Medical Record");
            System.out.println("(3) View Personal Schedule");
            System.out.println("(4) Set Availability For Appointments");
            System.out.println("(5) Accept Or Decline Appointment Requests");
            System.out.println("(6) View Upcoming Appointments");
            System.out.println("(7) Record Appointment Outcome");
            System.out.println("(8) Logout");
            choice = sc.nextInt();
            switch(choice){
                case 1:
                this.viewPatientRecords();
                break;
                case 2:
                this.updatePatientRecord();
                break;
                case 3:
                this.viewPersonalSchedule(); // View Personal Schedule
                break;
                case 4:
                this.setAvailabilityForAppointments();// Set Availability For Appointments
                break;
                case 5:
                this.acceptOrDeclineAppointments();// Accept Or Decline Appointment Requests
                break;
                case 6:
                this.viewUpcomingAppointment();
                break;
                case 7:
                this.makeAppointmentOutcomeRecord();
                break;
                case 8:
                System.out.println("Logging out...");
                break;
            }
        }
    }

    /**
     * Adds a patient to the list of patients under Doctor's care
     * @param patient Patient to be added to list
     * @see Patient
     */
    public void addPatient(Patient patient) {
        patientList.add(patient);
    }

    /**
     * View the Medical Record of Patients under Doctor's Care
     */
    public void viewPatientRecords(){
        if (patientList.isEmpty()) {
            System.out.println("No Patient Record Found");
            return;
        }
        else{
            for (Patient p : patientList){
                p.viewMedicalRecord();
                }
            }
    }
    
    /**
     * Sets Availability for Appointments
     * If AppointmentSlot is PENDING, it will be set as UNAVAILABLE
     * @see AppointmentSlot
     * @see AppointmentStatus
     */
    public void setAvailabilityForAppointments() {
        List <AppointmentSlot> schedule = AppointmentManager.getAppointmentsByDoctor(super.getHospitalID());
        System.out.println("Manage Availability for Appointments:");
        System.out.println("(1) Set Slot as Available");
        System.out.println("(2) Set Slot as Unavailable");
        int pick = InputValidator.getIntegerInput("Enter your choice: ", 1, 2);
        LocalDate date = InputValidator.getDateInput("Enter Date");
        if (pick == 1) {
        System.out.println("YOUR CURRENT UNAVAILABLE SLOTS: ");
        for (AppointmentSlot slot : schedule){
            if (slot.getDate().equals(date) && slot.getStatus().equals(AppointmentStatus.UNAVAILABLE)){
                System.out.printf("Appointment ID: %s, Time: %s\n", slot.getAppointmentID(), slot.getTime().toString());
            }
        }

        String choice = InputValidator.getAppointmentId("Enter Appointment ID of Slot To Be Added (E.g APT1): ");
        for (AppointmentSlot changedslot : schedule){
            if (changedslot.getAppointmentID().equals(choice)){
                changedslot.setStatus(AppointmentStatus.AVAILABLE);
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray){
                    if (slot.getAppointmentID().equals(changedslot.getAppointmentID())){
                        slot.setStatus(AppointmentStatus.AVAILABLE);
                        System.out.println("Slot Made Available!");
                        AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
                    }
                }
                return;
        }
        }System.out.println("No matching slot found for the given Appointment ID.");
    } else if (pick==2){
        System.out.println("YOUR CURRENT AVAILABLE SLOTS: ");
        for (AppointmentSlot slot : schedule){
            if (slot.getDate().equals(date) && slot.getStatus().equals(AppointmentStatus.AVAILABLE)){
                System.out.printf("Appointment ID: %s, Time: %s\n", slot.getAppointmentID(), slot.getTime().toString());
            }
        }
        System.out.println("Enter Appointment ID of Slot To Be Removed (E.g APT1): ");
        String choice = sc.nextLine().trim();
        for (AppointmentSlot changedslot : schedule){
            if (changedslot.getAppointmentID().equals(choice) && changedslot.getDate().equals(date)){
                changedslot.setStatus(AppointmentStatus.UNAVAILABLE);
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray){
                    if (slot.getAppointmentID().equals(changedslot.getAppointmentID())){
                        slot.setStatus(AppointmentStatus.UNAVAILABLE);
                        System.out.println("Slot Made Unavailable!");
                        AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
                    }
                }
        }
        }
    }}

    /**
     * Prints out Doctor's upcoming appointments and available AppointmentSlots
     * @see AppointmentSlot
     */
    public void viewPersonalSchedule() {
        System.out.println("=== Doctor's Personal Schedule ===");
        viewUpcomingAppointment();
        System.out.println("=== AVAILABLE SLOTS ===");
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.AVAILABLE){
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate().toString());
                System.out.println("Time: " + slot.getTime().toString());
                System.out.println("----------------------------------------------");
            }
        }
    }

    /**
     * Updates details in Medical Record of Patient under Care
     * @see MedicalRecord
     * @see Patient
     */
    public void updatePatientRecord() {
    System.out.print("Enter patient ID to update their record: ");
    String id = sc.nextLine();
    Patient patientToUpdate = null;
    for (Patient patient : patientList) {
    if (patient.getMedicalRecord().getPatientID().equalsIgnoreCase(id)) { // Case-insensitive match
        patientToUpdate = patient; // Found the patient
        break; // Exit the loop once the patient is found
    }
}
    if (patientToUpdate == null) {
        System.out.println("The patient ID you entered does not match your patient list: " + id);
        return;
    }


    // Update the patient's records
    System.out.print("Enter the diagnosis for the patient: ");
    String diagnosis = sc.nextLine();

    System.out.print("Enter the treatment plan for the patient: ");
    String treatmentPlan = sc.nextLine();

    // Update appointment status and patient records
    //matchedSlot.setStatus(AppointmentStatus.COMPLETED); // Update the slot's status
    patientToUpdate.getMedicalRecord().getPastDiagnoses().add(diagnosis);
    patientToUpdate.getMedicalRecord().getPastTreatments().add(treatmentPlan);

    System.out.println("Patient record updated successfully for ID: " + id);
}
    
    /**
     * Prints upcoming appointments of Doctor
     */
    public void viewUpcomingAppointment(){
        System.out.println("Doctor's Upcoming Appointments");
        boolean hasAppointments = false;
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.CONFIRMED) {
                hasAppointments = true; 
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate().toString());
                System.out.println("Time: " + slot.getTime().toString());
                System.out.println("PatientID: " + slot.getPatientID());
                System.out.println("----------------------------------------------");
            }
        }
        if (!hasAppointments) {
            System.out.println("No upcoming appointments found.");
        }
    }

    /**
     * Function for Doctor to accept or decline Pending Appointments
     */
    public void acceptOrDeclineAppointments(){
        System.out.println("PENDING APPOINTMENTS");
        for (AppointmentSlot slot : AppointmentManager.getAppointmentsByDoctor(super.getHospitalID())) {
            if (slot.getStatus() == AppointmentStatus.PENDING) {
                System.out.println("----------------------------------------------");
                System.out.println("Appointment ID: " + slot.getAppointmentID());
                System.out.println("Date: " + slot.getDate().toString());
                System.out.println("Time: " + slot.getTime().toString());
                System.out.println("PatientID: " + slot.getPatientID());
                System.out.println("----------------------------------------------");
            }
        }
    
        int choice = 0;
        String choice2 = "";
        while (choice <3){
            System.out.println("(1) ACCEPT APPOINTMENT");
            System.out.println("(2) DECLINE APPOINTMENT");
            System.out.println("(3) QUIT");
            choice = InputValidator.getIntegerInput("Choose by number: ",1,3);
            switch(choice){
                case 1:
                System.out.println("PENDING APPOINTMENTS");
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
                    if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.PENDING){ 
                        System.out.println("----------------------------------------------");
                        System.out.println("Appointment ID: " + slot.getAppointmentID());
                        System.out.println("Date: " + slot.getDate().toString());
                        System.out.println("Time: " + slot.getTime().toString());
                        System.out.println("PatientID: " + slot.getPatientID());
                        System.out.println("----------------------------------------------");
                    }
                }
                System.out.println("Enter Appointment ID to Accept: ");
                choice2 = sc.nextLine().trim();
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
                    if (slot.getAppointmentID().equals(choice2) ){ 
                        slot.setStatus(AppointmentStatus.CONFIRMED);
                        this.patientList.add(PatientManager.findPatient(slot.getPatientID()));
                        AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
                        System.out.println("Appointment ID " + choice2 + " has been CONFIRMED.");
                    }
                }
                break;
                case 2:
                System.out.println("PENDING APPOINTMENTS");
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
                    if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.PENDING){ 
                        System.out.println("----------------------------------------------");
                        System.out.println("Appointment ID: " + slot.getAppointmentID());
                        System.out.println("Date: " + slot.getDate().toString());
                        System.out.println("Time: " + slot.getTime().toString());
                        System.out.println("PatientID: " + slot.getPatientID());
                        System.out.println("----------------------------------------------");
                    }
                }
                System.out.println("Enter Appointment ID to Decline: ");
                choice2 = sc.nextLine().trim();
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
                    if (slot.getAppointmentID().equals(choice2)){ 
                        slot.setStatus(AppointmentStatus.CANCELLED);
                        AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
                        System.out.println("Appointment ID " + choice2 + " has been DECLINED.");
                    }
                }
                break;
        }
    }
}  

    /**
     *  Function to make AppointmentOutcomeRecord for COMPLETED AppointmentSlots
     * @see AppointmentOutcomeRecord
     * @see AppointmentSlot
     */
    public void makeAppointmentOutcomeRecord(){
        List<AppointmentSlot> confirmedAppointmentSlots;
        confirmedAppointmentSlots = AppointmentManager.getAppointmentsByDoctor(this.getHospitalID());
        String id = "";
        boolean validpatientID = false;
        boolean validAppointmentID = false;
        boolean validService = false;
        boolean validMedication = false;
        System.out.println("Patient ID to update his/her record[E.g P1001]:");
        id = sc.nextLine().trim();
        while (!validpatientID){
            for (Patient p : PatientManager.allPatients){
                if (p.getHospitalID().equals(id)){
                    validpatientID = true;
                    break;
                }
            }
            if(validpatientID){
                break;
            }
            System.out.println("Invalid Patient ID");
            System.out.println("Patient ID to update his/her record[E.g P1001]:");
            id = sc.nextLine().trim();
        }
        confirmedAppointmentSlots.retainAll(AppointmentManager.getAppointmentsByPatient(id));
        confirmedAppointmentSlots.removeIf(slot -> slot.getStatus() != AppointmentStatus.CONFIRMED);
        System.out.println("LIST OF APPOINTMENTS");
        System.out.println("----------------------------------------------");    
        for (AppointmentSlot slot : confirmedAppointmentSlots){
            System.out.println("Appointment ID:" + slot.getAppointmentID());
            System.out.println("Date:" + slot.getDate().toString());
            System.out.println("Time:" + slot.getTime().toString());
            System.out.println("----------------------------------------------");
        }
        System.out.println("ENTER APPOINTMENT ID OF APPOINTMENT TO MAKE APPOINTMENT OUTCOME RECORD FOR [E.g APT1]: ");
        String selectedAppointmentID = sc.nextLine().trim().toUpperCase();
        while (!validAppointmentID){
            for (AppointmentSlot slot : confirmedAppointmentSlots){
                if (slot.getAppointmentID().equals(selectedAppointmentID)){
                    validAppointmentID = true;
                    break;
                }
            }
            if(validAppointmentID){
                break;
            }
            System.out.println("Invalid Appointment ID");
            System.out.println("LIST OF APPOINTMENTS");
            System.out.println("----------------------------------------------");    
            for (AppointmentSlot slot : confirmedAppointmentSlots){
                System.out.println("Appointment ID:" + slot.getAppointmentID());
                System.out.println("Date:" + slot.getDate().toString());
                System.out.println("Time:" + slot.getTime().toString());
                System.out.println("----------------------------------------------");
            }
            selectedAppointmentID = InputValidator.getAppointmentId("ENTER APPOINTMENT ID OF APPOINTMENT TO MAKE APPOINTMENT OUTCOME RECORD FOR [E.g APT1]:\n");
        }
        System.out.println("The type of service of this diagnosis for the patient:");
        String serviceType = sc.nextLine().trim();
        while (!validService){
            for (TypeOfService tos : TypeOfService.values()){
                if (serviceType.equalsIgnoreCase(tos.toString())){
                    validService = true;
                    break;
                }
            }
            if(validService){
                break;
            }
            System.out.println("Invalid Type Of Service");
            System.out.println("Types Of Service:");
            System.out.println("----------------------------------------------"); 
            for (TypeOfService tos : TypeOfService.values()){
                System.out.println(tos.toString());
            }
            System.out.println("----------------------------------------------"); 
            System.out.println("The type of service of this diagnosis for the patient:");
            serviceType = sc.nextLine().trim();
        }
        String medicineType = InputValidator.getMedicationName("The prescribed medication of this diagnosis for the patient:");
        while (!validMedication){
            for (Medication med : InventoryManager.listOfMedications){
                if (medicineType.equalsIgnoreCase(med.getMedicationName())){
                    validMedication = true;
                    break;
                }
            }
            if(validMedication){
                break;
            }
            System.out.println("Invalid Medication");
            System.out.println("Types Of Medication:");
            System.out.println("----------------------------------------------"); 
            for (Medication med : InventoryManager.listOfMedications){
                System.out.println(med.getMedicationName());
            }
            System.out.println("----------------------------------------------"); 
            System.out.println("The prescribed medication of this diagnosis for the patient:");
            medicineType = sc.nextLine().trim();
        }
        int dosageAmount = InputValidator.getIntegerInput("The amount of prescribed medication of this diagnosis for the patient:", 1, 1000);
        Prescription pres = new Prescription(medicineType,PrescriptionStatus.PENDING,dosageAmount);
        String consultationNote = InputValidator.getConsultationNotes("Consultation note:");
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
        // Check if the slot matches the patient ID, doctor ID, and is confirmed
            if (selectedAppointmentID.equals(slot.getAppointmentID())) {
        // Update the outcome record with provided details
                slot.updateAppointmentOutcomeRecord(slot.getDate(), slot.getTime(), serviceType, pres, consultationNote);
                slot.setStatus(AppointmentStatus.COMPLETED);
                AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
                PatientManager.addDiagnosis(id, consultationNote);
                PatientManager.addTreatment(id, serviceType + "," + medicineType);
        // Log success message
                System.out.println("Outcome successfully recorded for Appointment ID: " + selectedAppointmentID);
                return; // Exit the loop after successfully updating
        }
    }
}

    /**
     * Function to convert information of Doctor into a string to be added to staff CSV
     */
    public String toCSV() {
        // Combine all attributes into a CSV string
        return super.getHospitalID() + ";" + super.getPassword() + ";" + super.name + ";" + "Doctor" + ";" + super.gender + ";" + super.age;
    }
}
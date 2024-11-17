import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User{
    private String name;
    private String age;
    private String gender;
    private List<Patient> patientList = new ArrayList<>();
    
    private static Scanner sc = new Scanner(System.in);

   public Doctor(String HospitalID, String doctorName, String gender, String age) {
        super(HospitalID,"password");
        this.name = doctorName;
        this.gender = gender;
        this.age = age;
    }

    public void setPassword(String password){
        super.setPassword(password);
    }
    public String getGender() {
        return gender;
    }

    public void addPatient(Patient patient) {
        patientList.add(patient);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    
    public String getDoctorID() {
        return super.getHospitalID();
    } 

    public String getDoctorName() {
        return this.name;
    }

    public void setName(String doctorName) {
        this.name = doctorName;
    } 

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
    

    public Patient findPatientByID(String patientID){
        for (Patient patient : patientList) {
            if (patient.getHospitalID() == patientID){
                return patient;
            }
        }
        return null;
    }
    
    // public void addPatientRecords(String patientID, AppointmentSlot a){
    //     Patient p = findPatientByID(patientID);
    //     if (p != null) {
    //     patientRecords.add(p);
    //     scheduleAvailability.add(a);}
    // }

    // public void removePatientRecords(String patientID, AppointmentSlot a){  //useless, in case need it
    //     Patient p = findPatientByID(patientID);
    //     if (p != null) {
    //     patientRecords.remove(p);
    //     scheduleAvailability.remove(a);}
    // }
    public void setAvailabilityForAppointments() {
        List <AppointmentSlot> schedule = AppointmentManager.getAppointmentsByDoctor(super.getHospitalID());
        System.out.println("Manage Availability for Appointments:");
        System.out.println("(1) Set Slot as Available");
        System.out.println("(2) Set Slot as Unavailable");
        System.out.print("Enter your choice: ");
        int pick = sc.nextInt();
        sc.nextLine();
        if (pick != 1 && pick != 2) {
        System.out.println("Invalid choice. Exiting.");
        return;}
        System.out.println("Enter Date in (YYYY-MM-DD) format, eg: 2024-11-15) :");
        String dateinput = sc.nextLine();
        LocalDate date = LocalDate.parse(dateinput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

           if (pick == 1) {
        System.out.println("YOUR CURRENT UNAVAILABLE SLOTS: ");
        for (AppointmentSlot slot : schedule){
            if (slot.getDate().equals(date) && slot.getStatus().equals(AppointmentStatus.UNAVAILABLE)){
                System.out.printf("Appointment ID: %s, Time: %s\n", slot.getAppointmentID(), slot.getTime().toString());
            }
        }

        System.out.println("Enter Appointment ID of Slot To Be Added (E.g APT1): ");
        String choice = sc.nextLine().trim();
        for (AppointmentSlot changedslot : schedule){
            if (changedslot.getAppointmentID().equals(choice)){
                changedslot.setStatus(AppointmentStatus.AVAILABLE);
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray){
                    if (slot.getAppointmentID().equals(changedslot.getAppointmentID())){
                        slot.setStatus(AppointmentStatus.AVAILABLE);
                        System.out.println("Slot Made Available!");
                        AppointmentManager.writeCSV(AppointmentManager.appointmentSlotArray);
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
            if (changedslot.getAppointmentID().equals(choice)){
                changedslot.setStatus(AppointmentStatus.UNAVAILABLE);
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray){
                    if (slot.getAppointmentID().equals(changedslot.getAppointmentID())){
                        slot.setStatus(AppointmentStatus.UNAVAILABLE);
                        System.out.println("Slot Made Unavailable!");
                        AppointmentManager.writeCSV(AppointmentManager.appointmentSlotArray);
                    }
                }
        }
        }
    }}

    public void viewPersonalSchedule() {
        System.out.println("=== Doctor's Personal Schedule ===");
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.AVAILABLE){
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate().toString());
                System.out.println("Time: " + slot.getTime().toString());
                System.out.println("----------------------------------------------");
            }
        }
        
    }

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

    //AppointmentSlot matchedSlot = null;
    //for (AppointmentSlot slot : appointmentSlotArray) {
    //    if (slot.getPatientID().equals(id)) {
    //        matchedSlot = slot;
    //        break;
    //    }
    //}

    //if (matchedSlot.getStatus() != AppointmentStatus.CONFIRMED) {
    //    System.out.println("The patient's appointment status is not confirmed. Cannot update records.");
    //    return;
    //}

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
        choice = sc.nextInt();
        sc.nextLine();
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
                    AppointmentManager.writeCSV(AppointmentManager.appointmentSlotArray);
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
                    AppointmentManager.writeCSV(AppointmentManager.appointmentSlotArray);
                    System.out.println("Appointment ID " + choice2 + " has been DECLINED.");
                }
            }
            break;
        }
    }
}  

    public void makeAppointmentOutcomeRecord(){
        System.out.print("patientID to update his/her record:");
        String id = sc.nextLine();
        System.out.print("The type of service of this diagnosis for the patient:");
        String serviceType = sc.nextLine();
        System.out.print("The prescribed medication of this diagnosis for the patient:");
        String medicineType = sc.nextLine();
        System.out.print("The amount of prescribed medication of this diagnosis for the patient:");
        String dosageAmount = sc.nextLine();
        Prescription pres = new Prescription(medicineType,PrescriptionStatus.PENDING,Integer.parseInt(dosageAmount));
        System.out.print("Consultation note:");
        String consultationNote = sc.nextLine();
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
        // Check if the slot matches the patient ID, doctor ID, and is confirmed
            if (slot.getPatientID().equals(id) && 
                slot.getDoctorID().equals(super.getHospitalID()) && 
                slot.getStatus()==AppointmentStatus.CONFIRMED) {

        // Update the outcome record with provided details
                slot.updateAppointmentOutcomeRecord(slot.getDate(), slot.getTime(), serviceType, pres, consultationNote);
                slot.setStatus(AppointmentStatus.COMPLETED);
                AppointmentManager.writeCSV(AppointmentManager.appointmentSlotArray);
        // Log success message
                System.out.println("Outcome successfully recorded for patient ID: " + id);
                return; // Exit the loop after successfully updating
    }
}

    //UPDATE PATIENT MEDICAL RECORDS,RECORD APPOINTMENT OUTCOME
    //AGGREGATION
    //UNAVAILABLE
    }}
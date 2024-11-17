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

    public void printMenu(){
        this.addPatient(PatientManager.allPatients.get(0));
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
                sc.nextLine();
                this.updatePatientRecord();
                break;
                case 3:
                sc.nextLine();
                this.viewPersonalSchedule(); // View Personal Schedule
                break;
                case 4:
                sc.nextLine();
                this.setAvailabilityForAppointments();// Set Availability For Appointments
                break;
                case 5:
                sc.nextLine();
                this.acceptOrDeclineAppointments();// Accept Or Decline Appointment Requests
                break;
                case 6:
                sc.nextLine();
                this.viewUpcomingAppointment();
                break;
                case 7:
                sc.nextLine();
                this.makeAppointmentOutcomeRecord();
                break;
                case 8:
                System.out.println("Logging out...");
                break;
            }
        }
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
        System.out.println("Enter Date in (YYYY-MM-DD) format, eg: 2024-11-15 :");
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
            if (changedslot.getAppointmentID().equals(choice)){
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

    public void viewPersonalSchedule() {
        System.out.println("=== Doctor's Personal Schedule ===");
        viewUpcomingAppointment();
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
        for (AppointmentSlot slot : confirmedAppointmentSlots){
            if (slot.getStatus() != AppointmentStatus.CONFIRMED){
                confirmedAppointmentSlots.remove(slot);
            }
        }
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
            System.out.println("ENTER APPOINTMENT ID OF APPOINTMENT TO MAKE APPOINTMENT OUTCOME RECORD FOR [E.g APT1]: ");
            selectedAppointmentID = sc.nextLine().trim().toUpperCase();
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
        System.out.print("The prescribed medication of this diagnosis for the patient:");
        String medicineType = sc.nextLine().trim();
        while (!validMedication){
            for (Medication med : Inventory.listOfMedications){
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
            for (Medication med : Inventory.listOfMedications){
                System.out.println(med.getMedicationName());
            }
            System.out.println("----------------------------------------------"); 
            System.out.println("The prescribed medication of this diagnosis for the patient:");
            medicineType = sc.nextLine().trim();
        }
        System.out.print("The amount of prescribed medication of this diagnosis for the patient:");
        String dosageAmount = sc.nextLine();
        Prescription pres = new Prescription(medicineType,PrescriptionStatus.PENDING,Integer.parseInt(dosageAmount));
        System.out.print("Consultation note:");
        String consultationNote = sc.nextLine();
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
        // Check if the slot matches the patient ID, doctor ID, and is confirmed
            if (selectedAppointmentID.equals(slot.getAppointmentID())) {
        // Update the outcome record with provided details
                slot.updateAppointmentOutcomeRecord(slot.getDate(), slot.getTime(), serviceType, pres, consultationNote);
                slot.setStatus(AppointmentStatus.COMPLETED);
                AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
                PatientManager.addDiagnosis(id, consultationNote);
                PatientManager.addTreatment(id, medicineType);
        // Log success message
                System.out.println("Outcome successfully recorded for Appointment ID: " + selectedAppointmentID);
                return; // Exit the loop after successfully updating
        }
    }
}
    public String toCSV() {
        // Combine all attributes into a CSV string
        return super.getHospitalID() + ";" + super.getPassword() + ";" + name + ";" + "Doctor" + ";" + gender + ";" + age;
    }
}
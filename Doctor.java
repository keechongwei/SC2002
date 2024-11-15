import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User{
    private String name;
    private int age;
    private String gender;
    private List<Patient> patientList = new ArrayList<>();
    
    private static Scanner sc = new Scanner(System.in);

   public Doctor(String HospitalID, String doctorName, String gender, int age) {
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public void viewPatientRecords(String patientID){
        if (patientList.isEmpty()) {
            System.out.println("No Patient Record Found");
            return;
        }
        else{
            for (Patient p : patientList){
                if (p.getHospitalID() == patientID){
                    p.viewMedicalRecord();
                }
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
        System.out.println("Enter Date in (YYYY-MM-DD) format, eg: 2024-11-15) :");
        String dateinput = sc.nextLine();
        LocalDate date = LocalDate.parse(dateinput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("YOUR CURRENT UNAVAILABLE SLOTS: ");
        for (AppointmentSlot slot : schedule){
            if (slot.getDate() == date && slot.getStatus() == AppointmentStatus.UNAVAILABLE){
                System.out.printf("Appointment ID: %s, Time: %s\n", slot.getAppointmentID(), slot.getTime().toString());
            }
        }
        System.out.println("Enter Appointment ID of Slot To Be Added (E.g APT1): ");
        String choice = sc.nextLine().trim();
        for (AppointmentSlot changedslot : schedule){
            if (changedslot.getAppointmentID() == choice){
                changedslot.setStatus(AppointmentStatus.AVAILABLE);
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray){
                    if (slot.getAppointmentID() == changedslot.getAppointmentID()){
                        slot.setStatus(AppointmentStatus.AVAILABLE);
                    }
                }
        }
        }
        System.out.println("Slot Made Available!");
    }

    public void removeAvailabilityForAppointments() {
        List <AppointmentSlot> schedule = AppointmentManager.getAppointmentsByDoctor(super.getHospitalID());
        System.out.println("Enter Date in (YYYY-MM-DD) format, eg: 2024-11-15) :");
        String dateinput = sc.nextLine();
        LocalDate date = LocalDate.parse(dateinput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("YOUR CURRENT AVAILABLE SLOTS: ");
        for (AppointmentSlot slot : schedule){
            if (slot.getDate() == date && slot.getStatus() == AppointmentStatus.AVAILABLE){
                System.out.printf("Appointment ID: %s, Time: %s\n", slot.getAppointmentID(), slot.getTime().toString());
            }
        }
        System.out.println("Enter Appointment ID of Slot To Be Removed (E.g APT1): ");
        String choice = sc.nextLine().trim();
        for (AppointmentSlot changedslot : schedule){
            if (changedslot.getAppointmentID() == choice){
                changedslot.setStatus(AppointmentStatus.UNAVAILABLE);
                for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray){
                    if (slot.getAppointmentID() == changedslot.getAppointmentID()){
                        slot.setStatus(AppointmentStatus.UNAVAILABLE);
                    }
                }
        }
        }
        System.out.println("Slot Made Unavailable!");
    }


    public void viewPersonalSchedule(){
        System.out.println("Doctor's Full Schedule:");
        this.viewUpcomingAppointment();
        System.out.println("Doctor's Available Timeslots:");
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.AVAILABLE){
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate().toString());
                System.out.println("Time: " + slot.getTime().toString());
                System.out.println("----------------------------------------------");
            }
        }
    }

//    public void updatePatientRecord() {
//         System.out.print("patientID to update of his/her record:");
//         String id = sc.nextLine();
//         System.out.print("The diagnosis for the patient:");
//         String diagnosis = sc.nextLine();
//         System.out.print("The treatment plan:");
//         String treatmentPlan = sc.nextLine();
//         Patient p = findPatientByID(id);
//         if (p != null) {//need to add updateOutcome
//             for (Patient patient : patientList) {
//             if (slot.getPatientID().equals(id) && slot.getStatus() == AppointmentStatus.CONFIRMED){
//                     slot.setStatus(AppointmentStatus.COMPLETED);
//                     p.setMedicalReport(diagnosis, treatmentPlan);
//                     System.out.println("Updated patient record for patient ID: " + id);}
//         }
//         } else {
//             System.out.println("Wrong patient ID: " + id);
        
//     }}

   public void viewUpcomingAppointment(){
    System.out.println("Doctor's Upcoming Appointments");
    for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
        if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.CONFIRMED) {
            System.out.println("----------------------------------------------");
            System.out.println("Date: " + slot.getDate().toString());
            System.out.println("Time: " + slot.getTime().toString());
            System.out.println("PatientID: " + slot.getPatientID());
            System.out.println("----------------------------------------------");
        }
    }
    }

   
   public void acceptOrDeclineAppointments(){
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
    int choice = 0;
    String choice2 = "";
    while (choice <3){
        System.out.print("(1) ACCEPT APPOINTMENT");
        System.out.print("(2) DECLINE APPOINTMENT");
        System.out.print("(3) QUIT");
        choice = sc.nextInt();
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
            System.out.print("Enter Appointment ID to Accept: ");
            choice2 = sc.nextLine();
            for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
                if (slot.getAppointmentID() == choice2){ 
                    slot.setStatus(AppointmentStatus.CONFIRMED);
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
            System.out.print("Enter Appointment ID to Decline: ");
            choice2 = sc.nextLine();
            for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
                if (slot.getAppointmentID() == choice2){ 
                    slot.setStatus(AppointmentStatus.CANCELLED);
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
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) 
            if (slot.getPatientID().equals(id) && slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.CONFIRMED) {
                    slot.updateAppointmentOutcomeRecord(slot.getDate(),slot.getTime(),serviceType, pres, consultationNote);
                    System.out.println("Outcome recorded for patient ID: " + id);}
                    
        }
}
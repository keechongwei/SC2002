import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User{
    protected String name;
    protected int age;
    protected String gender;
    private List<Patient> patientList = new ArrayList<>();
    
    private static Scanner sc = new Scanner(System.in);

   public Doctor(String HospitalID, String password, String doctorName, String gender, int age) {
        super(HospitalID,password);
        this.name = doctorName;
        this.gender = gender;
        this.age = age;
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

    // public Patient findPatientByID(String patientID){
    //     for (AppointmentSlot slot : AppointmentSlot.appointmentSlotArray) { /*need to declare public static List<AppointmentSlot> appointmentSlotArray = new ArrayList<>();*/
    //         if (slot.getPatientID().equals(patientID)) { /*need to declare getPatient*/
    //                 return slot.getPatient();
    //         }}
    //     return null;
    // }
    
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
        System.out.println("Doctor's Upcoming Appointments");
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.CONFIRMED) {
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate());
                System.out.println("Time: " + slot.getTime());
                System.out.println("PatientID: " + slot.getPatientID());
                System.out.println("----------------------------------------------");
            }
        }
        System.out.println("Doctor's Available Timeslots:");
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if (slot.getDoctorID().equals(super.getHospitalID()) && slot.getStatus() == AppointmentStatus.AVAILABLE){
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate());
                System.out.println("Time: " + slot.getTime());
                System.out.println("----------------------------------------------");
            }
        }
    }

   public void updatePatientRecord() {
        System.out.print("patientID to update of his/her record:");
        String id = sc.nextLine();
        System.out.print("The diagnosis for the patient:");
        String diagnosis = sc.nextLine();
        System.out.print("The treatment plan:");
        String treatmentPlan = sc.nextLine();
        Patient p = findPatientByID(id);
        if (p != null) {//need to add updateOutcome
            for (AppointmentSlot slot : AppointmentSlot.appointmentSlotArray) {
            if (slot.getPatientID().equals(id) && slot.getStatus() == AppointmentStatus.CONFIRMED){
                    slot.setStatus(COMPLETED);
                    p.setMedicalReport(diagnosis, treatmentPlan);
                    System.out.println("Updated patient record for patient ID: " + id);}
        }
        } else {
            System.out.println("Wrong patient ID: " + id);
        
    }}

   public void viewUpcomingAppointment(){
        System.out.println("Doctor's Upcoming Appointments");
        for (AppointmentSlot slot : AppointmentSlot.appointmentSlotArray) {
            if (slot.getDoctorID().equals(doctorID) && slot.getStatus() == CONFIRMED) { //Appointment Slot add getStatus, getDoctorID
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate());
                System.out.println("Time: " + slot.getTime());
                System.out.println("Status: " + slot.getStatus());
                System.out.println("DoctorID: " + slot.getDoctorID());
                System.out.println("PatientID: " + slot.getPatientID());
                System.out.println("Outcome: " + slot.getAppointmentOutcomeRecord());
                System.out.println("----------------------------------------------");
            }}}

   
   public void acceptOrDeclineAppointments(){
        for (AppointmentSlot slot : AppointmentSlot.appointmentSlotArray) {
            if (slot.getDoctorID().equals(doctorID) && slot.getStatus() == AppointmentStatus.PENDING)
                { 
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate());
                System.out.println("Time: " + slot.getTime());
                System.out.println("Status: " + slot.getStatus());
                System.out.println("DoctorID: " + slot.getDoctorID());
                System.out.println("PatientID: " + slot.getPatientID());
                System.out.println("Outcome: " + slot.getAppointmentOutcomeRecord());
                System.out.println("----------------------------------------------");
                System.out.print("Accept Appointment type 1, decline appointment type 2:");
                int choice2 = sc.nextInt();
                    if(choice2==1)
                        {slot.setStatus(CONFIRMED);
                        removeAvailabilityForAppointments(slot.getDate(), slot.getTime());
                        addPatientRecords(slot.getPatientID(), slot);
                        System.out.println("Accepting appointment with PatientID: " + slot.getPatientID());}
                    else if(choice2==2){
                        slot.setStatus(AppointmentStatus.CANCELLED);
                        System.out.println("Declining appointment with PatientID: " + slot.getPatientID());}
                    else {System.out.println("Invalid input");}
            }   
        }}  

    public void makeAppointmentOutcomeRecord(){
        System.out.print("patientID to update his/her record:");
        String id = sc.nextLine();
        System.out.print("The type of service of this diagnosis for the patient:");
        String serviceType = sc.nextLine();
        System.out.print("The prescribed medication of this diagnosis for the patient:");
        String medicineType = sc.nextLine();
        System.out.print("The amount of prescribed medication of this diagnosis for the patient:");
        String dosageAmount = sc.nextLine();
        Prescription pres = Prescription(medicineType,Prescription.valueOf("PENDING"),Integer.parseInt(dosageAmount));
        System.out.print("Consultation note:");
        String consultationNote = sc.nextLine();
        for (AppointmentSlot slot : AppointmentSlot.appointmentSlotArray) 
            if (slot.getPatientID().equals(id) && slot.getDoctorID().equals(doctorID) && slot.getStatus() == AppointmentStatus.CONFIRMED) {
                    slot.updateAppointmentOutcomeRecord(slot.getDate(),slot.getTime(),serviceType, pres, consultationNote);
                    System.out.println("Outcome recorded for patient ID: " + id);}
                    
        }
}
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends User{
    protected String name;
    protected int age;
    protected String gender;
//    private List<Patient> patientRecords = new ArrayList<>();
 //   private List<String> scheduleAvailability = new ArrayList<>();
    
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
    
    // public String getDoctorID() {
    //     return doctorID;
    // }

    // public void setDoctorID(String doctorID) {
    //     this.doctorID = doctorID;
    // } 

    public String getDoctorName() {
        return this.name;
    }

    public void setName(String doctorName) {
        this.name = doctorName;
    } 

    // public void viewPatientRecords(){
    //     if (patientRecords.isEmpty()) {
    //         System.out.println("No Patient Record Found");
    //         return;}
    //     for (Patient p : patientRecords)
    //         if(p != null)
    //             p.viewMedicalRecord();}

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

    // public void setAvailabilityForAppointments() {
    //     System.out.println("Type date (MM/DD, example:12/10):");
    //     String date = sc.nextLine();
    //     System.out.println("Type time (example:1800-1900):");
    //     String time = sc.nextLine();
    //     String availability = "Date:" + date + "\nTime:" + time; 
    //     scheduleAvailability.add(availability);  
    //     System.out.println("Availability added: " + availability);
    // }

    // public void removeAvailabilityForAppointments(String date, String time) {
    //     String check = "Date:" + date + "\nTime:" + time;
    //     for(String checked:scheduleAvailability)
    //         if(check.equals(checked))
    //             scheduleAvailability.remove(checked);
    // }

    public void viewPersonalSchedule(){
        System.out.println("Doctor's Full Schedule:");
        System.out.println("Doctor's Upcoming Appointments");
        for (AppointmentSlot slot : AppointmentSlot.appointmentSlotArray) {
            if (slot.getDoctorID().equals(doctorID) && slot.getStatus() == AppointmentStatus.CONFIRMED) {
                System.out.println("----------------------------------------------");
                System.out.println("Date: " + slot.getDate());
                System.out.println("Time: " + slot.getTime());
                System.out.println("Status: " + slot.getStatus());
                System.out.println("DoctorID: " + slot.getDoctorID());
                System.out.println("PatientID: " + slot.getPatientID());
                System.out.println("Outcome: " + slot.getAppointmentOutcomeRecord());
                System.out.println("----------------------------------------------");
            }}
        System.out.println("Doctor's Available Timeslots:");
        for (String slot : scheduleAvailability) {
                System.out.println(slot);}}

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

    public void appointmentOutcomeRecord(){
        System.out.print("patientID to update of his/her record:");
        String id = sc.nextLine();
        System.out.print("The type of service of this diagnosis for the patient:");
        String serviceType = sc.nextLine();
        System.out.print("The prescribed medication of this diagnosis for the patient:");
        String medicineType = sc.nextLine();
        System.out.print("Consultation note:");
        String consultationNote = sc.nextLine();
        for (AppointmentSlot slot : AppointmentSlot.appointmentSlotArray) 
            if (slot.getPatientID().equals(id) && slot.getDoctorID().equals(doctorID) && slot.getStatus() == AppointmentStatus.CONFIRMED) {
                    slot.updateOutcome(slot.getDate(), serviceType, medicineType, consultationNote);
                    System.out.println("Outcome recorded for patient ID: " + id);}
                    
        }
}
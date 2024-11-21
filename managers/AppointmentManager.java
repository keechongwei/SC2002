package managers;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Users.Doctor;
import appointment.AppointmentSlot;
import enums.AppointmentStatus;
import managers.csvhandlers.AppointmentCSVHandler;

/**
 * A Manager object meant to handle any appointment scheduling
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class AppointmentManager implements Manager{
    /**
     * List of Appointment Slots
     */
    public static List<AppointmentSlot> appointmentSlotArray = new ArrayList<>();
    /**
     * Number of Slots to be created per doctor
     */
    public static int numberOfSlots = 10;
    /**
     * ID of next appointment to be created
     */
    public static int nextAppointmentID = 1;

    /**
     * Generates AppointmentSlots for Doctors if appointments.csv is not populated
     * Generates hourly slots for each Doctor in the list. The number of slots depends on the value of the attribute numberOfSlots in AppointmentManager
     * @param doctors List of Doctors initialised from Staff.csv
     * @return void
     */
    public static void makeDailyAppointments(List<Doctor> doctors){
        // hour determines starting hour
        int hour = 9;
        for(int i = 0; i < numberOfSlots; i++){
            String dateString = DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
            String timeString = "";
            if (hour < 10){
                timeString = "0" + String.valueOf(hour) + ":00";
            }
            else{
                timeString = String.valueOf(hour) + ":00";
            }
            for (Doctor doctor : doctors) {
                    String doctorID = doctor.getHospitalID();
                    AppointmentSlot slot = new AppointmentSlot(dateString, timeString, "AVAILABLE", doctorID," ");
                    AppointmentManager.addAppointment(slot); 
                } 
            hour++;
            }
        }
    
    /**
     * Removes AppointmentSlots if a Doctor is removed
     * If AppointmentSlot is AVAILABLE, it is removed from the list of AppointmentSlots in AppointmentManager
     * If AppointmentSlot is PENDING, it is marked as CANCELLED
     * appointments.csv is updated
     * @param d Doctor to be removed
     * @return void
     */
    public static void removeAppointments(Doctor d) {
            appointmentSlotArray.removeIf(slot -> 
                slot.getDoctorID().equals(d.getHospitalID()) && slot.getStatus() == AppointmentStatus.AVAILABLE
            );
        
            for (AppointmentSlot slot : appointmentSlotArray) {
                if (slot.getDoctorID().equals(d.getHospitalID()) && slot.getStatus() == AppointmentStatus.PENDING) {
                    slot.setStatus(AppointmentStatus.CANCELLED);
                }
            }
        
            AppointmentCSVHandler.writeCSV(appointmentSlotArray);
        }
        
    /**
     * Adds an AppointmentSlot to List Of AppointmentManager 
     * Writes to appointments.csv, increments nextAppointmentID in AppointmentManager 
     * Used by makeDailyAppointments
     * @param slot AppointmentSlot to be added
     * @return void
     */
    public static void addAppointment(AppointmentSlot slot) {
        appointmentSlotArray.add(slot);
        AppointmentCSVHandler.appendAppointmentToCSV(slot);
        nextAppointmentID++;
    }

    /**
     * Returns a List of AppointmentSlots
     * Depending if PatientID of AppointmentSlot matches 
     * @param patientID PatientID to check AppointmentSlot against
     * @return List of AppointmentSlots that match PatientID
     */
    public static List<AppointmentSlot> getAppointmentsByPatient(String patientID) {
        List<AppointmentSlot> result = new ArrayList<>();
        for (AppointmentSlot slot : appointmentSlotArray) {
            if (slot.getPatientID().equals(patientID)) {
                result.add(slot);
            }
        }
        return result;
    }

    /**  
     * Returns a List of AppointmentSlots
     * Depending if DoctorID of AppointmentSlot matches 
     * @param doctorID DoctorID to check AppointmentSlot against
     * @return List of AppointmentSlots that match DoctorID
     */
    public static List<AppointmentSlot> getAppointmentsByDoctor(String doctorID) {
        List<AppointmentSlot> result = new ArrayList<>();
        for (AppointmentSlot slot : appointmentSlotArray) {
            if (slot.getDoctorID().equals(doctorID)) {
                result.add(slot);
            }
        }
        return result;
    }
    
    /**
     * Returns a List of AppointmentSlots
     * Depending if AppointmentStatus of AppointmentSlot is AVAILABLE
     * @param void
     * @return List of AppointmentSlots that have AppointmentStatus as AVAILABLE
     */
    public static List<AppointmentSlot> getAvailableAppointments(){
        List<AppointmentSlot> availSlots = new ArrayList<AppointmentSlot>();
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getStatus() == AppointmentStatus.AVAILABLE) {
                availSlots.add(slot);   
            }
        }
        return availSlots;
    }
    
    /**
     * Prints all AppointmentSlots in appointmentSlotArray of AppointmentManager
     * @param void
     * @return void
     */
    public static void viewAllAppointments() {
        System.out.printf("%-5s %-12s %-8s %-10s %-10s%n", "Appointment ID.", "Date", "Time", "Doctor", "PatientID", "Status");
        System.out.println("-".repeat(50));

        //show all appt slots
        for(AppointmentSlot apptSlot : appointmentSlotArray) {
            System.out.printf("%-5s %-12s %-8s %-10s %-10s%n", apptSlot.getAppointmentID(), 
            apptSlot.getDate(), apptSlot.getTime(), apptSlot.getDoctorID(), apptSlot.getStatus());
    }
        
    }

    /**
     * Loads in data from appointments.csv
     * Creates appointments.csv if it doesn't exist or populates it with appointment slots if its empty
     * @param void
     * @return void
     */
    public static void initialise() {
        if (!((AppointmentCSVHandler.csvFile).exists()) || (AppointmentCSVHandler.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("appointments.csv is empty or missing. Generating daily appointments...");
            AppointmentCSVHandler.writeHeader(AppointmentCSVHandler.appointmentsCSVHeader);
            AppointmentManager.makeDailyAppointments(StaffManager.doctors); // Replace getStaffList() with your method to get the staff data
        } else {
            // File exists and is not empty, load appointments from the CSV
            System.out.println("Loading appointments from appointments.csv...");
            AppointmentCSVHandler.loadCSV();
        }
    }
}

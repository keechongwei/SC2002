import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManager {
    public static List<AppointmentSlot> appointmentSlotArray = new ArrayList<>();
    // decides how many slots will there be in a day
    public static int numberofSlots = 10;
    public static int nextAppointmentID = 1;

    public static void makeDailyAppointments(List<Doctor> doctors){
        // hour determines starting hour
        int hour = 9;
        for(int i = 0; i < numberofSlots; i++){
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
        
    
    public static void addAppointment(AppointmentSlot slot) {
        appointmentSlotArray.add(slot);
        AppointmentCSVHandler.appendAppointmentToCSV(slot);
        nextAppointmentID++;
    }

    // for patients to see pending appointments, get all slots depending on DoctorID
    public static List<AppointmentSlot> getAppointmentsByPatient(String patientID) {
        List<AppointmentSlot> result = new ArrayList<>();
        for (AppointmentSlot slot : appointmentSlotArray) {
            if (slot.getPatientID().equals(patientID)) {
                result.add(slot);
            }
        }
        return result;
    }

    // for doctors to see pending appointments, get all slots depending on DoctorID
    public static List<AppointmentSlot> getAppointmentsByDoctor(String doctorID) {
        List<AppointmentSlot> result = new ArrayList<>();
        for (AppointmentSlot slot : appointmentSlotArray) {
            if (slot.getDoctorID().equals(doctorID)) {
                result.add(slot);
            }
        }
        return result;
    }
    
    // for patients to schedule, get all appointments that are still available
    public static List<AppointmentSlot> getAvailableAppointments(){
        List<AppointmentSlot> availSlots = new ArrayList<AppointmentSlot>();
        for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
            if(slot.getStatus() == AppointmentStatus.AVAILABLE) {
                availSlots.add(slot);   
            }
        }
        return availSlots;
    }
    public static List<AppointmentSlot> getAllAppointments() {
        return appointmentSlotArray;
    }

    public static void viewAllAppointments() {
        System.out.printf("%-5s %-12s %-8s %-10s %-10s%n", "Appointment ID.", "Date", "Time", "Doctor", "PatientID", "Status");
        System.out.println("-".repeat(50));

        //show all appt slots
        for(AppointmentSlot apptSlot : appointmentSlotArray) {
            System.out.printf("%-5s %-12s %-8s %-10s %-10s%n", apptSlot.getAppointmentID(), 
            apptSlot.getDate(), apptSlot.getTime(), apptSlot.getDoctorID(), apptSlot.getStatus());
    }
        
    }

    // initialises appointment array
    public static void initialiseAppointments() {
        if (!((AppointmentCSVHandler.csvFile).exists()) || (AppointmentCSVHandler.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("appointments.csv is empty or missing. Generating daily appointments...");
            AppointmentCSVHandler.writeHeader(AppointmentCSVHandler.appointmentsCSVHeader);
            AppointmentManager.makeDailyAppointments(StaffManager.doctors); // Replace getStaffList() with your method to get the staff data
        } else {
            // File exists and is not empty, load appointments from the CSV
            System.out.println("Loading appointments from appointments.csv...");
            AppointmentCSVHandler.loadAppointmentsFromCSV(AppointmentCSVHandler.csvFile);
        }
    }
    // Other functions for filtering by patient, status, etc.
}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppointmentManager {
    public static List<AppointmentSlot> appointmentSlotArray = new ArrayList<>();
    public static String appointmentsCSVHeader = "Date;Time;Appointment ID; DoctorID; PatientID;Appointment Status; OutcomeDate | Outcome Time |  Type Of Service | Medication Name ^ Medication Status ^ Medication Dosage | Consultation Notes";
    public static final File csvFile = new File("appointments.csv");
    // decides how many slots will there be in a day
    public static int numberofSlots = 10;
    public static int nextAppointmentID = 1;

    public static void updateAppointmentsCSV(){
        AppointmentManager.writeHeader(AppointmentManager.appointmentsCSVHeader);
        AppointmentManager.writeCSV(AppointmentManager.appointmentSlotArray);
    }
    
    // write header to Appointments CSV file
    public static void writeHeader(String header){
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.write(header + "\n");
        } catch (IOException e) {
            System.out.println("Error writing header to CSV file.");
            e.printStackTrace();
        }
    }
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
    
    public static void addAppointment(AppointmentSlot slot) {
        appointmentSlotArray.add(slot);
        appendAppointmentToCSV(slot);
        nextAppointmentID++;
    }
    
    // function to edit values of AppointmentSlots without creating rows
    public static void writeCSV(List<AppointmentSlot> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            // Write the header line first
            bw.write(appointmentsCSVHeader);
            bw.newLine();
            for (AppointmentSlot appointment : appointments) {
                bw.write(appointment.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV file: " + e.getMessage());
        }
    }

    // function to add new values of AppointmentSlots
    public static void appendAppointmentToCSV(AppointmentSlot slot) {
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.write(slot.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("Error writing appointment to CSV file.");
            e.printStackTrace();
        }
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

    public static void loadAppointmentsFromCSV(File csvFile) {
        boolean headerline = true;
        try (Scanner scanner = new Scanner(csvFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // ignore headerline
                if(headerline){
                    headerline = false;
                    continue;
                }
                String[] ApptSlotfields = line.split(";");
                String date = ApptSlotfields[0];
                String time = ApptSlotfields[1];
                String appointmentID = ApptSlotfields[2];
                String doctorID = ApptSlotfields[3];
                String patientID = ApptSlotfields[4];
                AppointmentStatus status = AppointmentStatus.valueOf(ApptSlotfields[5]);
                String outcome = ApptSlotfields.length > 6 ? ApptSlotfields[6] : "";
                AppointmentOutcomeRecord outcomeRecord = null;
                if (!outcome.equals("null")){
                    outcomeRecord = AppointmentOutcomeRecord.fromCSV(outcome);
                }
                appointmentSlotArray.add(new AppointmentSlot(date, time,status,doctorID,patientID,outcomeRecord,appointmentID));
                nextAppointmentID++;
            }
            System.out.println("Appointments loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load appointments from file.");
            e.printStackTrace();
        }
    }

    // initialises appointment array
    public static void initialiseAppointments() {
        if (!((AppointmentManager.csvFile).exists()) || (AppointmentManager.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("appointments.csv is empty or missing. Generating daily appointments...");
            AppointmentManager.writeHeader(AppointmentManager.appointmentsCSVHeader);
            AppointmentManager.makeDailyAppointments(StaffManager.doctors); // Replace getStaffList() with your method to get the staff data
        } else {
            // File exists and is not empty, load appointments from the CSV
            System.out.println("Loading appointments from appointments.csv...");
            AppointmentManager.loadAppointmentsFromCSV(AppointmentManager.csvFile);
        }
    }
    // Other functions for filtering by patient, status, etc.
}

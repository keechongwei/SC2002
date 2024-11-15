import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppointmentManager {
    public static List<AppointmentSlot> appointmentSlotArray = new ArrayList<>();
    public static final File csvFile = new File("appointments.csv");
    public static int nextAppointmentID = 1;

    public static void addAppointment(AppointmentSlot slot) {
        appointmentSlotArray.add(slot);
        appendAppointmentToCSV(slot);
        nextAppointmentID++;
    }
    // function to edit values of AppointmentSlots without creating rows
    public static void writeCSV(List<AppointmentSlot> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
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

    public static List<AppointmentSlot> getAppointmentsByDoctor(String doctorID) {
        List<AppointmentSlot> result = new ArrayList<>();
        for (AppointmentSlot slot : appointmentSlotArray) {
            if (slot.getDoctorID().equals(doctorID)) {
                result.add(slot);
            }
        }
        return result;
    }
    
    public static List<AppointmentSlot> getAllAppointments() {
        return appointmentSlotArray;
    }
    public static void loadAppointmentsFromCSV(File csvFile) {
        try (Scanner scanner = new Scanner(csvFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] ApptSlotfields = line.split(";");
                String date = ApptSlotfields[0];
                String time = ApptSlotfields[1];
                String appointmentID = ApptSlotfields[2];
                String doctorID = ApptSlotfields[3];
                String patientID = ApptSlotfields[4];
                AppointmentStatus status = AppointmentStatus.valueOf(ApptSlotfields[5]);
                String outcome = ApptSlotfields.length > 6 ? ApptSlotfields[6] : "";
                AppointmentOutcomeRecord outcomeRecord = null;
                if (!outcome.equals("")){
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
    // Other functions for filtering by patient, status, etc.
}

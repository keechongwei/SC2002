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

    public static void addAppointment(AppointmentSlot slot) {
        appointmentSlotArray.add(slot);
        appendAppointmentToCSV(slot);
    }

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
                String doctorID = ApptSlotfields[2];
                String patientID = ApptSlotfields[3];
                AppointmentStatus status = AppointmentStatus.valueOf(ApptSlotfields[4]);
                String outcome = ApptSlotfields.length > 5 ? ApptSlotfields[5] : "";
                AppointmentOutcomeRecord outcomeRecord = null;
                if (!outcome.equals("")){
                    outcomeRecord = AppointmentOutcomeRecord.fromCSV(outcome);
                }
                appointmentSlotArray.add(new AppointmentSlot(date, time,status,doctorID,patientID,outcomeRecord));
            }
            System.out.println("Appointments loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load appointments from file.");
            e.printStackTrace();
        }
    }
    // Other functions for filtering by patient, status, etc.
}

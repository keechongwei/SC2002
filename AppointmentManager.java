import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppointmentManager {
    private static List<AppointmentSlot> appointmentSlotArray = new ArrayList<>();

    public static void addAppointment(AppointmentSlot slot) {
        appointmentSlotArray.add(slot);
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
                String[] fields = line.split(";");
                String date = fields[0];
                String time = fields[1];
                String doctorID = fields[2];
                String patientID = fields[3];
                AppointmentStatus status = AppointmentStatus.valueOf(fields[4]);
                String outcome = fields.length > 5 ? fields[5] : "";
                appointmentSlotArray.add(new AppointmentSlot(date, time, doctorID, patientID, status, outcome));
            }
            System.out.println("Appointments loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load appointments from file.");
            e.printStackTrace();
        }
    }
}
    // Other functions for filtering by patient, status, etc.
}

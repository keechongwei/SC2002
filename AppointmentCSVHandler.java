import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AppointmentCSVHandler implements CSVHandler{
    public static String appointmentsCSVHeader = "Date;Time;Appointment ID; DoctorID; PatientID;Appointment Status; OutcomeDate | Outcome Time |  Type Of Service | Medication Name ^ Medication Status ^ Medication Dosage | Consultation Notes";
    public static final File csvFile = new File("appointments.csv");

        // write header to Appointments CSV file
    public static void writeHeader(String header){
        try (FileWriter writer = new FileWriter(csvFile, true)) {
            writer.write(header + "\n");
        } catch (IOException e) {
            System.out.println("Error writing header to CSV file.");
            e.printStackTrace();
        }
    }

    public static void loadCSV() {
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
            AppointmentManager.appointmentSlotArray.add(new AppointmentSlot(date, time,status,doctorID,patientID,outcomeRecord,appointmentID));
            AppointmentManager.nextAppointmentID++;
        }
        System.out.println("Appointments loaded successfully!");
    } catch (FileNotFoundException e) {
        System.out.println("Unable to load appointments from file.");
        e.printStackTrace();
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

}

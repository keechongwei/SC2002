import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
// import java.time.LocalDate;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;

public class Pharmacist extends User {
    private List<AppointmentSlot> appointments;
    private final Inventory inventory;
    private String gender;
    private int age;

    public Pharmacist(String HospitalID, String password) {
        super(HospitalID, password);
        this.appointments = new ArrayList<>();
        this.inventory = new Inventory("Medicine_List.csv");
        loadAppointments();
    }

    // Load pending prescriptions from CSV file
    private void loadAppointments() {
        appointments.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("Appointments.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length >= 7 && parts[5].equals("COMPLETED")) {
                    String outcomeStr = parts[6];
                    if (!outcomeStr.equals("null")) {
                        AppointmentOutcomeRecord outcome = AppointmentOutcomeRecord.fromCSV(outcomeStr);
                        if (outcome.getPrescribedMedication() != null) {
                            AppointmentSlot appointment = new AppointmentSlot(
                                parts[0], parts[1], 
                                AppointmentStatus.valueOf(parts[5]),
                                parts[3], parts[4], 
                                outcome, parts[2]
                            );
                            appointments.add(appointment);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    private void saveAppointments() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Appointments.csv"))) {
            writer.println("Date;Time;AppointmentID;DoctorID;PatientID;Status;OutcomeRecord");
            for (AppointmentSlot appointment : appointments) {
                writer.println(appointment.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }

    public void viewAllAppointmentOutcomes() {
        loadAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        System.out.println("\n=== All Appointment Outcomes ===");
        for (AppointmentSlot appointment : appointments) {
            displayAppointmentWithPrescriptionStatus(appointment);
        }
    }

    public void viewPendingPrescriptions() {
        loadAppointments();
        List<AppointmentSlot> pendingAppointments = appointments.stream()
            .filter(apt -> {
                Prescription prescription = apt.getAppointmentOutcomeRecord().getPrescribedMedication();
                return prescription != null && prescription.getStatus() == PrescriptionStatus.PENDING;
            })
            .collect(Collectors.toList());

        if (pendingAppointments.isEmpty()) {
            System.out.println("No pending prescriptions found.");
            return;
        }

        System.out.println("\n=== Pending Prescriptions ===");
        for (AppointmentSlot appointment : pendingAppointments) {
            displayAppointmentWithPrescriptionStatus(appointment);
        }
    }

    private void displayAppointmentWithPrescriptionStatus(AppointmentSlot appointment) {
        AppointmentOutcomeRecord outcome = appointment.getAppointmentOutcomeRecord();
        Prescription prescription = outcome.getPrescribedMedication();
        
        System.out.println("\n------------------------");
        System.out.println("Appointment ID: " + appointment.getAppointmentID());
        System.out.println("Date: " + appointment.getDate());
        System.out.println("Time: " + appointment.getTime());
        System.out.println("Doctor ID: " + appointment.getDoctorID());
        System.out.println("Patient ID: " + appointment.getPatientID());
        System.out.println("Type of Service: " + outcome.getTypeOfService());
        System.out.println("Medication: " + prescription.getMedicationName());
        System.out.println("Dosage: " + prescription.getDosage());
        System.out.println("Status: " + prescription.getStatus());
        System.out.println("------------------------");
    }

    public void updatePrescriptionStatus() {
        loadAppointments();
        viewPendingPrescriptions();
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter Appointment ID to dispense prescription (or 'cancel' to exit): ");
        String appointmentId = scanner.nextLine();
        
        if (appointmentId.equalsIgnoreCase("cancel")) return;

        Optional<AppointmentSlot> appointmentOpt = appointments.stream()
            .filter(apt -> apt.getAppointmentID().equals(appointmentId))
            .findFirst();

        if (appointmentOpt.isPresent()) {
            AppointmentSlot appointment = appointmentOpt.get();
            Prescription prescription = appointment.getAppointmentOutcomeRecord().getPrescribedMedication();
            
            if (prescription.getStatus() == PrescriptionStatus.PENDING) {
                boolean stockUpdated = inventory.updateMedication(
                    prescription.getMedicationName(), 
                    prescription.getDosage(), 
                    false
                );
                
                if (stockUpdated) {
                    prescription.setStatus(PrescriptionStatus.DISPENSED);
                    saveAppointments();
                    System.out.println("Prescription successfully dispensed.");
                    
                    // Check for low stock alert
                    Medication med = inventory.getMedication(prescription.getMedicationName());
                    if (med != null && med.isLowStockAlert()) {
                        System.out.println("\nLOW STOCK ALERT");
                        System.out.println("Medication: " + med.getMedicationName());
                        System.out.println("Current Stock: " + med.getStock());
                        System.out.println("Low Stock Threshold: " + med.getLowStockValue());
                    }
                } else {
                    System.out.println("Failed to dispense - Insufficient stock.");
                }
            } else {
                System.out.println("This prescription has already been dispensed.");
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }

    public void submitReplenishmentRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter medication name: ");
        String medicationName = scanner.nextLine();
        System.out.print("Enter quantity to request: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        try (PrintWriter writer = new PrintWriter(new FileWriter("ReplenishRequestsList.csv", true))) {
            writer.println(medicationName + "," + quantity + ",PENDING");
            System.out.println("Replenishment request submitted successfully.");
        } catch (IOException e) {
            System.out.println("Error submitting replenishment request: " + e.getMessage());
        }
    }

    public void viewMedicationInventory() {
        inventory.viewInventory();
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

    // public static void main(String[] args) {
    //     Inventory inventory = new Inventory("Medicine_List.csv");
    //     Pharmacist pharmacist = new Pharmacist("Pharm123", "securepass");

    //     Scanner scanner = new Scanner(System.in);
    //     boolean running = true;

    //     while (running) {
    //         System.out.println("\n==== Pharmacist Menu ====");
    //         System.out.println("1. View Inventory");
    //         System.out.println("2. View Pending Prescriptions");
    //         System.out.println("3. Dispense Prescriptions");
    //         System.out.println("4. Submit Replenish Request");
    //         System.out.println("5. View Appointment Details");
    //         System.out.println("6. Exit");
    //         System.out.print("Choose an option: ");

    //         int choice = scanner.nextInt();
    //         scanner.nextLine();

    //         switch (choice) {
    //             case 1:
    //                 System.out.println("\nInventory:");
    //                 pharmacist.viewInventory(inventory);
    //                 break;
    //             case 2:
    //                 System.out.println("\nPending Prescriptions:");
    //                 List<Prescription> pending = pharmacist.viewPendingPrescriptions();
    //                 if (pending.isEmpty()) {
    //                     System.out.println("No pending prescriptions.");
    //                 } else {
    //                     for (int i = 0; i < pending.size(); i++) {
    //                         Prescription p = pending.get(i);
    //                         System.out.println((i + 1) + ". Medication: " + p.getMedicationName() + 
    //                                         ", Dosage: " + p.getDosage() + 
    //                                         ", Status: " + p.getStatus());
    //                     }
    //                 }
    //                 break;
    //             case 3:
    //                 System.out.println("\nPending Prescriptions:");
    //                 List<Prescription> pendingForDispense = pharmacist.viewPendingPrescriptions();
    //                 if (pendingForDispense.isEmpty()) {
    //                     System.out.println("No pending prescriptions to dispense.");
    //                 } else {
    //                     for (Prescription p : pendingForDispense) {
    //                         System.out.println("Medication: " + p.getMedicationName() +
    //                                         ", Dosage: " + p.getDosage());
    //                     }
    //                     System.out.print("\nEnter medication name to dispense: ");
    //                     String medToDispense = scanner.nextLine();

    //                     if (pharmacist.updatePrescriptionStatus(medToDispense)) {
    //                         System.out.println("Prescription successfully dispensed.");
    //                     } else {
    //                         System.out.println("Failed to dispense prescription.");
    //                     }
    //                 }
    //                 break;
    //             case 4:
    //                 System.out.print("Enter medication name to replenish: ");
    //                 String medName = scanner.nextLine();
    //                 System.out.print("Enter stock amount: ");
    //                 int stockAmount = scanner.nextInt();
    //                 scanner.nextLine();
    //                 pharmacist.submitReplenishRequest(medName, stockAmount);
    //                 break;
    //             case 5:
    //                 System.out.print("Enter Appointment ID to view details: ");
    //                 String aptId = scanner.nextLine();
    //                 boolean found = false;
    //                 for (AppointmentSlot apt : pharmacist.appointments) {
    //                     if (apt.getAppointmentID().equals(aptId)) {
    //                         pharmacist.displayAppointmentDetails(apt);
    //                         found = true;
    //                         break;
    //                     }
    //                 }
    //                 if (!found) {
    //                     System.out.println("Appointment not found.");
    //                 }
    //                 break;
    //             case 6:
    //                 running = false;
    //                 System.out.println("Exiting Pharmacist system.");
    //                 break;
    //             default:
    //                 System.out.println("Invalid choice. Please try again.");
    //         }
    //     }
    //     scanner.close();
    // }
}

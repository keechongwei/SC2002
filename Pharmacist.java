import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Pharmacist extends Staff {
    private List<AppointmentSlot> appointments;
    private final Inventory inventory;

    // Primary constructor with basic authentication details
    public Pharmacist(String HospitalID, String password) {
        super(HospitalID, password);
        this.appointments = new ArrayList<>();
        this.inventory = new Inventory("Medicine_List.csv");
        loadAppointments();
    }

    // Constructor with all the details
    public Pharmacist(String HospitalID, String name, String gender, String age) {
        super(HospitalID, "password");
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.appointments = new ArrayList<>();
        this.inventory = new Inventory("Medicine_List.csv");

    }

    public void printMenu() {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        boolean running = true;

        while(running) {
            try {
                System.out.println("\n=== PHARMACIST MENU, ENTER CHOICE ===");
                System.out.println("(1) View Appointment Outcome Record");
                System.out.println("(2) Update Prescription Status");
                System.out.println("(3) View Medication Inventory");
                System.out.println("(4) Submit Replenishment Request");
                System.out.println("(5) Logout");
                
                if (sc.hasNextInt()) {
                    choice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    switch(choice) {
                        case 1:
                            this.viewAllAppointmentOutcomes();
                            break;
                        case 2:
                            this.updatePrescriptionStatus(sc); // Pass scanner as parameter
                            break;
                        case 3:
                            this.viewMedicationInventory();
                            break;
                        case 4:
                            this.submitReplenishmentRequest(sc); // Pass scanner as parameter
                            break;
                        case 5:
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                } else {
                    System.out.println("Please enter a valid number.");
                    sc.nextLine(); // Clear invalid input
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                sc.nextLine(); // Clear any bad input
            }
        }
        System.out.println("Logging out...");
        // Don't close the scanner here as it might be needed by the calling method
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
                // Remove any leading/trailing spaces from parts
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                if (parts.length >= 7 && parts[5].trim().equals("COMPLETED")) {
                    String outcomeStr = parts[6].trim();
                    if (!outcomeStr.equals("null")) {
                        AppointmentOutcomeRecord outcome = AppointmentOutcomeRecord.fromCSV(outcomeStr);
                        if (outcome != null && outcome.getPrescribedMedication() != null) {
                            AppointmentSlot appointment = new AppointmentSlot(
                                parts[0].trim(), 
                                parts[1].trim(), 
                                AppointmentStatus.valueOf(parts[5].trim()),
                                parts[3].trim(), 
                                parts[4].trim(), 
                                outcome,
                                parts[2].trim()
                            );
                            appointments.add(appointment);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error processing appointments: " + e.getMessage());
        }
    }

    private void saveAppointments() {
        List<String> allLines = new ArrayList<>();
        allLines.add("Date;Time;AppointmentID;DoctorID;PatientID;Status;OutcomeRecord");
        
        try (BufferedReader br = new BufferedReader(new FileReader("Appointments.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(";");
                if (parts.length >= 3) {
                    String appointmentId = parts[2].trim();
                    // Check if this appointment is in our modified list
                    Optional<AppointmentSlot> modifiedAppointment = appointments.stream()
                        .filter(a -> a.getAppointmentID().equals(appointmentId))
                        .findFirst();
                    
                    if (modifiedAppointment.isPresent()) {
                        allLines.add(modifiedAppointment.get().toCSV());
                    } else {
                        allLines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading appointments: " + e.getMessage());
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("Appointments.csv"))) {
            for (String line : allLines) {
                writer.println(line);
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

        // Sort appointments to show PENDING first
        appointments.sort((a1, a2) -> {
            Prescription p1 = a1.getAppointmentOutcomeRecord().getPrescribedMedication();
            Prescription p2 = a2.getAppointmentOutcomeRecord().getPrescribedMedication();
            if (p1 == null) return 1;
            if (p2 == null) return -1;
            return p2.getStatus().compareTo(p1.getStatus());
        });

        System.out.println("\n=== All Appointment Outcomes (Pending First) ===");
        for (AppointmentSlot appointment : appointments) {
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

    public void updatePrescriptionStatus(Scanner scanner) { // Accept scanner as parameter
        loadAppointments();
        viewAllAppointmentOutcomes();
        
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
                Medication med = inventory.getMedication(prescription.getMedicationName());
                if (med != null && med.getStock() < prescription.getDosage()) {
                    System.out.println("ERROR: Insufficient stock. Required dosage: " + 
                        prescription.getDosage() + ", Available stock: " + med.getStock());
                return;
            }
                
                boolean stockUpdated = inventory.updateMedication(
                    prescription.getMedicationName(), 
                    prescription.getDosage(), 
                    false
                );
                
                if (stockUpdated) {
                    prescription.setStatus(PrescriptionStatus.DISPENSED);
                    appointments.sort((a1, a2) -> {
                        Prescription p1 = a1.getAppointmentOutcomeRecord().getPrescribedMedication();
                        Prescription p2 = a2.getAppointmentOutcomeRecord().getPrescribedMedication();
                        if (p1 == null) return 1;
                        if (p2 == null) return -1;
                        return p2.getStatus().compareTo(p1.getStatus());
                    });

                    for (AppointmentSlot slot : AppointmentManager.appointmentSlotArray) {
                        if (slot.getAppointmentID().equals(appointment.getAppointmentID())) {
                            slot.getAppointmentOutcomeRecord().getPrescribedMedication().setStatus(PrescriptionStatus.DISPENSED);
                            break;
                        }
                    }

                    saveAppointments();
                    AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
                    System.out.println("Prescription successfully dispensed.");
                }
            } else {
                System.out.println("This prescription has already been dispensed.");
            }
        } else {
            System.out.println("Appointment not found.");
        }
    }

    public void submitReplenishmentRequest(Scanner scanner) { // Accept scanner as parameter
        inventory.viewInventory();
        
        System.out.print("\nEnter medication name to replenish (or 'cancel' to exit): ");
        String medicationName = scanner.nextLine();
        
        if (medicationName.equalsIgnoreCase("cancel")) return;
        
        Medication med = inventory.getMedication(medicationName);
        if (med != null) {
            if (!inventory.checkReplenishRequestExists(medicationName)) {
                System.out.print("Enter quantity to request: ");
                if (scanner.hasNextInt()) {
                    int quantity = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (quantity > 0) {
                        inventory.submitReplenishRequest(medicationName, quantity);
                    } else {
                        System.out.println("Quantity must be greater than 0.");
                    }
                } else {
                    System.out.println("Please enter a valid number.");
                    scanner.nextLine(); // Clear invalid input
                }
            } else {
                System.out.println("A pending replenishment request already exists for " + medicationName);
            }
        } else {
            System.out.println("Medication not found in inventory.");
        }
    }

    public void viewMedicationInventory() {
        inventory.viewInventory();  // This will now show both inventory and pending requests
        
        // Check for any low stock alerts and handle replenishment
        List<Medication> lowStockMeds = inventory.updateAllAlertLevels();
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

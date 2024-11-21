import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The {@code Pharmacist} class represents a pharmacist in the hospital system.
 * Pharmacists manage prescriptions, appointments, and medication inventory.
 * This class provides methods to interact with the hospital's database of
 * appointments and inventory.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class Pharmacist extends Staff {
    /**
     * A list of appointment slots assigned to the pharmacist.
     */
    private List<AppointmentSlot> appointments;

    /**
     * An {@code InventoryManager} instance for managing medication inventory.
     */
    private final InventoryManager inventory;

    /**
     * Constructs a {@code Pharmacist} object with basic authentication details.
     * 
     * @param HospitalID the unique ID of the pharmacist within the hospital system.
     * @param password   the password for the pharmacist's account.
     */
    public Pharmacist(String HospitalID, String password) {
        super(HospitalID, password);
        this.appointments = new ArrayList<>();
        this.inventory = new InventoryManager("Medicine_List.csv");
        loadAppointments();
    }

    /**
     * Constructs a {@code Pharmacist} object with detailed personal information.
     * 
     * @param HospitalID the unique ID of the pharmacist within the hospital system.
     * @param name       the name of the pharmacist.
     * @param gender     the gender of the pharmacist.
     * @param age        the age of the pharmacist.
     */
    public Pharmacist(String HospitalID, String name, String gender, String age) {
        super(HospitalID, "password");
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.appointments = new ArrayList<>();
        this.inventory = new InventoryManager("Medicine_List.csv");

    }

    /**
     * Displays the pharmacist's menu and handles user interaction.
     * Allows the pharmacist to perform tasks like viewing appointment outcomes,
     * updating prescription statuses, and managing inventory.
     */
    public void printMenu() {
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
                
                choice = InputValidator.getIntegerInput("Enter your choice: ", 1, 5);

                switch(choice) {
                    case 1:
                        this.viewAllAppointmentOutcomes();
                        break;
                    case 2:
                        this.updatePrescriptionStatus();
                        break;
                    case 3:
                        this.viewMedicationInventory();
                        break;
                    case 4:
                        this.submitReplenishmentRequest();
                        break;
                    case 5:
                        running = false;
                        break;
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        System.out.println("Logging out...");
    }

    /**
     * Loads completed appointment data from a CSV file and filters for appointments
     * that have pending prescriptions.
     */
    public void loadAppointments() {
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

    /**
     * Saves the updated appointment data back to the CSV file.
     */
    public void saveAppointments() {
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

    /**
     * Displays all appointment outcomes sorted by prescription status,
     * with pending prescriptions shown first.
     */
    private void viewAllAppointmentOutcomes() {
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

    /**
     * Displays detailed information about a specific appointment, including prescription status.
     * 
     * @param appointment the appointment to display.
     */
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

    /**
     * Updates the status of a pending prescription to dispensed if sufficient stock is available.
     */
    public void updatePrescriptionStatus() {
        loadAppointments();
        viewAllAppointmentOutcomes();
        
        String appointmentId = InputValidator.getAppointmentId(
            "\nEnter Appointment ID to dispense prescription (or 'cancel' to exit): "
        );
        
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

    /**
     * Submits a request to replenish stock for a specific medication.
     */
    public void submitReplenishmentRequest() {
        inventory.viewInventory();
        
        String medicationName = InputValidator.getMedicationName("\nEnter medication name to replenish (or 'cancel' to exit): ");
        
        if (medicationName.equalsIgnoreCase("cancel")) return;
        
        Medication med = inventory.getMedication(medicationName);
        if (med != null) {
            if (!inventory.checkReplenishRequestExists(medicationName)) {
                int quantity = InputValidator.getIntegerInput("Enter quantity to request: ", 1, 1000);
                
                inventory.submitReplenishRequest(medicationName, quantity);
            } else {
                System.out.println("A pending replenishment request already exists for " + medicationName);
            }
        } else {
            System.out.println("Medication not found in inventory.");
        }
    }

    /**
     * Displays the medication inventory along with any pending replenishment requests.
     */
    public void viewMedicationInventory() {
        inventory.viewInventory();  // This will now show both inventory and pending requests
        
        // Check for any low stock alerts and handle replenishment
        List<Medication> lowStockMeds = inventory.updateAllAlertLevels();
    }


    /**
     * Converts the pharmacist's details into a CSV-formatted string.
     * 
     * @return a CSV string representation of the pharmacist's details.
     */
    public String toCSV() {
        // Combine all attributes into a CSV string
        return super.getHospitalID() + ";" + super.getPassword() + ";" + name + ";" + "Pharmacist" + ";" + gender + ";" + age;
    }
}

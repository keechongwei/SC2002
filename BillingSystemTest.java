import java.io.*;
import java.util.*;
public class BillingSystemTest {
    public static void main(String[] args) {
        // First, let's create our test appointments file
        try {
            createTestAppointments();
            
            // Create and run billing system
            System.out.println("=== Starting Billing System Test ===\n");
            BillingSystem billingSystem = new BillingSystem();
            
            // Process all completed appointments
            System.out.println("Processing appointments...\n");
            billingSystem.processCompletedAppointments();
            
            // View bills for each test patient
            System.out.println("\n=== Viewing Bills for Each Patient ===");
            System.out.println("\nPatient P1001 Bills (Consultation with Ibuprofen):");
            billingSystem.viewPatientBills("P1001");
            
            System.out.println("\nPatient P1002 Bills (X-Ray no medication):");
            billingSystem.viewPatientBills("P1002");
            
            System.out.println("\nPatient P1003 Bills (Consultation with multiple medications):");
            billingSystem.viewPatientBills("P1003");
            
            System.out.println("\nPatient P1004 Bills (Blood Test with Paracetamol):");
            billingSystem.viewPatientBills("P1004");
            
            // Test payment processing for one bill
            System.out.println("\n=== Testing Payment Processing ===");
            billingSystem.processPayment();
            
        } catch (Exception e) {
            System.out.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTestAppointments() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("appointments.csv"))) {
            // Write header
            writer.println("Date;Time;Appointment ID;DoctorID;PatientID;Appointment Status;OutcomeDate|Outcome Time|Type Of Service|Medication Name^Medication Status^Medication Dosage|Consultation Notes");
            
            // Test Case 1: Consultation with single medication
            writer.println("2024-11-17;09:00;APT1;D001;P1001;COMPLETED;2024-11-17|09:00|CONSULTATION|Ibuprofen^DISPENSED^10|null");
            
            // Test Case 2: X-Ray with no medication
            writer.println("2024-11-17;10:00;APT2;D002;P1002;COMPLETED;2024-11-17|10:00|XRAY|null|null");
            
            // Test Case 3: Consultation with multiple medications
            writer.println("2024-11-17;11:00;APT3;D001;P1003;COMPLETED;2024-11-17|11:00|CONSULTATION|Paracetamol^DISPENSED^20|Amoxicillin^DISPENSED^15|null");
            
            // Test Case 4: Blood Test with single medication
            writer.println("2024-11-17;12:00;APT4;D003;P1004;COMPLETED;2024-11-17|12:00|BLOOD_TEST|Paracetamol^DISPENSED^5|null");
            
            // Test Case 5: Incomplete appointment (should be ignored)
            writer.println("2024-11-17;13:00;APT5;D001;P1005;SCHEDULED;null|null|CONSULTATION|null|null");
            
            System.out.println("Test appointments created successfully.");
        } catch (IOException e) {
            System.out.println("Error creating test appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
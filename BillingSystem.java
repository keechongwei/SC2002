import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BillingSystem {
    private static final String BILLING_FILE = "Bills.csv";
    private static final String APPOINTMENTS_FILE = "appointments.csv";
    private static final double CONSULTATION_FEE = 50.00;

    private static final Map<String, Double> MEDICATION_PRICES = new HashMap<>() {{
        put("Paracetamol", 0.1);
        put("Ibuprofen", 0.5);
        put("Amoxicillin", 0.2);
    }};
    
    private static class Bill {
        String billId;
        String patientId;
        String appointmentId;
        LocalDateTime date;
        double consultationFee;
        Map<String, Integer> medications;
        double medicationTotal;
        double totalAmount;
        boolean isPaid;

        public Bill(String patientId, String appointmentId, LocalDateTime date) {
            String timestamp = String.valueOf(System.currentTimeMillis());
            this.billId = "B" + timestamp.substring(timestamp.length() - 4);
            this.patientId = patientId;
            this.appointmentId = appointmentId;
            this.date = date;
            this.consultationFee = CONSULTATION_FEE;
            this.medications = new HashMap<>();
            this.medicationTotal = 0.0;
            this.totalAmount = 0.0;
            this.isPaid = false;
        }

        public String getBillID() {
            return this.billId;
        }

        public String toCSV() {
            StringBuilder medicationsStr = new StringBuilder();
            for (Map.Entry<String, Integer> med : medications.entrySet()) {
                medicationsStr.append(med.getKey()).append("^").append(med.getValue()).append("|");
            }
            
            return String.format("%s;%s;%s;%s;%.2f;%s;%.2f;%.2f;%b",
                billId, patientId, appointmentId,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                consultationFee, medicationsStr.toString(),
                medicationTotal, totalAmount, isPaid
            );
        }
    }

    private List<Bill> bills;

    public BillingSystem() {
        this.bills = new ArrayList<>();
        loadBills();
    }

    private void loadBills() {
        File file = new File(BILLING_FILE);
        if (!file.exists()) {
            saveBills();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(";");
                if (parts.length < 9) continue;

                Bill bill = new Bill(parts[1], parts[2], LocalDateTime.parse(parts[3]));
                bill.billId = parts[0];
                bill.consultationFee = Double.parseDouble(parts[4]);
                
                if (!parts[5].isEmpty()) {
                    String[] medStrings = parts[5].split("\\|");
                    for (String medStr : medStrings) {
                        if (!medStr.isEmpty()) {
                            String[] medParts = medStr.split("\\^");
                            if (medParts.length >= 2) {
                                bill.medications.put(medParts[0], Integer.parseInt(medParts[1]));
                        }
                    }
                }
                
                bill.medicationTotal = Double.parseDouble(parts[6]);
                bill.totalAmount = Double.parseDouble(parts[7]);
                bill.isPaid = Boolean.parseBoolean(parts[8]);
                
                bills.add(bill);
            }
        } }catch (IOException e) {
            System.out.println("Error loading bills: " + e.getMessage());
        }
    }

    public void processCompletedAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader(APPOINTMENTS_FILE))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(";");
                if (parts.length < 7) continue;  // Changed to 7 since we need at least 7 fields
                
                // Trim all parts to remove any whitespace
                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }
                
                if (!"COMPLETED".equalsIgnoreCase(parts[5])) {
                    continue;
                }
                
                String appointmentId = parts[2];
                if (billExistsForAppointment(appointmentId)) {
                    continue;
                }

                String patientId = parts[4];
                LocalDateTime appointmentDateTime = LocalDateTime.of(
                    LocalDate.parse(parts[0]),
                    LocalTime.parse(parts[1])
                );
                
                Bill bill = new Bill(patientId, appointmentId, appointmentDateTime);
                
                // Parse medications from the appointment format
                String[] outcomeInfo = parts[6].split("\\|");  // Changed from parts[7] to parts[6]
                if (outcomeInfo.length >= 4) {
                    String medicationInfo = outcomeInfo[3];
                    String[] medInfo = medicationInfo.split("\\^");
                    if (medInfo.length >= 3 && "DISPENSED".equals(medInfo[1])) {
                        String medicationName = medInfo[0];
                        try {
                            int dosage = Integer.parseInt(medInfo[2]);
                            bill.medications.put(medicationName, dosage);
                            System.out.println("Added medication: " + medicationName + " with dosage: " + dosage);
                        } catch (NumberFormatException e) {
                            System.out.println("Error parsing dosage for " + medicationName + ": " + medInfo[2]);
                        }
                    }
                }
                
                calculateTotals(bill);
                bills.add(bill);
                System.out.println("Created new bill: " + bill.billId + " for appointment: " + appointmentId);
                System.out.println("Medication total: $" + bill.medicationTotal);
            }
            saveBills();
            
        } catch (IOException e) {
            System.out.println("Error processing appointments: " + e.getMessage());
            e.printStackTrace();  
        }
    }

    private boolean billExistsForAppointment(String appointmentId) {
        return bills.stream().anyMatch(bill -> appointmentId.equals(bill.appointmentId));
    }

    private void calculateTotals(Bill bill) {
        double medicationTotal = 0.0;
        for (Map.Entry<String, Integer> med : bill.medications.entrySet()) {
            double price = MEDICATION_PRICES.getOrDefault(med.getKey(), 1.0);
            double subtotal = price * med.getValue();
            medicationTotal += subtotal;
            System.out.println(String.format("Calculating %s: %d units * $%.2f = $%.2f", 
                med.getKey(), med.getValue(), price, subtotal));  // Debug line
        }
        
        bill.medicationTotal = medicationTotal;
        bill.totalAmount = bill.consultationFee + medicationTotal;
    }

    private void saveBills() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BILLING_FILE))) {
            writer.println("BillID;PatientID;AppointmentID;Date;ConsultationFee;Prescriptions;MedicationTotal;TotalAmount;IsPaid");
            for (Bill bill : bills) {
                writer.println(bill.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error saving bills: " + e.getMessage());
        }
    }

    public void markAsPaid(String billId) {
        Bill bill = findBill(billId);
        if (bill != null) {
            bill.isPaid = true;
            saveBills();
            System.out.println("Bill " + billId + " marked as paid successfully!");
        } else {
            System.out.println("Bill not found: " + billId);
        }
    }

    public void displayBill(String billId) {
        Bill bill = findBill(billId);
        if (bill == null) {
            System.out.println("Bill not found!");
            return;
        }

        System.out.println("\n=== HOSPITAL BILL ===");
        System.out.println("Bill ID: " + bill.billId);
        System.out.println("Patient ID: " + bill.patientId);
        System.out.println("Appointment ID: " + bill.appointmentId);
        System.out.println("Date: " + bill.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("\nConsultation Fee: $" + String.format("%.2f", bill.consultationFee));
        
        if (!bill.medications.isEmpty()) {
            System.out.println("\nPrescribed Medications:");
            System.out.println("-".repeat(40));
            for (Map.Entry<String, Integer> med : bill.medications.entrySet()) {
                double price = MEDICATION_PRICES.getOrDefault(med.getKey(), 1.0);
                double itemTotal = price * med.getValue();
                System.out.printf("%-20s x%d @ $%.2f = $%.2f%n", 
                    med.getKey(), 
                    med.getValue(), 
                    price,
                    itemTotal);
            }
        }
        
        System.out.printf("Medication Total: $%.2f%n", bill.medicationTotal);
        System.out.println("-".repeat(40));
        System.out.printf("Total Amount: $%.2f%n", bill.totalAmount);
        System.out.println("Payment Status: " + (bill.isPaid ? "PAID" : "PENDING"));
        System.out.println("=".repeat(40));
    }

    public void viewPatientBills(String patientId) {
        boolean found = false;
        System.out.println("Searching for bills with patient ID: " + patientId); // Debug line
        for (Bill bill : bills) {
            System.out.println("Checking bill with patient ID: " + bill.patientId); // Debug line
            if (bill.patientId.equals(patientId)) {
                displayBill(bill.billId);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No bills found for patient " + patientId);
        }
    }

    private Bill findBill(String billId) {
        return bills.stream()
            .filter(b -> b.billId.equals(billId))
            .findFirst()
            .orElse(null);
    }

    public void processPayment() {
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n=== Payment Processing ===");
                System.out.print("Enter Bill ID (or 'exit' to cancel): ");
                String billId = sc.nextLine().trim();
                
                if (billId.equalsIgnoreCase("exit")) {
                    System.out.println("Payment processing cancelled.");
                    break;
                }
                
                Bill bill = findBill(billId);
                if (bill == null) {
                    System.out.println("Error: Bill ID " + billId + " not found.");
                    continue;
                }
                
                if (bill.isPaid) {
                    System.out.println("This bill has already been paid.");
                    continue;
                }
                
                // Display bill details for confirmation
                displayBill(billId);
                
                System.out.print("\nConfirm payment for Bill " + billId + "? (yes/no): ");
                String confirm = sc.nextLine().trim().toLowerCase();
                
                if (confirm.equals("yes")) {
                    markAsPaid(billId);
                    System.out.println("Payment processed successfully!");
                    break;
                } else {
                    System.out.println("Payment cancelled.");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred during payment processing: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        // Initialize billing system
        BillingSystem billingSystem = new BillingSystem();

        // Process completed appointments
        billingSystem.processCompletedAppointments();


        System.out.println("\n=== Bills for Patient P1001 ===");
        billingSystem.viewPatientBills("P1001");

        System.out.println("\nDebug Information:");
        System.out.println("Total bills in system: " + billingSystem.bills.size());
        for (Bill bill : billingSystem.bills) {
            System.out.println("Found bill: " + bill.billId + " for patient: " + bill.patientId);
        }

        billingSystem.processPayment();

        System.out.println("\nTest completed successfully!");
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Login {
    enum Role{Unknown,Patient,Doctor,Pharmacist,Administrator}
    /* remember to convert functions involving appointments to print name cause appointment slots only have ID
       cause rn its just doctor and patient ID */
    static List<Patient> patients = new ArrayList<Patient>();
    static List<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
    static List<Administrator> administrators = new ArrayList<Administrator>();
    static List<Doctor> doctors = new ArrayList<Doctor>();
    static String staffCSVHeader = "Staff ID; Password ; Name ; Role ; Gender ; Age";
    static File medicineFile = new File("Medicine_List.csv");
    static File patientRecordsFile = new File("Patient_List.csv");
    static File staffRecordsFile = new File("Staff_List.csv");
    static final File staffRecordsFile2 = new File("Staff_List2.csv"); //eventually will just be Staff_List.csv
    static Role role = Role.Unknown;
    static boolean loggedIn = false;
    static boolean validID = false;
    static boolean validPassword = false;

    // initialises appointment array
    public static void initializeAppointments() {
        if (!((AppointmentManager.csvFile).exists()) || (AppointmentManager.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("appointments.csv is empty or missing. Generating daily appointments...");
            AppointmentManager.writeHeader(AppointmentManager.appointmentsCSVHeader);
            AppointmentManager.makeDailyAppointments(doctors); // Replace getStaffList() with your method to get the staff data
        } else {
            // File exists and is not empty, load appointments from the CSV
            System.out.println("Loading appointments from appointments.csv...");
            AppointmentManager.loadAppointmentsFromCSV(AppointmentManager.csvFile);
        }
    }
    // function used to initialise patient, staff, medicine data
    private static void initialise(){
        boolean headerline = true;
        try{
            Scanner scanner = new Scanner(patientRecordsFile);
            while (scanner.hasNextLine()) {
                if(headerline){
                    headerline = false;
                    continue;
                }
                else{
                        String line = scanner.nextLine();
                        String[] patientFields = line.split(";");
                        String patientID = patientFields[0];
                        String name = patientFields[1];
                        String dateofbirth = patientFields[2];
                        String gender = patientFields[3];
                        String bloodType = patientFields[4];
                        String emailAddress = patientFields[5];
                        String pastDiagnoses = patientFields.length > 6 ? patientFields[6] : "";
                        String pastTreatments = patientFields.length > 7 ? patientFields[7] : "";
                        String phoneNumber = "";
                        Patient p = new Patient(patientID, name, dateofbirth, gender, bloodType, phoneNumber, emailAddress);
                        patients.add(p);
                }
            }
            System.out.println("Patients Information Retrieved Successfully!");
        } catch (FileNotFoundException e){
            System.out.println("Unable to Retrieve Patients Information!");
            e.printStackTrace();
        }
        headerline = true;
        try{
            Scanner scanner = new Scanner(staffRecordsFile);
            while (scanner.hasNextLine()) {
                if(headerline){
                    headerline = false;
                    continue;
                }
                else{
                     // staffs.add(getRecordFromLine(scanner.nextLine()));
                     // create list of staff
                     String line = scanner.nextLine();
                     String[] fields = line.split(";");
                     if (fields[2].equals("Doctor")){
                        Doctor d = new Doctor(fields[0],fields[1],fields[3],fields[4]);
                        doctors.add(d);
                     }
                     else if (fields[2].equals("Pharmacist")){
                        Pharmacist ph = new Pharmacist(fields[0],fields[1],fields[3],fields[4]);
                        pharmacists.add(ph);
                     }
                     else if (fields[2].equals("Administrator")){
                        Administrator adm = new Administrator(fields[0],fields[1],fields[3],fields[4]);
                        administrators.add(adm);
                     }
                }
            }
            System.out.println("Staff Information Retrieved Successfully!");
            //System.out.println(staffs);
        } catch (FileNotFoundException e){
            System.out.println("Unable to Retrieve Staff Information!");
            e.printStackTrace();
        }
        try{
            Inventory.loadMedicationsFromCSV(Inventory.csvFilePath);
            System.out.println("Medicine Information Retrieved Successfully!");
        } catch (FileNotFoundException e){
            System.out.println("Unable to Retrieve Medicine Information!");
            e.printStackTrace();
        }
    }
    // function used to read data from csv files
    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        Scanner rowScanner = new Scanner(line);
        rowScanner.useDelimiter(";");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        return values;
    }
    private static void IDCheck(String ID){
        for(Patient p : patients){
            if (p.getHospitalID().equals(ID)){
                validID = true;
                role = Role.Patient;
            } 
        }
        for(Doctor d : doctors){
            if (d.getHospitalID().equals(ID)){
                validID = true;
                role = Role.Doctor;
            } 
        }
        for(Pharmacist ph : pharmacists){
            if (ph.getHospitalID().equals(ID)){
                validID = true;
                role = Role.Pharmacist;
            } 
        }
        for(Administrator adm : administrators){
            if (adm.getHospitalID().equals(ID)){
                validID = true;
                role = Role.Administrator;
            } 
        }
    }
    private static void passwordCheck(String password){
        Scanner sc = new Scanner(System.in);
        // for first login, password is password
        if (password.equals("password")){
            System.out.println("Change Your Password: ");
            password = sc.nextLine();
            System.out.println("Verify Your New Password: ");
            String check = sc.nextLine();
            while (!check.equals(password)){
                System.out.println("Password Change Failed.");
                System.out.println("Change Your Password: ");
                password = sc.nextLine();
                System.out.println("Verify Your New Password: ");
                check = sc.nextLine();  
            }
            validPassword = true;
        }
        else{
            System.out.println("Invalid Password");
        }
    }
    private static void updateStaffCSV(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(staffRecordsFile2))) {
            // Write the header line first
            bw.write(staffCSVHeader);
            bw.newLine();
            for (Doctor d : doctors) {
                bw.write(d.toCSV());
                bw.newLine();
            }
            for (Pharmacist ph : pharmacists) {
                bw.write(ph.toCSV());
                bw.newLine();
            }
            for (Administrator adm : administrators) {
                bw.write(adm.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV file: " + e.getMessage());
        }
    }
    private static void updateAppointmentsCSV(){
        AppointmentManager.writeHeader(AppointmentManager.appointmentsCSVHeader);
        AppointmentManager.writeCSV(AppointmentManager.appointmentSlotArray);
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String ID = "NULL";
        String password = "NULL";
        initialise(); // loads in data from csv
        while(!loggedIn){
            System.out.println("=== HOSPITAL MANAGEMENT SYSTEM LOGIN PAGE ===");
            System.out.println("Enter Hospital ID: ");
            ID = sc.nextLine();
            IDCheck(ID);
            while (!validID){
                System.out.println("Invalid ID");
                System.out.println("Enter Hospital ID: ");
                ID = sc.nextLine();
                IDCheck(ID);  
            }
            while (!validPassword){
                System.out.println("Enter Password: ");
                password = sc.nextLine();
                password = password.toLowerCase();
                passwordCheck(password);
            }
            loggedIn = true;
        }
        System.out.println("Successful Login!");
        initializeAppointments();
        int choice = 0;
        switch(role){
            case Patient:
            Patient curPat = null;
            for (Patient pat : patients) {
                if (pat.getMedicalRecord().getPatientID().equals(ID)) {
                    curPat = pat;
                    curPat.setPassword(password); 
                    break;
                }
            }
            choice = 0;
            while(choice != 9){
                System.out.println("=== PATIENT MENU, ENTER CHOICE ===");
                System.out.println("(1) View Medical Record");
                System.out.println("(2) Update Personal Information");
                System.out.println("(3) View Available Appointment Slots");
                System.out.println("(4) Schedule An Appointment");
                System.out.println("(5) Reschedule An Appointment");
                System.out.println("(6) Cancel An Appointment");
                System.out.println("(7) View Scheduled Appointments");
                System.out.println("(8) View Past Appointment Outcome Records");
                System.out.println("(9) Logout");
                choice = sc.nextInt();

                switch(choice){
                    case 1:
                    curPat.viewMedicalRecord();
                    break;
                    case 2:
                    curPat.updatePersonalInfo();// Update Personal Information
                    break;
                    case 3:
                    curPat.viewAvailAppointmentSlot();// View Available Appointment Slots
                    break;
                    case 4:
                    curPat.scheduleAppointments();// Schedule An Appointment
                    break;
                    case 5:
                    curPat.rescheduleAppointment();// Reschedule An Appointment
                    break;
                    case 6:
                    curPat.cancelAppointment();// Cancel An Appointment
                    break;
                    case 7:
                    curPat.viewAppointmentStatus();// View Scheduled Appointments
                    break;
                    case 8:
                    curPat.viewAppointmentOutcomeRecord();// View Past Appointments Outcome Record
                    break;
                    case 9:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                }
            }
            break;
            case Doctor:
            Doctor d = null;
            for (Doctor doctor : doctors){
                if (doctor.getDoctorID().equals(ID)) {
                    d = doctor; 
                    d.setPassword(password);
                    break;
                }
            }
            d.addPatient(patients.get(0));
            choice = 0;
            while(choice != 8){
                System.out.println("=== DOCTOR MENU, ENTER CHOICE ===");
                System.out.println("(1) View Patient Medical Record");
                System.out.println("(2) Update Patient Medical Record");
                System.out.println("(3) View Personal Schedule");
                System.out.println("(4) Set Availability For Appointments");
                System.out.println("(5) Accept Or Decline Appointment Requests");
                System.out.println("(6) View Upcoming Appointments");
                System.out.println("(7) Record Appointment Outcome");
                System.out.println("(8) Logout");
                choice = sc.nextInt();
                switch(choice){
                    case 1:
                    d.viewPatientRecords();
                    break;
                    case 2:
                    d.updatePatientRecord();
                    break;
                    case 3:
                    d.viewPersonalSchedule(); // View Personal Schedule
                    break;
                    case 4:
                    d.setAvailabilityForAppointments();// Set Availability For Appointments
                    break;
                    case 5:
                    d.acceptOrDeclineAppointments();// Accept Or Decline Appointment Requests
                    break;
                    case 6:
                    d.viewUpcomingAppointment();
                    break;
                    case 7:
                    d.makeAppointmentOutcomeRecord();
                    break;
                    case 8:
                    System.out.println("Logging out...");
                    break;
                }
            }
            break;
            case Pharmacist:
            Pharmacist pharmacist = new Pharmacist("P001", "password", "male", "29");
            for (Pharmacist ph : pharmacists){
                if (ph.getHospitalID().equals(ID)) {
                    pharmacist = ph; 
                    ph.setPassword(password);
                    break;
                }
            }
            choice = 0;
            while(choice != 6){
                System.out.println("=== PHARMACIST MENU, ENTER CHOICE ===");
                System.out.println("(1) View Appointment Outcome Record");
                System.out.println("(2) View Pending Prescriptions");
                System.out.println("(3) Update Prescription Status");
                System.out.println("(4) View Medication Inventory");
                System.out.println("(5) Submit Replenishment Request");
                System.out.println("(6) Logout");
                choice = sc.nextInt();

                
                switch(choice) {
                    case 1:
                        pharmacist.viewAllAppointmentOutcomes();
                        break;
                    case 2:
                        pharmacist.viewPendingPrescriptions();
                        break;
                    case 3:
                        pharmacist.updatePrescriptionStatus();
                        break;
                    case 4:
                        pharmacist.viewMedicationInventory();
                        break;
                    case 5:
                        pharmacist.submitReplenishmentRequest();
                        break;
                }
            }
            break;
            case Administrator:
            Administrator curAdmin;
            for (Administrator adm : administrators){
                if (adm.getHospitalID().equals(ID)) {
                    curAdmin = adm; 
                    adm.setPassword(password);
                    break;
                }
            }
            choice = 0;
            while(choice != 5){
                System.out.println("=== ADMINISTRATOR MENU, ENTER CHOICE ===");
                System.out.println("(1) View and Manage Hospital Staff");
                System.out.println("(2) View Appointments Details");
                System.out.println("(3) View and Manage Medication History");
                System.out.println("(4) Approve Replenishment Requests");
                System.out.println("(5) Logout");
                choice = sc.nextInt();

                Administrator administrator = new Administrator("A001", "password");

                // Made manage staff and inventroy non-static for security
                switch(choice){
                    case 1:
                    // View and Manage Hospital Staff
                    administrator.manageStaff();
                    break;
                    case 2:
                    // View Appointments Details
                    AppointmentManager.viewAllAppointments();
                    break;
                    case 3:
                    // View and Manage Medication History
                    administrator.manageInventory();
                    break;
                    case 4:
                    // Approve Replenishment Request
                    administrator.approveReplenishmentRequest();
                    break;
                }
            }
            break;
        }
        updateStaffCSV();
        updateAppointmentsCSV();
    }   
}
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Login {
    enum Role{Unknown,Patient,Doctor,Pharmacist,Administrator}
    static List<List<String>> patients = new ArrayList<>();
    static List<List<String>> staffs = new ArrayList<>();
    static List<List<String>> medicines = new ArrayList<>();
    static File medicineFile = new File("Medicine_List.csv");
    static File patientRecordsFile = new File("Patient_List.csv");
    static File staffRecordsFile = new File("Staff_List.csv");
    static Role role = Role.Unknown;
    static boolean loggedIn = false;
    static boolean validID = false;
    static boolean validPassword = false;
    // function used to initialise patient, staff, medicine data
    private static void initialise(){
        boolean headerline = true;
        try{
            Scanner scanner = new Scanner(patientRecordsFile);
            while (scanner.hasNextLine()) {
                if(headerline){
                    headerline = false;
                    getRecordFromLine(scanner.nextLine());
                }
                else{
                    patients.add(getRecordFromLine(scanner.nextLine()));
                }
            }
            System.out.println("Patients Information Retrieved Successfully!");
            //System.out.println(patients);
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
                    getRecordFromLine(scanner.nextLine());
                }
                else{
                    staffs.add(getRecordFromLine(scanner.nextLine()));
                }
            }
            System.out.println("Staff Information Retrieved Successfully!");
            //System.out.println(staffs);
        } catch (FileNotFoundException e){
            System.out.println("Unable to Retrieve Staff Information!");
            e.printStackTrace();
        }
        headerline = true;
        try{
            Scanner scanner = new Scanner(medicineFile);
            while (scanner.hasNextLine()) {
                if(headerline){
                    headerline = false;
                    getRecordFromLine(scanner.nextLine());
                }
                else{
                    medicines.add(getRecordFromLine(scanner.nextLine()));
                }
            }
            System.out.println("Medicine Information Retrieved Successfully!");
            //System.out.println(medicines);
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
        for(List<String> patient : patients){
            if (patient.get(0).equals(ID)){
                validID = true;
                role = Role.Patient;
            } 
        }
        for(List<String> staff : staffs){
            String temp = staff.get(2);
            if (staffs.get(0).equals(ID)){
                validID = true;
                if (temp.equals("Doctor")){
                    role = role.Doctor;
                }
                else if (temp.equals("Pharmacist")){
                    role = role.Pharmacist;
                }
                else if (temp.equals("Administrator")){
                    role = role.Administrator;
                }
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
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String ID = "NULL";
        String password = "NULL";
        initialise(); // loads in data from csv
        while(!loggedIn){
            System.out.println("HOSPITAL MANAGEMENT SYSTEM LOGIN PAGE");
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
        User curUser = new User(ID,password);
        int choice = 0;
        switch(role){
            case Patient:
            //Patient curPat = (Patient) curUser;
            choice = 0;
            while(choice != 9){
                System.out.println("PATIENT MENU, ENTER CHOICE");
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
                    // View Medical Record
                    break;
                    case 2:
                    // Update Personal Information
                    break;
                    case 3:
                    // View Available Appointment Slots
                    break;
                    case 4:
                    // Schedule An Appointment
                    break;
                    case 5:
                    // Reschedule An Appointment
                    break;
                    case 6:
                    // Cancel An Appointment
                    break;
                    case 7:
                    // View Scheduled Appointments
                    break;
                    case 8:
                    // View Past Appointments Outcome Record
                    break;
                }
            }
            break;
            case Doctor:
            Doctor curDoc = (Doctor) curUser;
            choice = 0;
            while(choice != 8){
                System.out.println("DOCTOR MENU, ENTER CHOICE");
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
                    // View Patient Medical Record
                    break;
                    case 2:
                    // Update Patient Medical Record
                    break;
                    case 3:
                    // View Personal Schedule
                    break;
                    case 4:
                    // Set Availability For Appointments
                    break;
                    case 5:
                    // Accept Or Decline Appointment Requests
                    break;
                    case 6:
                    // View Upcoming Appointments
                    break;
                    case 7:
                    // Record Appointment Outcome
                    break;
                }
            }
            break;
            case Pharmacist:
            //Pharmacist curPharm = (Pharmacist) curUser;
            choice = 0;
            while(choice != 5){
                System.out.println("PHARMACIST MENU, ENTER CHOICE");
                System.out.println("(1) View Appointment Outcome Record");
                System.out.println("(2) Update Prescription Status");
                System.out.println("(3) View Medication Inventory");
                System.out.println("(4) Submit Replenishment Request");
                System.out.println("(5) Logout");
                choice = sc.nextInt();
                switch(choice){
                    case 1:
                    // View Appointment Outcome  Record
                    break;
                    case 2:
                    // Update Prescription Status
                    break;
                    case 3:
                    // View Medication Inventory
                    break;
                    case 4:
                    // Submit Replenishment Request
                    break;
                }
            }
            break;
            case Administrator:
            //Administrator curAdmin = (Administrator) curUser;
            choice = 0;
            while(choice != 5){
                System.out.println("ADMINISTRATOR MENU, ENTER CHOICE");
                System.out.println("(1) View and Manage Hospital Staff");
                System.out.println("(2) View Appointments Details");
                System.out.println("(3) View and Manage Medication History");
                System.out.println("(4) Approve Replenishment Requests");
                System.out.println("(5) Logout");
                choice = sc.nextInt();
                switch(choice){
                    case 1:
                    // View and Manage Hospital Staff
                    break;
                    case 2:
                    // View Appointments Details
                    break;
                    case 3:
                    // View and Manage Medication History
                    break;
                    case 4:
                    // Approve Replenishment Request
                    break;
                }
            }
            break;
        }

    }   
}
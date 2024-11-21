package utility;
import java.util.Scanner;

import Users.Administrator;
import Users.Doctor;
import Users.Patient;
import Users.Pharmacist;
import Users.User;
import managers.*;
import managers.csvhandlers.*;

/**
 * Handles the login process for the Hospital Management System, including 
 * user authentication, password validation, and initialization of data 
 * for patients, staff, medicines, and appointments.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class Login {

    /**
     * Indicates whether the user is logged in.
     */
    static boolean loggedIn = false;

    /**
     * Indicates whether the provided ID is valid.
     */
    static boolean validID = false;

    /**
     * Indicates whether the provided password is valid.
     */
    static boolean validPassword = false;

    /**
     * Stores the user ID of the current user.
     */
    static String ID = "NULL";

    /**
     * Stores the password of the current user.
     */
    static String password = "NULL";

    /**
     * Represents the currently logged-in user.
     */
    static User user;
    
    /**
     * Helper method to initialise alls manager and show success or error message.
     * @param manager 
     * @param managerType A string representing the type of manager to intialise
     */
    private static void initialiseManager(Manager manager, String managerType) {
        try {
            manager.initialise();
            System.out.printf("%s Information Retrieved Successfully!%n", managerType);
        } catch (Exception e) {
            System.out.printf("Unable to Retrieve %s Information!%n", managerType);
            e.printStackTrace();
        }
    }

    /**
     * Initializes the system by loading patient, staff, medicine, and appointment data.
     * Displays success or error messages depending on the outcome of each initialization step.
     */
    public static void initialise() {
        initialiseManager(new PatientManager(), "Patients");
        initialiseManager(new StaffManager(), "Staff");
        initialiseManager(new InventoryManager("Medicine_List.csv"), "Medicine");
        initialiseManager(new AppointmentManager(), "Appointments");
    }
    
    /**
     * Validates the provided ID against known IDs for patients, doctors, pharmacists, and administrators.
     *
     * @param ID the ID to validate
     */
    public static void IDCheck(String ID){
        for(Patient p : PatientManager.allPatients ){
            if (p.getHospitalID().equalsIgnoreCase(ID)){
                validID = true;
            } 
        }
        for(Doctor d : StaffManager.doctors){
            if (d.getHospitalID().equals(ID)){
                validID = true;
            } 
        }
        for(Pharmacist ph : StaffManager.pharmacists){
            if (ph.getHospitalID().equalsIgnoreCase(ID)){
                validID = true;
            } 
        }
        for(Administrator adm : StaffManager.administrators){
            if (adm.getHospitalID().equalsIgnoreCase(ID)){
                validID = true;
            } 
        }
    }

    /**
     * Validates the provided password for the specified user ID.
     * If the password is a default password ("password"), the user is required to change it.
     *
     * @param ID       the user ID
     * @param password the password to validate
     */
    public static void passwordCheck(String ID,String password){
        Scanner sc = new Scanner(System.in);
        for(Patient p : PatientManager.allPatients ){
            if (p.getHospitalID().equalsIgnoreCase(ID)){    
                user = (Patient) p;    
                if (p.getPassword().equals("password")){
                    password = InputValidator.getPassword("First Time Login! Change Your Password (Ensure at least 8 characters and 1 number): ");
                    String check = InputValidator.getPassword("Verify Your New Password: ");
                    while (!check.equals(password) || password.length() == 0 ){
                        System.out.println("Password Change Failed.");
                        password = InputValidator.getPassword("Change Your Password (Ensure at least 8 characters and 1 number): ");
                        check = InputValidator.getPassword("Verify Your New Password: ")  ;
                    }
                    validPassword = true;
                    p.setPassword(password);
                }
                else if (p.getPassword().equals(password)){
                        validPassword = true;
                    }
                else{
                    System.out.println("Invalid Password.");
                    validPassword = false;
                }
            }
        }
        for(Doctor d : StaffManager.doctors){
            if (d.getHospitalID().equalsIgnoreCase(ID)){
                user = (Doctor) d;        
                if (d.getPassword().equals("password")){
                    password = InputValidator.getPassword("First Time Login! Change Your Password (Ensure at least 8 characters and 1 number): ");
                    String check = InputValidator.getPassword("Verify Your New Password: ");
                    while (!check.equals(password) || password.length() == 0 ){
                        System.out.println("Password Change Failed.");
                        password = InputValidator.getPassword("Change Your Password (Ensure at least 8 characters and 1 number): ");
                        check = InputValidator.getPassword("Verify Your New Password: ")  ;
                    }
                    validPassword = true;
                    d.setPassword(password);
                }
                else if (d.getPassword().equals(password)){
                        validPassword = true;
                    }
                else{
                    System.out.println("Invalid Password.");
                    validPassword = false;
                }
            }
        }
        for(Pharmacist ph : StaffManager.pharmacists){
            if (ph.getHospitalID().equals(ID)){ 
                user = (Pharmacist) ph;        
                if (ph.getPassword().equals("password")){
                    password = InputValidator.getPassword("First Time Login! Change Your Password (Ensure at least 8 characters and 1 number): ");
                    String check = InputValidator.getPassword("Verify Your New Password: ");
                    while (!check.equals(password) || password.length() == 0 ){
                        System.out.println("Password Change Failed.");
                        password = InputValidator.getPassword("Change Your Password (Ensure at least 8 characters and 1 number): ");
                        check = InputValidator.getPassword("Verify Your New Password: ")  ;
                    }
                    validPassword = true;
                    ph.setPassword(password);
                }
                else if (ph.getPassword().equals(password)){
                        validPassword = true;
                    }
                else{
                    System.out.println("Invalid Password.");
                    validPassword = false;
                }
        }
    }
        for(Administrator adm : StaffManager.administrators){
            if (adm.getHospitalID().equalsIgnoreCase(ID)){   
                user = (Administrator) adm;      
                if (adm.getPassword().equals("password")){
                    password = InputValidator.getPassword("First Time Login! Change Your Password (Ensure at least 8 characters and 1 number): ");
                    String check = InputValidator.getPassword("Verify Your New Password: ");
                    while (!check.equals(password) || password.length() == 0 ){
                        System.out.println("Password Change Failed.");
                        password = InputValidator.getPassword("Change Your Password (Ensure at least 8 characters and 1 number): ");
                        check = InputValidator.getPassword("Verify Your New Password: ")  ;
                    }
                    validPassword = true;
                    adm.setPassword(password);
                }
                else if (adm.getPassword().equals(password)){
                        validPassword = true;
                    }
                else{
                    System.out.println("Invalid Password.");
                    validPassword = false;
                }
            }
        }
        if (validPassword){
            PatientManagerCSVHandler.writeCSV();
            PatientManagerCSVHandler.loadCSV();
            StaffCSVHandler.writeCSV();
            StaffCSVHandler.loadCSV();
        }
    }
    /**
     * Handles the login process, requiring the user to provide valid credentials.
     *
     * @param loggedIn indicates whether the user is currently logged in
     * @param sc       the {@code Scanner} used for user input
     */
    public static void login(boolean loggedIn, Scanner sc){
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
                passwordCheck(ID,password);
            }
            loggedIn = true;
            System.out.println("Successful Login!");
        }
    }

    /**
     * Main entry point for the login system. Initializes data, manages login, and handles user session.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        initialise(); // loads in data from csv
        PatientRegistration.printMenu();
        login(loggedIn,sc);
        user.printMenu();
        PatientManagerCSVHandler.writeCSV();
        StaffCSVHandler.writeCSV();
        AppointmentCSVHandler.writeCSV(AppointmentManager.appointmentSlotArray);
        sc.close();
    }   
}
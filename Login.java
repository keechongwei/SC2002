import java.util.Scanner;

public class Login {
    /* remember to convert functions involving appointments to print name cause appointment slots only have ID
       cause rn its just doctor and patient ID */
    static boolean loggedIn = false;
    static boolean validID = false;
    static boolean validPassword = false;
    static String ID = "NULL";
    static String password = "NULL";
    static User user;
    
    // function used to initialise patient, staff, medicine data
    private static void initialise(){
        AppointmentManager.initialiseAppointments();
        try{
            PatientManager.loadRecordsCSV();
            System.out.println("Patients Information Retrieved Successfully!");
        } catch (Exception e){
            System.out.println("Unable to Retrieve Patients Information!");
            e.printStackTrace();
        }
        try{
            StaffManager.loadRecordsCSV();
            System.out.println("Staff Information Retrieved Successfully!");
            //System.out.println(staffs);
        } catch (Exception e){
            System.out.println("Unable to Retrieve Staff Information!");
            e.printStackTrace();
        }
        try{
            Inventory.loadMedicationsFromCSV(Inventory.csvFilePath);
            System.out.println("Medicine Information Retrieved Successfully!");
        } catch (Exception e){
            System.out.println("Unable to Retrieve Medicine Information!");
            e.printStackTrace();
        }
    }
    private static void IDCheck(String ID){
        for(Patient p : PatientManager.allPatients ){
            if (p.getHospitalID().equals(ID)){
                validID = true;
            } 
        }
        for(Doctor d : StaffManager.doctors){
            if (d.getHospitalID().equals(ID)){
                validID = true;
            } 
        }
        for(Pharmacist ph : StaffManager.pharmacists){
            if (ph.getHospitalID().equals(ID)){
                validID = true;
            } 
        }
        for(Administrator adm : StaffManager.administrators){
            if (adm.getHospitalID().equals(ID)){
                validID = true;
            } 
        }
    }
    private static void passwordCheck(String ID,String password){
        Scanner sc = new Scanner(System.in);
        for(Patient p : PatientManager.allPatients ){
            if (p.getHospitalID().equals(ID)){    
                user = (Patient) p;    
                if (p.getPassword().equals("password")){
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
            if (d.getHospitalID().equals(ID)){
                user = (Doctor) d;        
                if (d.getPassword().equals("password")){
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
            if (adm.getHospitalID().equals(ID)){   
                user = (Administrator) adm;      
                if (adm.getPassword().equals("password")){
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
    }

    private static void login(boolean loggedIn, Scanner sc){
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
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        initialise(); // loads in data from csv
        login(loggedIn,sc);
        user.printMenu();
        PatientManager.writeAllRecords();
        StaffManager.updateStaffCSV();
        AppointmentCSVHandler.updateAppointmentsCSV();
    }   

}
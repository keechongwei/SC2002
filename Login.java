import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Login {
    enum Role{Unknown,Patient,Doctor,Pharmacist,Administrator}
    static List<List<String>> patients = new ArrayList();
    static List<List<String>> staffs = new ArrayList<>();
    static List<List<String>> medicines = new ArrayList<>();
    static File medicineFile = new File("Medicine_List.csv");
    static File patientRecordsFile = new File("Patient_List.csv");
    static File staffRecordsFile = new File("Staff_List.csv");
    static Role role = Role.Unknown;
    static boolean loggedIn = false;
    static boolean validID = false;
    static boolean validPassword = false;
    // fucntion used to initialise patient, staff, medicine data
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
        for(int i = 0; i<patients.size();i++){
            if (patients.get(i).get(0).equals(ID)){
                validID = true;
                role = Role.Patient;
            } 
        }
        for(int i = 0; i<staffs.size();i++){
            String temp = staffs.get(i).get(0);
            if (staffs.get(i).get(0).equals(ID)){
                validID = true;
                if (temp == "Doctor"){
                    role = role.Doctor;
                }
                else if (temp == "Pharmacist"){
                    role = role.Pharmacist;
                }
                else if (temp == "Administrator"){
                    role = role.Administrator;
                }
            } 
        }
    }
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String ID;
        String password;
        initialise();
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
            System.out.println("Enter Password: ");
            password = sc.nextLine();
            password = password.toLowerCase();
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
                System.out.println("Invalid ID");
            }
            loggedIn = true;
        }
        System.out.println("Successful Login!");
        switch(role){
            case Patient:
            //implement Patient Menu
            break;
            case Doctor:
            //implement Doctor Menu
            break;
            case Administrator:
            break;
            case Pharmacist:
            break;
        }

    }   
}
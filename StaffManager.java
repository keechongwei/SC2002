import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class StaffManager {
    static String staffCSVHeader = "Staff ID; Password ; Name ; Role ; Gender ; Age";
    static List<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
    static List<Administrator> administrators = new ArrayList<Administrator>();
    static List<Doctor> doctors = new ArrayList<Doctor>();
    public static final File csvFile = new File("Staff_List_Copy.csv"); 
    static String staffRecordsCSV = "Staff_List_Copy.csv";
    static boolean headerline = true;

    static Scanner input_scanner = new Scanner(System.in);

    public static void loadRecordsCSV(){
        try (Scanner scanner = new Scanner(csvFile)){
            while (scanner.hasNextLine()) {
                if(headerline){
                    String line = scanner.nextLine();
                    headerline = false;
                    continue;
                }
                else{
                    String line = scanner.nextLine();
                    String[] fields = line.split(";");
                    if (fields[3].equals("Doctor")){
                        if(!(fields[1].equals("password"))){
                            Doctor d = new Doctor(fields[0],fields[2],fields[4],fields[5]);
                            d.setPassword(fields[1]);
                            doctors.add(d);
                        }
                        else{
                            Doctor d = new Doctor(fields[0],fields[2],fields[4],fields[5]);
                            doctors.add(d);     
                        }
                    }
                    else if (fields[3].equals("Pharmacist")){
                        if(!(fields[1].equals("password"))){
                            Pharmacist ph = new Pharmacist(fields[0],fields[2],fields[4],fields[5]);
                            ph.setPassword(fields[1]);
                            pharmacists.add(ph);
                        }
                        else{
                            Pharmacist ph = new Pharmacist(fields[0],fields[2],fields[4],fields[5]);
                            pharmacists.add(ph);
                        }
                    }
                    else if (fields[3].equals("Administrator")){
                        if(!(fields[1].equals("password"))){
                            Administrator adm = new Administrator(fields[0],fields[2],fields[4],fields[5]);
                            adm.setPassword(fields[1]);
                            administrators.add(adm);
                        }
                        else{
                            Administrator adm = new Administrator(fields[0],fields[2],fields[4],fields[5]);
                            administrators.add(adm);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("Unable to Retrieve Patients Information!");
            e.printStackTrace();
        }
    }

    public static void updateStaffCSV(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
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

    public static void updateStaff() {
        System.out.println("Enter HospitalID: ");
        String hospitalID = input_scanner.nextLine();
    
        String roleLetter = hospitalID.substring(0,1);

        List<User> temp;

        // Flag to check if found
        boolean found = false;

        if (roleLetter.equals("P")) {
            temp = pharmacists;
        } else if (roleLetter.equals("D")) {
            temp = doctors;
        } else if (roleLetter.equals("A")) {
            temp = administrators;
        } else {
            System.out.println("Invalid Hospital ID");
            return;
        }

        if name:
        tme.setName(dfssdfds)


    
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(hospitalID + ";")) {
                String[] fields = lines.get(i).split(";");

                // Update found flag to true;
                found = true;
                int roleHandlingFlag = 0;
    
                // Split the existing line to get the current details
                String[] details = lines.get(i).split(";");
                String name = details[1];
                String role = details[2];
                String gender = details[3];
                String age = details[4];
    
                System.out.println("Pick category to update: ");
                System.out.println("1 - Name");
                System.out.println("2 - Role");
                System.out.println("3 - Gender");
                System.out.println("4 - Age");
    
                int input = input_scanner.nextInt();
                input_scanner.nextLine(); // Skip \n
    
                switch (input) {
                    case 1:
                        System.out.println("Name: ");
                        name = input_scanner.nextLine();
                        break;
    
                    case 2:
                        System.out.println("Pick the Role: ");
                        System.out.println("1 - Doctor");
                        System.out.println("2 - Pharmacist");
                        System.out.println("3 - Administrator");
                        int role_choice = input_scanner.nextInt();
                        input_scanner.nextLine();
                    
                        switch (role_choice) {
                            case 1:
                                role = "Doctor";

                                // Assuming fields[2] is previous role, if there is a change in role, from another to Doctor, update doctor handling 
                                if(!role.equalsIgnoreCase(fields[2])) {
                                    roleHandlingFlag = 1;
                                }
                                
                                break;
                            case 2:
                                role = "Pharmacist";
                                // Assuming fields[2] is previous role, if there is a change in role, from another to Doctor, update doctor handling 
                                if(!role.equalsIgnoreCase(fields[2])) {
                                    roleHandlingFlag = 2;
                                }
                                break;
                            case 3:
                                role = "Administrator";
                                if(!role.equalsIgnoreCase(fields[2])) {
                                    roleHandlingFlag = 3;
                                }
                                break;
                            default:
                                System.out.println("Invalid role choice. Keeping the current role.");
                                break;
                        }
                        break;
    
                    case 3:
                        System.out.println("Pick the Gender: ");
                        System.out.println("1 - Male");
                        System.out.println("2 - Female");
                        int gender_choice = input_scanner.nextInt();
                        input_scanner.nextLine();

                        switch (gender_choice) {
                            case 1:
                                gender = "Male";
                                break;
                            case 2:
                                gender = "Female";
                                break;
                            default:
                                System.out.println("Invalid gender choice. Keeping the current gender.");
                                break;
                        }
                        break;
    
                    case 4:
                        System.out.println("Age: ");
                        age = input_scanner.nextLine();
                        break;
    
                    default:
                        System.out.println("Invalid input noob.");
                        break;
                }

                // if switch to diff role, handle change in ID or/and DoctorHandling 
                if (roleHandlingFlag == 1) {
                    hospitalID = getNextID(staffs, role);
                    boolean check = doctorHandling(hospitalID, name, gender, age, true);
                    
                    if (check != true) {
                        System.out.println("Unable to add Doctor");
                        return;
                    } 
                } else { // Edit here to handle different incremenitng
                    hospitalID = getNextID(staffs, role);
                }

                // Replace the old line with the updated details
                lines.set(i, String.join(";", hospitalID, name, role, gender, age));
                break;
            }

        } 
        if (found) {
            writeCSVFile(lines, staffRecordsCSV);
        } else {
            System.out.println("Invalid HospitalID");
        }
        
    }

    public static boolean doctorHandling(String hospitalID, String name, String gender, String age, boolean addOrRemove) {
        // true means adding new doctor
        boolean removed = false;
        if (addOrRemove == true) {
            Doctor newDoc = new Doctor(hospitalID, name, gender, age);
            List<Doctor> temp = new ArrayList<>();
            temp.add(newDoc);
            AppointmentManager.makeDailyAppointments(temp); 
            return true;
        } else {
            //Remove doc appointments from CSV
            System.out.println("");

            // If still have pending appointments for the doctor, stop removal, return false
            return removed;
        }

    }
}

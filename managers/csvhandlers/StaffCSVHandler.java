package managers.csvhandlers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import Users.Administrator;
import Users.Doctor;
import Users.Pharmacist;
import managers.StaffManager;

/**
 * A CSVHandler object meant to write and read from Staff_List_Copy.csv
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */

public class StaffCSVHandler implements CSVHandler{
    /**
     * Header String for appointments.csv
     */
    static String staffCSVHeader = "Staff ID; Password ; Name ; Role ; Gender ; Age";
    /**
     * File object to write to and read from
     */
    public static final File csvFile = new File("Staff_List_Copy.csv"); 

    /**
    * Overridden Method from CSVHandler Interface.
    * Loads information from a csv file
    * For this class, loads appointments based on csvFile attribute of StaffCSVHandler from Staff_List_Copy.csv
    * @param void
    * @return void
    */
    public static void loadCSV(){
        boolean headerline = true;
        try (Scanner scanner = new Scanner(csvFile)){
            while (scanner.hasNextLine()) {
                if(headerline){
                    scanner.nextLine();
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
                            d.addPatientsUnderCare();
                            StaffManager.doctors.add(d);
                        }
                        else{
                            Doctor d = new Doctor(fields[0],fields[2],fields[4],fields[5]);
                            d.addPatientsUnderCare();
                            StaffManager.doctors.add(d);     
                        }
                    }
                    else if (fields[3].equals("Pharmacist")){
                        if(!(fields[1].equals("password"))){
                            Pharmacist ph = new Pharmacist(fields[0],fields[2],fields[4],fields[5]);
                            ph.setPassword(fields[1]);
                            StaffManager.pharmacists.add(ph);
                        }
                        else{
                            Pharmacist ph = new Pharmacist(fields[0],fields[2],fields[4],fields[5]);
                            StaffManager.pharmacists.add(ph);
                        }
                    }
                    else if (fields[3].equals("Administrator")){
                        if(!(fields[1].equals("password"))){
                            Administrator adm = new Administrator(fields[0],fields[2],fields[4],fields[5]);
                            adm.setPassword(fields[1]);
                            StaffManager.administrators.add(adm);
                        }
                        else{
                            Administrator adm = new Administrator(fields[0],fields[2],fields[4],fields[5]);
                            StaffManager.administrators.add(adm);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("Unable to Retrieve Patients Information!");
            e.printStackTrace();
        }
    }

    /** 
     * Writes Staff information to Staff_List_Copy.csv
     * Retrieves Data From StaffManager to update Staff_List_Copy.csv
     * @param void
     * @return void
     * @see StaffManager
     */
    public static void writeCSV(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            // Write the header line first
            bw.write(staffCSVHeader);
            bw.newLine();
            for (Doctor d : StaffManager.doctors) {
                bw.write(d.toCSV());
                bw.newLine();
            }
            for (Pharmacist ph : StaffManager.pharmacists) {
                bw.write(ph.toCSV());
                bw.newLine();
            }
            for (Administrator adm : StaffManager.administrators) {
                bw.write(adm.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV file: " + e.getMessage());
        }
    }
}


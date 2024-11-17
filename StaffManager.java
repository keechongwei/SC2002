import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaffManager {
    static String staffCSVHeader = "Staff ID; Password ; Name ; Role ; Gender ; Age";
    static List<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
    static List<Administrator> administrators = new ArrayList<Administrator>();
    static List<Doctor> doctors = new ArrayList<Doctor>();
    public static final File csvFile = new File("Staff_List_Copy.csv"); 
    static boolean headerline = true;

    public static void loadRecordsCSV(){
        try (Scanner scanner = new Scanner(csvFile)){
            while (scanner.hasNextLine()) {
                if(headerline){
                    headerline = false;
                    continue;
                }
                else{
                    String line = scanner.nextLine();
                    String[] fields = line.split(";");
                    if (fields[2].equals("Doctor")){
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
                    else if (fields[2].equals("Pharmacist")){
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
                    else if (fields[2].equals("Administrator")){
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

}

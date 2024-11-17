import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Administrator extends Staff {
    enum Filter_type{Name,Role,Gender,Age}
    static List<List<String>> staffs = new ArrayList<>();
    static String staffRecordsCSV = "Staff_List.csv";
    static String medicineListCSV = "Medicine_List.csv";
    static String replenishRecordsCSV = "Replenish_Request_List.csv";
    private String gender;
    private int age;
    private String name;

    Scanner input_scanner = new Scanner(System.in);

	private Object _attribute;

     // Constructor for Administrator class
     public Administrator(String HospitalID, String password) {
        super(HospitalID, password);
    }

     public Administrator(String HospitalID, String name, String gender, String age) {
        super(HospitalID, "password");
        this.name = name;
        this.gender = gender;
        this.age = Integer.valueOf(age);
    }

    public void printMenu(){
        Scanner sc = new Scanner(System.in);
        int choice = 0;
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
                this.manageStaff();
                break;
                case 2:
                // View Appointments Details
                AppointmentManager.viewAllAppointments();
                break;
                case 3:
                // View and Manage Medication History
                this.manageInventory();
                break;
                case 4:
                // Approve Replenishment Request
                this.approveReplenishmentRequest();
                break;
            }
        }
    }
    public void setPassword(String password){
        super.setPassword(password);
    }

    public String toCSV() {
        // Combine all attributes into a CSV string
        return super.getHospitalID() + ";" + super.getPassword() + ";" + name + ";" + "Administrator" + ";" + gender + ";" + age;
    }
    
    public static void main(String[] args) {
        // Create an Administrator instance
        Administrator admin = new Administrator("Hospital123", "Password123");
    
        // Initialize staff details
        //admin.printDoubleList(staffs);

        // Filter staff 
        //List<List<String>> filteredStaffs = admin.filterStaff(Filter_type.Name, "John");
    
        // Print the filtered staff list
        ///System.out.println("Filtered Staff List:");
        //admin.printDoubleList(filteredStaffs);

        
        admin.manageStaff();

        //admin.manageInventory();

        //admin.approveReplenishmentRequest();

        //Administrator.viewAllAppointments();
    }

    private static void initialise_staff_details(){
        boolean headerline = true;
        // Scanner scanner = new Scanner(staffRecordsFile);
        // while (scanner.hasNextLine()) {
        //     if(headerline){
        //         headerline = false;
        //         getRecordFromLine(scanner.nextLine());
        //     }
        //     else{
        //         staffs.add(getRecordFromLine(scanner.nextLine()));
        //     }
        // }
        List<String> temp = readCSVFile(staffRecordsCSV);

            // Loop through the lines, skipping the header
        for (String line : temp) {
            if (headerline) {
                headerline = false; // Skip the header line
                continue;
            }

            // Split each line by ';' and add as a list to 'staffs'
            List<String> staffDetails = List.of(line.split(";"));
            staffs.add(staffDetails);
        }
        
        System.out.println("Staff Information Retrieved Successfully!");
        //System.out.println(staffs);
    }

    // For filtering staff by age
	public static List<List<String>> filterStaff(Filter_type filter_type) {        
        if (filter_type != Filter_type.Age) {System.out.println("Please enter a valid integer value for age");}

        List<List<String>> filteredStaffs = new ArrayList<List<String>>(staffs);
        Collections.sort(filteredStaffs, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> staff1, List<String> staff2) {
                int age1 = Integer.parseInt(staff1.get(4)); // Assuming age is at index 4
                int age2 = Integer.parseInt(staff2.get(4));
                return Integer.compare(age1, age2);
            }
        });

        return filteredStaffs;
		//throw new UnsupportedOperationException();
	}

    // For sorting staff by age
	public static List<List<String>> filterStaff(Filter_type filter_type, int age) {
        
        if (filter_type != Filter_type.Age) {System.out.println("Please enter a valid integer value for age");}

        List<List<String>> filteredStaffs = new ArrayList<>();

        for(int i = 0; i<staffs.size();i++){
            int temp = Integer.parseInt(staffs.get(i).get(4));
            
            if (temp == age){
                filteredStaffs.add(staffs.get(i));
            }
        }

        return filteredStaffs;
		//throw new UnsupportedOperationException();
	}

    // For filtering staff by name role and gender
    public static List<List<String>> filterStaff(Filter_type filterType, String name_role_gender) {

        //System.out.println("filterbystr");

        List<List<String>> filteredStaffs = new ArrayList<>();
        int index = -1;

        // Edge case where user inputs null
        name_role_gender = name_role_gender.trim();
        if(name_role_gender == null) {
            return filteredStaffs;
        }
        // Capitalise input so that can match csv
        else {
            name_role_gender = name_role_gender.substring(0, 1).toUpperCase() + name_role_gender.substring(1).toLowerCase();
        }
        
        switch (filterType){
            case Name: index = 1; break;
            case Role: index = 2; break;
            case Gender: index = 3; break;
            default: index = -1; break;
        };

        if (index == -1) {
            System.out.println("Invalid filter type for String value.");
            filteredStaffs.clear();
            return filteredStaffs;
        }

        for (List<String> staff : staffs) {
            if (staff.get(index).contains(name_role_gender)) {
                filteredStaffs.add(staff);
            }
        }

        return filteredStaffs;
		//throw new UnsupportedOperationException();
	}

    public static void printDoubleList(List<List<String>> doubList) { 
        for (List<String> singleList : doubList) {
            for (String string : singleList) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }

	public void manageStaff() {

        initialise_staff_details();

        // Search by ID
        System.out.println("=== Staff Manager ===");
        System.out.println("Course of Action: ");
        System.out.println("1 - Search/ View Staff");
        System.out.println("2 - Remove Staff");
        System.out.println("3 - Update Staff");
        System.out.println("4 - Add Staff");

        int input = input_scanner.nextInt();
        input_scanner.nextLine(); // Clear newline

        switch (input) {
            case 1: 
                System.out.println("=== Staff List: ===");
                System.out.println("Filter by:");
                System.out.println("1 - Name");
                System.out.println("2 - Role");
                System.out.println("3 - Search by Age");
                System.out.println("4 - Age (Ascending)");
                System.out.println("5 - Gender");
                System.out.println("6 - View All");

                int manageStaff_choice = input_scanner.nextInt();
                input_scanner.nextLine(); // Clear newline

                // Filtered List of Staffs
                List<List<String>> filteredStaffs = new ArrayList<>();

                switch (manageStaff_choice) {
                    case 1:
                        System.out.println("Name: ");
                        String searchByname = input_scanner.nextLine();

                        filteredStaffs = Administrator.filterStaff(Filter_type.Name, searchByname);
                        break;

                    case 2:
                        System.out.println("Pick the Role: ");
                        System.out.println("1 - Doctor");
                        System.out.println("2 - Pharmacist");
                        System.out.println("3 - Administrator");
                        int role_choice = input_scanner.nextInt();
                        input_scanner.nextLine();
                        String role = "";
                    
                        switch (role_choice) {
                            case 1: role = "Doctor"; break;
                            case 2: role = "Pharmacist"; break;
                            case 3: role = "Administrator"; break;
                            default: System.out.println("Invalid role choice."); break;
                        }

                        filteredStaffs = Administrator.filterStaff(Filter_type.Role, role);
                        break;

                    case 3:
                        System.out.println("Age: ");
                        int searchByAge = input_scanner.nextInt();
                        input_scanner.nextLine();

                        filteredStaffs = Administrator.filterStaff(Filter_type.Age, searchByAge);
                        break;

                    case 4:
                        filteredStaffs = Administrator.filterStaff(Filter_type.Age);
                        break;

                    case 5:
                        System.out.println("Gender: ");
                        String gender_choice = input_scanner.nextLine();

                        filteredStaffs = Administrator.filterStaff(Filter_type.Gender, gender_choice);
                        break;    
        
                    case 6:
                        filteredStaffs = Administrator.filterStaff(Filter_type.Age);
                        break;

                    default:
                        System.out.println("Invalid Option.");
                        break;
                }

                if (filteredStaffs.size() == 0) {
                    System.out.println("None found.");
                } else {
                    Administrator.printDoubleList(filteredStaffs);
                }
                break;
            case 2: removeStaff(); break;
            case 3: StaffManager.updateStaff(); break;
            case 4: addStaff(); break;
            default: System.out.println("1 to 3 you dummy, gtfo"); break;
        }

		//throw new UnsupportedOperationException();
	}

    // private void updateStaff() {
    //     System.out.println("Enter HospitalID: ");
    //     String hospitalID = input_scanner.nextLine();
    
    //     List<String> lines = readCSVFile(staffRecordsCSV);

    //     // Flag to check if found
    //     boolean found = false;
    
    //     for (int i = 0; i < lines.size(); i++) {
    //         if (lines.get(i).startsWith(hospitalID + ";")) {
    //             String[] fields = lines.get(i).split(";");

    //             // Update found flag to true;
    //             found = true;
    //             int roleHandlingFlag = 0;
    
    //             // Split the existing line to get the current details
    //             String[] details = lines.get(i).split(";");
    //             String name = details[1];
    //             String role = details[2];
    //             String gender = details[3];
    //             String age = details[4];
    
    //             System.out.println("Pick category to update: ");
    //             System.out.println("1 - Name");
    //             System.out.println("2 - Role");
    //             System.out.println("3 - Gender");
    //             System.out.println("4 - Age");
    
    //             int input = input_scanner.nextInt();
    //             input_scanner.nextLine(); // Skip \n
    
    //             switch (input) {
    //                 case 1:
    //                     System.out.println("Name: ");
    //                     name = input_scanner.nextLine();
    //                     break;
    
    //                 case 2:
    //                     System.out.println("Pick the Role: ");
    //                     System.out.println("1 - Doctor");
    //                     System.out.println("2 - Pharmacist");
    //                     System.out.println("3 - Administrator");
    //                     int role_choice = input_scanner.nextInt();
    //                     input_scanner.nextLine();
                    
    //                     switch (role_choice) {
    //                         case 1:
    //                             role = "Doctor";

    //                             // Assuming fields[2] is previous role, if there is a change in role, from another to Doctor, update doctor handling 
    //                             if(!role.equalsIgnoreCase(fields[2])) {
    //                                 roleHandlingFlag = 1;
    //                             }
                                
    //                             break;
    //                         case 2:
    //                             role = "Pharmacist";
    //                             // Assuming fields[2] is previous role, if there is a change in role, from another to Doctor, update doctor handling 
    //                             if(!role.equalsIgnoreCase(fields[2])) {
    //                                 roleHandlingFlag = 2;
    //                             }
    //                             break;
    //                         case 3:
    //                             role = "Administrator";
    //                             if(!role.equalsIgnoreCase(fields[2])) {
    //                                 roleHandlingFlag = 3;
    //                             }
    //                             break;
    //                         default:
    //                             System.out.println("Invalid role choice. Keeping the current role.");
    //                             break;
    //                     }
    //                     break;
    
    //                 case 3:
    //                     System.out.println("Pick the Gender: ");
    //                     System.out.println("1 - Male");
    //                     System.out.println("2 - Female");
    //                     int gender_choice = input_scanner.nextInt();
    //                     input_scanner.nextLine();

    //                     switch (gender_choice) {
    //                         case 1:
    //                             gender = "Male";
    //                             break;
    //                         case 2:
    //                             gender = "Female";
    //                             break;
    //                         default:
    //                             System.out.println("Invalid gender choice. Keeping the current gender.");
    //                             break;
    //                     }
    //                     break;
    
    //                 case 4:
    //                     System.out.println("Age: ");
    //                     age = input_scanner.nextLine();
    //                     break;
    
    //                 default:
    //                     System.out.println("Invalid input noob.");
    //                     break;
    //             }

    //             // if switch to diff role, handle change in ID or/and DoctorHandling 
    //             if (roleHandlingFlag == 1) {
    //                 hospitalID = getNextID(staffs, role);
    //                 boolean check = doctorHandling(hospitalID, name, gender, age, true);
                    
    //                 if (check != true) {
    //                     System.out.println("Unable to add Doctor");
    //                     return;
    //                 } 
    //             } else { // Edit here to handle different incremenitng
    //                 hospitalID = getNextID(staffs, role);
    //             }

    //             // Replace the old line with the updated details
    //             lines.set(i, String.join(";", hospitalID, name, role, gender, age));
    //             break;
    //         }

    //     } 
    //     if (found) {
    //         writeCSVFile(lines, staffRecordsCSV);
    //     } else {
    //         System.out.println("Invalid HospitalID");
    //     }
        
    // }
    
    private void addStaff() {
        System.out.println("Enter new staff details:");
        System.out.println("Name: ");
        String name = input_scanner.nextLine();
        System.out.println("Role:");
        String role = input_scanner.nextLine();
        System.out.println("Gender: ");
        String gender = input_scanner.nextLine();
        System.out.println("Age: ");
        String age = input_scanner.nextLine();

        List<List<String>> filteredStaffs = new ArrayList<>();
        filteredStaffs = filterStaff(Filter_type.Role, role);

        String hospitalID = getNextID(filteredStaffs, role);

        List<String> lines = readCSVFile(staffRecordsCSV);
        lines.add(String.join(";", hospitalID, name, role, gender, age));

        writeCSVFile(lines, staffRecordsCSV);

        // If adding new Doctor, create new doctor's appt slots
        System.out.println(role);
        if(role.equals("Doctor")) {
            boolean check = StaffManager.doctorHandling(hospitalID, name, gender, age, true);
            if (check != true) {
                System.out.println("Unable to add Doctor");
                return;
            } 
        }

        System.out.println("New staff added successfully.");
    }

    private void removeStaff() {
        System.out.println("Enter HospitalID: ");
        String hospitalID = input_scanner.nextLine();

        List<String> lines = readCSVFile(staffRecordsCSV);
        boolean removed = lines.removeIf(line -> line.startsWith(hospitalID + ";"));

        if (removed) {
            writeCSVFile(lines, staffRecordsCSV);
            System.out.println("Staff with HospitalID " + hospitalID + " removed successfully.");
        } else {
            System.out.println("No staff found with HospitalID " + hospitalID + ".");
        }
    }

    public static List<String> readCSVFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return lines;
    }

    public static void writeCSVFile(List<String> lines, String CSVFile) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSVFile))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }

    private static String getNextID(List<List<String>> doubleList, String role) {
        int largest_ID  = 0;
        String role_letter = "";

        if (role.equalsIgnoreCase("Doctor")) {
            role_letter = "D";

        } else if (role.equalsIgnoreCase("Pharmacist")) {
            role_letter = "P";

        } else if (role.equalsIgnoreCase("Administrator")) {
            role_letter = "A";
            
        } else {
            System.out.println("Unknown Role");
            role_letter = "?";
        }

        for (List<String> singlList : doubleList) {
            if (singlList.get(0).substring(0,1).equals(role_letter)) {
                String temp = singlList.get(0);

                //Remove the first letter from the ID, to get the largeset index
                int ID_without_letter = Integer.parseInt(temp.substring(1));
                
                if (ID_without_letter >= largest_ID) {
                    largest_ID = ID_without_letter;
                }
            }
        }

        String formattedNumber = String.format("%03d", largest_ID+1);
        String nextID = role_letter + String.valueOf(formattedNumber);

        return nextID;
    }

    // private boolean doctorHandling(String hospitalID, String name, String gender, String age, boolean addOrRemove) {
    //     // true means adding new doctor
    //     boolean removed = false;
    //     if (addOrRemove == true) {
    //         Doctor newDoc = new Doctor(hospitalID, name, gender, age);
    //         List<Doctor> temp = new ArrayList<>();
    //         temp.add(newDoc);
    //         AppointmentManager.makeDailyAppointments(temp); 
    //         return true;
    //     } else {
    //         //Remove doc appointments from CSV
    //         System.out.println("");

    //         // If still have pending appointments for the doctor, stop removal, return false
    //         return removed;
    //     }

    // }

    // private String getNextID(List<List<String>> doubleList, String role) {
    //     int largest_ID  = 0;
    //     String role_letter = "";

    //     if (role.equalsIgnoreCase("Doctor")) {
    //         role_letter = "D";

    //     } else if (role.equalsIgnoreCase("Pharmacist")) {
    //         role_letter = "P";

    //     } else if (role.equalsIgnoreCase("Administrator")) {
    //         role_letter = "A";
            
    //     } else {
    //         System.out.println("Unknown Role");
    //         role_letter = "?";
    //     }


    //     for (List<String> singlList : doubleList) {
    //         if (singlList.get(0).substring(0,1).equals(role_letter)) {
    //             String temp = singlList.get(0);

    //             //Remove the first letter from the ID, to get the largeset index
    //             int ID_without_letter = Integer.parseInt(temp.substring(1));
                
    //             if (ID_without_letter >= largest_ID) {
    //                 largest_ID = ID_without_letter;
    //             }
    //         }
    //     }

    //     String formattedNumber = String.format("%03d", largest_ID+1);
    //     String nextID = role_letter + String.valueOf(formattedNumber);

    //     return nextID;
    // }

    // might need to add error checking for duplicates and other kinds of inputs
	public void manageInventory() { 
        Inventory inventory = new Inventory(medicineListCSV);
    
        System.out.println("=== Course of Action: ===");
        System.out.println("1 - View Inventory");
        System.out.println("2 - Add to Current Stock");
        System.out.println("3 - Add new Stock");
        System.out.println("4 - Delete Stock");
        System.out.println("5 - Update Stock");
        System.out.println("6 - Update Stock Level Alert");
        int inventory_choice = input_scanner.nextInt();
        input_scanner.nextLine();
    
        switch (inventory_choice) {
            case 1:
                System.out.println("=== Inventory ===");
                inventory.viewInventory();
                break;
    
            case 2:
                System.out.println("=== Adding Stock ===");
                System.out.println("Medication Name: ");
                String medication_choice = input_scanner.nextLine();
                System.out.println("Amount to add: ");
                int amount = input_scanner.nextInt();
                input_scanner.nextLine();
    
                inventory.updateMedication(medication_choice, amount, true);
                break;

            case 3: // might need to add error checking for duplicates and other kinds of inputs
                System.out.println("=== Adding New Stock ===");
                System.out.println("Medication Name: ");
                String new_med_choice = input_scanner.nextLine();
                System.out.println("Stock: ");
                int new_med_amount = input_scanner.nextInt();
                input_scanner.nextLine();
                System.out.println("Low Stock Value: ");
                int alertValue = input_scanner.nextInt();
                input_scanner.nextLine();

                inventory.addNewMedication(new_med_choice, new_med_amount, alertValue);

                Medication med = inventory.getMedication(new_med_choice);
                if(med == null) {break;}

                System.out.println("Added new stock: " + med.getMedicationName() + " with stock: " + med.getStock());

                break;
    
            case 4:
                System.out.println("=== Deleting Stock ===");
                System.out.println("Medication Name: ");
                String deleted_choice = input_scanner.nextLine();
    
                Medication deleted_med = inventory.getMedication(deleted_choice);
                if(deleted_med == null) {break;}

                inventory.removeMedication(deleted_choice);
                System.out.println("Deleted stock: " + deleted_med.getMedicationName());
                break;
    
            case 5:
                System.out.println("=== Updating Stock ===");
                System.out.println("Medication Name: ");
                String updated_choice = input_scanner.nextLine();
                System.out.println("Updated Amount: ");
                int updated_amount = input_scanner.nextInt();
                input_scanner.nextLine();
                boolean add_or_remove = false;
                int current_stock = 0;

                Medication medication = inventory.getMedication(updated_choice);
                if(medication == null) {break;}

                current_stock = medication.getStock();
                
                if (updated_amount > current_stock) {
                    updated_amount -= updated_amount;
                    add_or_remove = true;
                } else if (updated_amount < current_stock) {
                    current_stock -= updated_amount;
                    updated_amount = current_stock;
                    add_or_remove = false;
                }

                if(!inventory.updateMedication(updated_choice, updated_amount, add_or_remove)) {
                    System.out.println("Failed to update medication stock .");
                }
                break;
    
            case 6:
                System.out.println("=== Updating Stock Alert Level ===");
                System.out.println("Medication Name: ");
                String med_level_choice = input_scanner.nextLine();
                System.out.println("New Alert Level: ");
                int new_limit = input_scanner.nextInt();
                input_scanner.nextLine();

                Medication updated_med = inventory.getMedication(med_level_choice);
                if(updated_med == null) {break;}

                updated_med.updateLowStockLevel(new_limit);
                System.out.println("Updated: " + updated_med.getMedicationName() + " with limit: " + updated_med.getLowStockValue());
                inventory.writeCSVFile();
                break;
    
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
    
	public void approveReplenishmentRequest() {
        //Show all pending requests
        List<String> lines = readCSVFile(replenishRecordsCSV);

        // Split each line into fields (excluding the header)
        System.out.println("=== Pending Replishment Requests ===");
        System.out.println("Select by index, press any 0 to exit");

        for (int i = 1; i < lines.size(); i++) { // Start from index 1 to skip header
            String[] fields = lines.get(i).split(";");

            // If request is already approved, skip entry
            if(fields[3].equals("Approved")) {
                System.out.println("Skipping Approved Req");
                continue;
            }

            System.out.println("Request ID: " + fields[0] + " Medicine Name: " + fields[1] + " Add Amount: " + fields[2]);
        }

        //Select to approve by index
        int index_to_approve = input_scanner.nextInt();
        input_scanner.nextLine();

        for (int i = 0; i<lines.size(); i++) { // Start from index 1 to skip header
            String[] fields = lines.get(i).split(";");

            if (fields[0].equals(String.valueOf(index_to_approve))) {

                // Get list of medicine already available, run through list to see if requested med already in inventory
                Inventory inventory = new Inventory("Medicine_List.csv");
                List<Medication> medications = inventory.getInventory();
                boolean medInInventory = false;
                for(Medication medication : medications) {

                    // If not new med, update stock amount
                    if (fields[1].equals(medication.getMedicationName())) {

                        //update stock value and csv file
                        inventory.updateMedication(fields[1], Integer.parseInt(fields[2]), true);
                        medInInventory = true;
                        fields[3] = "Approved"; 
                    }   
                }
                
                // If new med add to csv
                if (medInInventory == false) {
                    System.out.println("Enter Low Stock Value: ");
                    int alertValue = input_scanner.nextInt();
                    input_scanner.nextLine();

                    inventory.addNewMedication(fields[1], Integer.parseInt(fields[2]), alertValue);
                    fields[3] = "Approved"; 
                }

                lines.set(i, String.join(";", fields[0], fields[1], fields[2], fields[3]));
                break;
            }
        }

        Administrator.writeCSVFile(lines, replenishRecordsCSV);
	}

}


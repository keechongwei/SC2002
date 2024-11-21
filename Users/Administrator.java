package Users;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import managers.AppointmentManager;
import managers.InventoryManager;
import managers.StaffManager;
import managers.csvhandlers.InventoryCSVHandler;
import managers.csvhandlers.StaffCSVHandler;
import utility.InputValidator;
import utility.Medication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * An Administrator class, a subclass of Staff
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class Administrator extends Staff {

    enum FilterType{Name,Role,Gender,Age}
    enum csvField{}

     /**
     * Full list of all staff 
     */
    static List<List<String>> staffs = new ArrayList<>();

    /**
     * Fileanme of staff csv to be read
     * Constants defined by index of details in csv row
     */
    static String staffRecordsCSV = "Staff_List_Copy.csv";
    public static final int STAFFID = 0, PASSWORD = 1, NAME = 2, ROLE = 3, GENDER = 4, AGE = 5;

    /**
     * Fileanme of medication inventory csv to be read 
     * Constants defined by index of details in csv row
     */
    static String medicineListCSV = "Medicine_List.csv";

    /**
     * Fileanme of repplenish requet csv to be read 
     * Constants defined by index of details in csv row
     */
    static String replenishRecordsCSV = "Replenish_Request_List.csv";
    public static final int REQID = 0, MEDICATIONNAME = 1, ADDEDAMT = 2, STATUS = 3;

    /**
     * Scanner object to read inputs
     */
    Scanner input_scanner = new Scanner(System.in);

     /**
     * Constructor for Administrator Object
     * @param HospitalID Unique HospitalID
     * @param name Admin's Name
     * @param gender Admin's Gender
     * @param age Admin's Age
     */
     public Administrator(String HospitalID, String name, String gender, String age) {
        super(HospitalID, "password");
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    /**
     * Function to print Menu for Administrator
     * @param void
     * @return void
     */
    public void printMenu(){
        int choice = 0;
        while(choice != 5){
            System.out.println("=== ADMINISTRATOR MENU, ENTER CHOICE ===");
            System.out.println("(1) View and Manage Hospital Staff");
            System.out.println("(2) View Appointments Details");
            System.out.println("(3) View and Manage Medication Inventory");
            System.out.println("(4) Approve Replenishment Requests");
            System.out.println("(5) Logout");
            choice = InputValidator.getIntegerInput("Choice: ", 1, 5);

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

    /**
     * Function to convert information of DocAdministratortor into a string to be added to staff CSV
     * @param void
     * @return String row to be written into Staff CSV
     */
    public String toCSV() {
        // Combine all attributes into a CSV string
        return super.getHospitalID() + ";" + super.getPassword() + ";" + name + ";" + "Administrator" + ";" + gender + ";" + age;
    }

    /**
     * Function to add all staff from Staff.csv into a static staffs list
     * @param void
     * @return void
     */
    private static void initialise_staff_details(){
        boolean headerline = true;
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
    }

    /**
     * Overloaded function for filtering staff by age in ascending order
     * @param filter_type type of filter must be Age
     * @return filteredStaffs filtered list of Staff
     */
	public static List<List<String>> filterStaff(FilterType filterType) {        
        if (filterType != FilterType.Age) {System.out.println("Please enter a valid filter.");}

        List<List<String>> filteredStaffs = new ArrayList<List<String>>(staffs);

        Collections.sort(filteredStaffs, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> staff1, List<String> staff2) {
                int age1 = Integer.parseInt(staff1.get(AGE)); // Assuming age is at index 4
                int age2 = Integer.parseInt(staff2.get(AGE));
                return Integer.compare(age1, age2);
            }
        });

        return filteredStaffs;
	}

    /**
     * Overloaded function for filtering staff by specific age
     * @param filter_type type of filter must be Age
     * @param age specific age to look for
     * @return filteredStaffs filtered list of Staff
     */
	public static List<List<String>> filterStaff(FilterType filterType, int age) {
        
        if (filterType != FilterType.Age) {System.out.println("Please enter a valid filter.");}

        List<List<String>> filteredStaffs = new ArrayList<>();

        for(int i = 0; i<staffs.size();i++){
            int temp = Integer.parseInt(staffs.get(i).get(AGE));
            
            if (temp == age){
                filteredStaffs.add(staffs.get(i));
            }
        }

        return filteredStaffs;
	}

    /**
     * Overloaded function for filtering staff by name, role or gender
     * @param filter_type type of filter must be Name, Role or Gender
     * @param name_role_gender specific name, role or gender to look for
     * @return filteredStaffs filtered list of Staff
     */
    public static List<List<String>> filterStaff(FilterType filterType, String name_role_gender) {

        if (filterType == FilterType.Age) {System.out.println("Please enter a valid filter.");}

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
            case Name: index = NAME; break;
            case Role: index = ROLE; break;
            case Gender: index = GENDER; break;
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

     /**
     * Function to print a nested list of Strings
     * @param doubList
     * @return void
     */
    public static void printDoubleList(List<List<String>> doubList) { 
        for (List<String> singleList : doubList) {
            for (String string : singleList) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }

    /**
     * Function to manage all staffs
     * Allow administratotr or choose between searching for staff w/o filters, removing staff, updating staff details or adding new staff
     * Searching for staff w/o filters using appropriate filter function to get a filtered list, before using printDoubeleList to print
     * Removing staff uses removeStaff function (private)
     * Udpdating staff details uses updateStaff from StaffManager
     * Adding staff uses addStaff function (private)
     * Removing and Adding are (private) to Administrator class for security and encapsulation purposes
     * Changes are written to static list of staffs before being written to CSV
     * @param  void
     * @return void
     * @see StaffManager
     */
	public void manageStaff() {
        // Reinitalise static staff list to reflect any changes to Staff_List.csv
        staffs.clear(); initialise_staff_details();

        // Search by ID
        System.out.println("=== Staff Manager ===");
        System.out.println("Course of Action: ");
        System.out.println("1 - Search/ View Staff");
        System.out.println("2 - Remove Staff");
        System.out.println("3 - Update Staff");
        System.out.println("4 - Add Staff");
        System.out.println();

        int input = InputValidator.getIntegerInput("Choice: ", 1, 4);

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
                System.out.println();

                int manageStaff_choice = InputValidator.getIntegerInput("Choice: ", 1 , 6);                

                // Filtered List of Staffs
                List<List<String>> filteredStaffs = new ArrayList<>();

                switch (manageStaff_choice) {
                    case 1:
                        String searchByname = InputValidator.getName("Name: ");
                        filteredStaffs = Administrator.filterStaff(FilterType.Name, searchByname);
                        break;

                    case 2:
                        String role = InputValidator.getRole("Role: ");
                        filteredStaffs = Administrator.filterStaff(FilterType.Role, role);
                        break;

                    case 3:
                        int searchByAge = InputValidator.getIntegerInput("Age: ", 1, 100);    
                        filteredStaffs = Administrator.filterStaff(FilterType.Age, searchByAge);
                        break;

                    case 4:
                        filteredStaffs = Administrator.filterStaff(FilterType.Age);
                        break;

                    case 5:
                        String gender_choice = InputValidator.getGender("Gender: ");
                        filteredStaffs = Administrator.filterStaff(FilterType.Gender, gender_choice);
                        break;    
        
                    case 6:
                        printDoubleList(filteredStaffs);
                        filteredStaffs = Administrator.filterStaff(FilterType.Age);
                        break;

                    default:
                        System.out.println("Invalid Option.");
                        return;
                }

                if (filteredStaffs.size() == 0) {
                    System.out.println("None found.");
                } else {
                    System.out.println(" Found: " + filteredStaffs.size());
                    Administrator.printDoubleList(filteredStaffs);
                    System.out.println();
                    filteredStaffs.clear();
                }
                break;
            case 2: removeStaff(); break;
            case 3: StaffManager.updateStaff(); break;
            case 4: addStaff(); break;
            default: System.out.println("1 to 3 you dummy, gtfo"); return;
        }
	}
    
    /**
     * Function to add new staff
     * Queries for new Staff details
     * Creates new Doctor/Pharmacist/Administrator Object and adds it to static list staffs and respective static role list in StaffManager
     * Writes changes to Staff_List.csv
     * @param  void
     * @return void
     * @see StaffManager
     */
    private void addStaff() {
        System.out.println("Enter new staff details: ");
        String name = InputValidator.getName("Name: ");
        String role = InputValidator.getRole("Role: ");
        String gender = InputValidator.getGender("Gender: ");
        String age = String.valueOf(InputValidator.getIntegerInput("Age: ", 0, 100));
        String password = InputValidator.getPassword("Password: ");

        List<List<String>> filteredStaffs = new ArrayList<>();
        filteredStaffs = filterStaff(FilterType.Role, role);

        String hospitalID = getNextID(filteredStaffs, role);

        List<String> lines = readCSVFile(staffRecordsCSV);
        lines.add(String.join(";", hospitalID, password, name, role, gender, age));

        writeCSVFile(lines, staffRecordsCSV);

        Staff s = null;

        if (role.equals("Pharmacist")) {
            s = new Pharmacist(hospitalID, password, gender, age);
            StaffManager.pharmacists.add((Pharmacist)s);
        } else if (role.equals("Doctor")) {
            s = new Doctor(hospitalID, name, gender, age);
            boolean check = StaffManager.doctorHandling((Doctor)s, true);
            if (check != true) {
                System.out.println("Unable to add Doctor");
                return;
            } 
            StaffManager.doctors.add((Doctor)s);
        } else {
            s = new Administrator(hospitalID, password, gender, age);
            StaffManager.administrators.add((Administrator)s);
        }

        System.out.println("New staff added successfully.");
        System.out.println();
    }

    /**
     * Function to remove staff
     * Queries for new unqiue Hospital ID and retrieve specific Staff Object using StaffManager
     * Removes staff from Staff_List.csv, removes object from static staff list in StaffManager
     * If removing a doctor handles changes to appointments using StaffManager
     * @param  void
     * @return void
     * @see StaffManager
     */
    private void removeStaff() {
        String hospitalID = InputValidator.getNonEmptyString("Enter HospitalID: ");
        System.out.println("");

        List<String> lines = readCSVFile(staffRecordsCSV);

        boolean removed = lines.removeIf(line -> line.startsWith(hospitalID + ";"));

        if (removed) {
            if (hospitalID.substring(0,1).equalsIgnoreCase("D")) {
                StaffManager.doctorHandling((Doctor)StaffManager.getStaffByID(hospitalID), false);
            }
            
            writeCSVFile(lines, staffRecordsCSV);
            StaffManager.pharmacists.clear();
            StaffManager.doctors.clear();
            StaffManager.administrators.clear();
            StaffCSVHandler.loadCSV();

            System.out.println("Staff with HospitalID " + hospitalID + " removed successfully.");
        } else {
            System.out.println("No staff found with HospitalID " + hospitalID + ".");
        }

        System.out.println();
    }

    /**
     * Function to read Staff_List.csv
     * @param filename
     * @return lines
     */
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

    /**
     * Function to wrtie to Staff_List.csv
     * @param lines
     * @param CSVFile
     * @return lines
     */
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

    /**
     * Runs through list to find largest current ID, return next ID
     * @param doubleList
     * @param role
     * @return nextID Next largest ID
     */
    private static String agetNextID(List<List<String>> doubleList, String role) {
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
            if (singlList.get(0).substring(0,1).equalsIgnoreCase(role_letter)) {
                String temp = singlList.get(0);

                //Remove the first letter from the ID, to get the largeset index
                int ID_without_letter = Integer.parseInt(temp.substring(1));
                
                if (ID_without_letter >= largest_ID) {
                    largest_ID = ID_without_letter;
                }
            }
        }

        // To format the ID to reflect A001 format, where A is specific to role
        String formattedNumber = String.format("%03d", largest_ID+1);
        String nextID = role_letter + String.valueOf(formattedNumber);

        return nextID;
    }

    /**
     * Function to manage inventory
     * Allow administratotr or choose between veiwing inventory, adding new or current stock, updating or deleting stock, and udapting stock's low alert value
     * View inventory prints entire inventory
     * Add to current stock by getting and setting specific medication Object by Name through InventoryManager
     * Add new stock uses InventoryManager to create and update new Medication Object
     * Removing stock uses InventoryManager to create and delete new Medication Object
     * Update Stock allows stock level to be directly udapted through InventoryManager
     * Update Stock Alert allows low stock level to be directly udapted through InventoryManager
     * @param  void
     * @return void
     * @see InventorManager
     */
	public void manageInventory() { 
        InventoryManager inventory = new InventoryManager(medicineListCSV);
    
        System.out.println("=== Course of Action: ===");
        System.out.println("1 - View Inventory");
        System.out.println("2 - Add to Current Stock");
        System.out.println("3 - Add new Stock");
        System.out.println("4 - Delete Stock");
        System.out.println("5 - Update Stock");
        System.out.println("6 - Update Stock Level Alert");

        int inventory_choice = InputValidator.getIntegerInput("Choice: ", 1, 6);
    
        switch (inventory_choice) {
            case 1:
                System.out.println("=== Inventory ===");
                inventory.viewInventory();
                break;
    
            case 2:
                System.out.println("=== Adding Stock ===");
                String medication_choice = InputValidator.getName("Medication Name: ");
                int amount = InputValidator.getIntegerInput("Amount to add: ", 1, 10000);
                System.out.println("");
    
                inventory.updateMedication(medication_choice, amount, true);
                break;

            case 3:
                System.out.println("=== Adding New Stock ===");
                String new_med_choice = InputValidator.getName("Medication Name: ");
                int new_med_amount = InputValidator.getIntegerInput("Stock: ", 1, 10000);
                int alertValue = InputValidator.getIntegerInput("Low Stock Value: ", 1, 10000);
                inventory.addNewMedication(new_med_choice, new_med_amount, alertValue);

                Medication med = inventory.getMedication(new_med_choice);
                if(med == null) {break;}

                System.out.println("Added new stock: " + med.getMedicationName() + " with stock: " + med.getStock());
                break;
    
            case 4:
                System.out.println("=== Deleting Stock ===");
                String deleted_choice = InputValidator.getName("Medication Name: ");

                Medication deleted_med = inventory.getMedication(deleted_choice);
                if(deleted_med == null) {break;}

                inventory.removeMedication(deleted_choice);
                System.out.println("Deleted stock: " + deleted_med.getMedicationName());
                break;
    
            case 5:
                System.out.println("=== Updating Stock ===");
                String updated_choice = InputValidator.getName("Medication Name: ");
                int updated_amount = InputValidator.getIntegerInput("Updated Amount: ", 1, 10000);

                boolean add_or_remove = false;
                int current_stock = 0;

                Medication medication = inventory.getMedication(updated_choice);
                if(medication == null) {break;}

                current_stock = medication.getStock();
                System.out.print("Previous stock amount: " + current_stock + " ");
                
                if (updated_amount > current_stock) {
                    updated_amount -= current_stock;
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
                String med_level_choice = InputValidator.getName("Medication Name: ");
                int new_limit = InputValidator.getIntegerInput("New Alert Level: ", 1, 10000);

                Medication updated_med = inventory.getMedication(med_level_choice);
                if(updated_med == null) {break;}

                updated_med.updateLowStockLevel(new_limit);
                System.out.println("Updated: " + updated_med.getMedicationName() + " with limit: " + updated_med.getLowStockValue());
                InventoryCSVHandler.writeCSV();
                break;
    
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
     
    /**
     * Function to manage Replenishment Requests
     * Allow administratotr to veiw all Replenishment Requests and approve 
     * Updates stock or create new medication via Inventory Manager
     * Write changes by to Replenih Reques csv 
     * @param  void
     * @return void
     * @see InventorManager
     */
	public void approveReplenishmentRequest() {
        //Show all pending requests
        List<String> lines = readCSVFile(replenishRecordsCSV);
        int idmax = 0;
        // Split each line into fields (excluding the header)
        System.out.println("=== Pending Replishment Requests ===");

        for (int i = 1; i < lines.size(); i++) { // Start from index 1 to skip header
            String[] fields = lines.get(i).split(";");
            idmax++;

            // If request is already approved, skip entry
            if(fields[STATUS].equals("Approved")) {
                // System.out.println("Skipping Approved Req");
                continue;
            }

            System.out.println("Request ID: " + fields[REQID] + " Medicine Name: " + fields[MEDICATIONNAME] + " Add Amount: " + fields[ADDEDAMT]);
        }

        //Select to approve by index
        System.out.println("Select by index, press 0 to exit: ");
        int index_to_approve = 0;
        index_to_approve = InputValidator.getIntegerInput("Choice, enter 0 to exit: ", index_to_approve, idmax);
        if(index_to_approve == 0) {return;}

        for (int i = 0; i<lines.size(); i++) { // Start from index 1 to skip header
            String[] fields = lines.get(i).split(";");

            if (fields[REQID].equals(String.valueOf(index_to_approve))) {
                if (fields[STATUS].equals("Approved")) {
                    System.out.println("Invalid Choice, request already approved");
                    return;
                }

                // Get list of medicine already available, run through list to see if requested med already in inventory
                InventoryManager inventory = new InventoryManager("Medicine_List.csv");
                List<Medication> medications = inventory.getInventory();
                boolean medInInventory = false;
                for(Medication medication : medications) {

                    // If not new med, update stock amount
                    if (fields[MEDICATIONNAME].equalsIgnoreCase(medication.getMedicationName())) {

                        //update stock value and csv file
                        inventory.updateMedication(fields[MEDICATIONNAME], Integer.parseInt(fields[ADDEDAMT]), true);
                        medInInventory = true;
                        fields[STATUS] = "Approved"; 
                    }   
                }
                
                // If new med add to csv
                if (medInInventory == false) {
                    int alertValue = InputValidator.getIntegerInput("Enter Low Stock Value: ", 1, 10000);
                    System.out.println("");

                    inventory.addNewMedication(fields[MEDICATIONNAME], Integer.parseInt(fields[ADDEDAMT]), alertValue);
                    fields[STATUS] = "Approved"; 
                }

                lines.set(i, String.join(";", fields[REQID], fields[MEDICATIONNAME], fields[ADDEDAMT], fields[STATUS]));
                break;
            }
        }

        Administrator.writeCSVFile(lines, replenishRecordsCSV);
	}

}

import java.util.ArrayList;
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

public class Administrator extends User {
    enum Filter_type{Name,Role,Gender,Age}
    static List<List<String>> staffs = new ArrayList<>();
    //static File staffRecordsFile = new File("Staff_List.csv");
    static String staffRecordsCSV = "Staff_List.csv";
    static String replenishRecordsCSV = "Replenish_Request_List.csv";

    Scanner input_scanner = new Scanner(System.in);

	private Object _attribute;

     // Constructor for Administrator class
     public Administrator(String HospitalID, String password) {
        super(HospitalID, password);
    }

    public static void main(String[] args) {
        // Create an Administrator instance
        Administrator admin = new Administrator("Hospital123", "Password123");
    
        // Initialize staff details
        initialise_staff_details();
        //admin.printDoubleList(staffs);

        // Filter staff 
        List<List<String>> filteredStaffs = admin.filterStaff(Filter_type.Name, "John");
    
        // Print the filtered staff list
        System.out.println("Filtered Staff List:");
        admin.printDoubleList(filteredStaffs);

        //admin.manageStaff();

        //admin.manageInventory();

        //admin.approveReplenishmentRequest();

        Administrator.viewAllAppointments();
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
            case 3: updateStaff(); break;
            case 4: addStaff(); break;
            default: System.out.println("1 to 3 you dummy, gtfo"); break;
        }

		//throw new UnsupportedOperationException();
	}


    private void removeStaff() {
        System.out.println("Enter HospitalID: ");
        String hospitalID = input_scanner.nextLine();

        List<String> lines = readCSVFile(staffRecordsCSV);
        boolean removed = lines.removeIf(line -> line.startsWith(hospitalID + ";"));

        if (removed) {
            writeCSVFile(lines);
            System.out.println("Staff with HospitalID " + hospitalID + " removed successfully.");
        } else {
            System.out.println("No staff found with HospitalID " + hospitalID + ".");
        }
    }

    private void updateStaff() {
        System.out.println("Enter HospitalID: ");
        String hospitalID = input_scanner.nextLine();
    
        List<String> lines = readCSVFile(staffRecordsCSV);
    
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(hospitalID + ";")) {
    
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
                                break;
                            case 2:
                                role = "Pharmacist";
                                break;
                            case 3:
                                role = "Administrator";
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
    
                // Replace the old line with the updated details
                lines.set(i, String.join(";", hospitalID, name, role, gender, age));
                break;
            }
        } 
        writeCSVFile(lines);

    }
    

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

        String hospitalID = getNextID(filteredStaffs);

        List<String> lines = readCSVFile(staffRecordsCSV);
        lines.add(String.join(";", hospitalID, name, role, gender, age));

        writeCSVFile(lines);
        System.out.println("New staff added successfully.");
    }

    private String getNextID(List<List<String>> doubleList) {
        int largest_ID  = 0;

        for (List<String> singlList : doubleList) {
            String temp = singlList.get(0);

            //Remove the first letter from the ID, to get the largeset index
            int ID_without_letter = Integer.parseInt(temp.substring(1));
            
            if (ID_without_letter >= largest_ID) {
                largest_ID = ID_without_letter;
            }
        }

        // Assumes that all in doubleList have the same role, i.e. same first letter in ID
        String role_letter = doubleList.get(0).get(0).substring(0,1);
        String formattedNumber = String.format("%03d", largest_ID+1);
        String nextID = role_letter + String.valueOf(formattedNumber);

        return nextID;
    }

    private static List<String> readCSVFile(String filename) {
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

    private static void writeCSVFile(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Staff_List.csv"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }

    // might need to add error checking for duplicates and other kinds of inputs
	public void manageInventory() { 
        Inventory inventory = new Inventory("Medicine_List.csv");
    
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
    
                inventory.getMedication(medication_choice).ifPresentOrElse(
                    med -> {
                        med.addStock(amount);
                        System.out.println("Updated: " + med.getMedicationName() + " with stock: " + med.getStock());
                    },
                    () -> System.out.println("Medication " + medication_choice + " not found.")
                );
                inventory.writeCSVFile();
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

                inventory.getMedication(new_med_choice).ifPresentOrElse(
                    med -> {
                        System.out.println("Added new stock: " + med.getMedicationName() + " with stock: " + med.getStock());
                    },
                    () -> System.out.println("Medication " + new_med_choice + " not found.")
                );

                break;
    
            case 4:
                System.out.println("=== Deleting Stock ===");
                System.out.println("Medication Name: ");
                String deleted_choice = input_scanner.nextLine();
    
                inventory.getMedication(deleted_choice).ifPresentOrElse(
                    med -> {
                        inventory.removeMedication(deleted_choice);
                        inventory.viewInventory();
                        System.out.println("Deleted stock: " + med.getMedicationName());
                    },
                    () -> System.out.println("Medication " + deleted_choice + " not found.")
                );
                break;
    
            case 5:
                System.out.println("=== Updating Stock ===");
                System.out.println("Medication Name: ");
                String updated_choice = input_scanner.nextLine();
                System.out.println("Updated Amount: ");
                int updated_amount = input_scanner.nextInt();
                input_scanner.nextLine();
    
                inventory.getMedication(updated_choice).ifPresentOrElse(
                    med -> {
                        int current_stock = med.getStock();
                        if (updated_amount > current_stock) {
                            med.addStock(updated_amount - current_stock);
                        } else if (updated_amount < current_stock) {
                            med.removeStock(current_stock - updated_amount);
                        }
                        System.out.println("Updated: " + med.getMedicationName() + " with stock: " + med.getStock());
                    },
                    () -> System.out.println("Medication " + updated_choice + " not found.")
                );
                inventory.writeCSVFile();
                break;
    
            case 6:
                System.out.println("=== Updating Stock Alert Level ===");
                System.out.println("Medication Name: ");
                String med_level_choice = input_scanner.nextLine();
                System.out.println("New Alert Level: ");
                int new_limit = input_scanner.nextInt();
                input_scanner.nextLine();
                inventory.getMedication(med_level_choice).ifPresentOrElse(
                    med -> {
                        med.updateLowStockLevel(new_limit);
                        System.out.println("Updated: " + med.getMedicationName() + " with limit: " + med.getLowStockValue());
                    },
                    () -> System.out.println("Medication " + med_level_choice + " not found.")
                ); 
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
        for (int i = 1; i < lines.size(); i++) { // Start from index 1 to skip header
            String[] fields = lines.get(i).split(";");
            System.out.println("Request ID: " + fields[0] + " Medicine Name: " + fields[1] + " Add Amount: " + fields[2]);
        }

        //Select to approve by index
        int index_to_approve = input_scanner.nextInt();
        input_scanner.nextLine();
        List<String> temp = new ArrayList<>();

        for (String line : lines) { // Start from index 1 to skip header
            String[] fields = line.split(";");
            if (fields[0].equals(String.valueOf(index_to_approve))) {

                // Get list of medicine already available, run through list to see if requested med in already in inventory
                Inventory inventory = new Inventory("Medicine_List.csv");
                List<Medication> medications = inventory.getInventory();
                boolean medInInventory = false;
                for(Medication medication : medications) {

                    // If not new med, update stock amount
                    if (fields[1].equals(medication.getMedicationName())) {

                        //update stock value and csv fike
                        medication.addStock(Integer.parseInt(fields[2]));
                        inventory.writeCSVFile();
                        System.out.println("Updated: " + medication.getMedicationName() + " with limit: " + medication.getLowStockValue());
                        medInInventory = true;
                        break;
                    }   
                }
                
                // If new med add to csv
                if (medInInventory == false) {
                    System.out.println("Enter Low Stock Value: ");
                    int alertValue = input_scanner.nextInt();
                    input_scanner.nextLine();

                    inventory.addNewMedication(fields[1], Integer.parseInt(fields[2]), alertValue);
                }
                
                continue;
            } 

            temp.add(line);
        }
	}

}


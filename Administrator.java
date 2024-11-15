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
    static File staffRecordsFile = new File("Staff_List.csv");

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

        admin.manageInventory();
    }

    private static void initialise_staff_details(){
        boolean headerline = true;

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
    // For filtering staff by age
	public List<List<String>>  filterStaff(Filter_type filter_type) {        
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
	public List<List<String>> filterStaff(Filter_type filter_type, int age) {
        
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
    public List<List<String>> filterStaff(Filter_type filterType, String name_role_gender) {

        System.out.println(filterType);
        List<List<String>> filteredStaffs = new ArrayList<>();
        int index = -1;

        switch (filterType){
            case Name: index = 1; break;
            case Role: index = 2; break;
            case Gender: index = 3; break;
            default: index = -1; break;
        };
        System.out.println(index);

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

    public void printDoubleList(List<List<String>> doubList) { 
        for (List<String> singleList : doubList) {
            for (String string : singleList) {
                System.out.print(string + " ");
            }
            System.out.println();
        }
    }

	public void manageStaff() {

        // Search by ID
        System.out.println("Enter choice: ");
        System.out.println("1 - Remove Staff");
        System.out.println("2 - Update Staff");
        System.out.println("3 - Add Staff");

        int input = input_scanner.nextInt();
        input_scanner.nextLine(); // Clear newline

        switch (input) {
            case 1: removeStaff(); break;
            case 2: updateStaff(); break;
            case 3: addStaff(); break;
            default: System.out.println("1 to 3 you dummy, gtfo"); break;
        }

		//throw new UnsupportedOperationException();
	}


    private void removeStaff() {
        System.out.println("Enter HospitalID: ");
        String hospitalID = input_scanner.nextLine();

        List<String> lines = readCSVFile();
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
    
        List<String> lines = readCSVFile();
        boolean found = false;
    
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(hospitalID + ";")) {
                found = true;
    
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

        List<String> lines = readCSVFile();
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

    private List<String> readCSVFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Staff_List.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
        }
        return lines;
    }

    private void writeCSVFile(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("Staff_List.csv"))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to the CSV file: " + e.getMessage());
        }
    }

	public void viewScheduledAppointment() {
		throw new UnsupportedOperationException();
	}

	public void manageInventory() {
        Inventory inventory = new Inventory("Medicine_List.csv");
    
        System.out.println("Course of Action: ");
        System.out.println("1 - View Inventory");
        System.out.println("2 - Add Stock");
        System.out.println("3 - Delete Stock");
        System.out.println("4 - Update Stock");
        System.out.println("5 - Update Stock Level Alert");
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
    
            case 3:
                System.out.println("=== Deleting Stock ===");
                System.out.println("Medication Name: ");
                String deleted_choice = input_scanner.nextLine();
    
                inventory.getMedication(deleted_choice).ifPresentOrElse(
                    med -> {
                        int total_stock_amount = med.getStock();
                        med.removeStock(total_stock_amount);
                        System.out.println("Deleted stock for: " + med.getMedicationName());
                    },
                    () -> System.out.println("Medication " + deleted_choice + " not found.")
                );
                inventory.writeCSVFile();
                break;
    
            case 4:
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
    
            case 5:
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

		//throw new UnsupportedOperationException();
	}
}


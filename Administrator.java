import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Administrator extends User {
    enum Filter_type{Name,Role,Gender,Age}
    static List<List<String>> staffs = new ArrayList<>();
    static List<List<String>> filteredStaffs = new ArrayList<>();
    static File staffRecordsFile = new File("Staff_List.csv");

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
    
        // Filter staff by role "Doctor"
        admin.filterStaff(Filter_type.Name, "John");

        if (filteredStaffs.size() == 0) {
            System.out.println("None found");
        }
    
        // Print the filtered staff list
        System.out.println("Filtered Staff List:");
        admin.printDoubleList(filteredStaffs);
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
	public void filterStaff(Filter_type filter_type) {
        
        if (filter_type != Filter_type.Age) {System.out.println("Please enter a valid integer value for age");}

        filteredStaffs = new ArrayList<List<String>>(staffs);
        Collections.sort(filteredStaffs, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> staff1, List<String> staff2) {
                int age1 = Integer.parseInt(staff1.get(4)); // Assuming age is at index 4
                int age2 = Integer.parseInt(staff2.get(4));
                return Integer.compare(age1, age2);
            }
        });

		//throw new UnsupportedOperationException();
	}

    // For sorting staff by age
	public void filterStaff(Filter_type filter_type, int age) {
        
        if (filter_type != Filter_type.Age) {System.out.println("Please enter a valid integer value for age");}

        for(int i = 0; i<staffs.size();i++){
            int temp = Integer.parseInt(staffs.get(i).get(4));
            
            if (temp == age){
                filteredStaffs.add(staffs.get(i));
            }
        }

		//throw new UnsupportedOperationException();
	}

    // For filtering staff by name role and gender
    public void filterStaff(Filter_type filterType, String name_role_gender) {

        filteredStaffs.clear(); // Clear previous filter results
        System.out.println(filterType);
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
            return;
        }

        for (List<String> staff : staffs) {
            if (staff.get(index).contains(name_role_gender)) {
                filteredStaffs.add(staff);
            }
        }

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
		throw new UnsupportedOperationException();
	}

	public void viewScheduledAppointment() {
		throw new UnsupportedOperationException();
	}

	public void manageInventory() {
		throw new UnsupportedOperationException();
	}

	public void approveReplenishmentRequest() {
		throw new UnsupportedOperationException();
	}
}


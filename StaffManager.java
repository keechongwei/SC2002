import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A Manager object meant to handle any update to staff details
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class StaffManager implements Manager {
    /**
     * List of Pharmacists
     */
    static List<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
    /**
     * List of Administrators
     */
    static List<Administrator> administrators = new ArrayList<Administrator>();
    /**
     * List of Doctors
     */
    static List<Doctor> doctors = new ArrayList<Doctor>();
    /**
     * Scanner object to read inputs
     */
    static Scanner input_scanner = new Scanner(System.in);

    /**
     * Check if Staff_List.csv exists
     * If yes, uses StaffCSVHandler to load Staff details into separate static lists
     * @param void
     * @return void
     * @see StaffCSVHandler
     */
    public static void initialise() {
        if (!((StaffCSVHandler.csvFile).exists()) || (StaffCSVHandler.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("Staff_List.csv is empty or missing.");
        } else {
            System.out.println("Loading Staff from Staff_List.csv...");
            StaffCSVHandler.loadCSV();
        }
    }

     /**
     * Check if HospitalID exists
     * If yes, get the Staff object from respective static role list
     * Queries for udpate details, uses switch cases to handle different changes to name, role, gender and age
     * If changing roles, create new respective staff Object, appends to respective static list and remove old Oject from list
     * Write changes back to CSV using StaffCSVHandler
     * @param void
     * @return void
     * @see StaffCSVHandler
     */
    public static void updateStaff() {
        String hospitalID = InputValidator.getNonEmptyString("Enter HospitalID: ");
        String roleLetter = hospitalID.substring(0,1);

        List<? extends Staff> temp;
        Staff staff = null;

        // Flag to check if found

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

        for (Staff tempStaff : temp) {
            if (tempStaff.getHospitalID().equalsIgnoreCase(hospitalID)) {
                staff = tempStaff;
                break;
            }
        }

        if (staff == null) {
            System.out.println("Staff member not found.");
            return;
        }

        System.out.println();
        System.out.println("Pick category to update: ");
        System.out.println("1 - Name");
        System.out.println("2 - Role");
        System.out.println("3 - Gender");
        System.out.println("4 - Age");

        int input = InputValidator.getIntegerInput("Choice: ", 1, 4);

        switch (input) {
            case 1:
                String inputName = InputValidator.getName("New Name: ");
                staff.setName(inputName);
                break;

            case 2:
                int role_index = 0;
                String role_choice = InputValidator.getRole("Role: ");
                if (role_choice.equalsIgnoreCase("Doctor")) {
                    role_index = 1;
                } else if (role_choice.equalsIgnoreCase("Pharmacist")) {
                    role_index = 2;
                } else if (role_choice.equalsIgnoreCase("Administrator")){
                    role_index = 3;
                }

                String newHospitalID = "";
                String name = staff.getName();
                String gender = staff.getGender();
                String age = staff.getAge();
                
                switch (role_index) {
                    case 1: 
                        // Check if there is a switch of role, if yes make new obj;
                        if (!(role_choice.substring(0,1).equals(roleLetter))) {

                            newHospitalID = getNextID(doctors);
                            Doctor doc = new Doctor(newHospitalID, name, gender, age); 
                            doc.setPassword((staff.getPassword()));
                            doctors.add(doc);

                            // Handle addition of new doc appt slots
                            doctorHandling(doc, true);
                        } 
                        break;

                    case 2: 
                        if (!(role_choice.substring(0,1).equals(roleLetter))) {
                            newHospitalID = getNextID(pharmacists);
                            Pharmacist pharm = new Pharmacist(newHospitalID, name, gender, age); 
                            System.out.println(staff.getPassword());
                            pharm.setPassword((staff.getPassword()));
                            System.out.println(pharm.getPassword());
                            pharmacists.add(pharm);
                            
                            // If remove doctor, remove appts
                            if(roleLetter.equalsIgnoreCase("D")) {
                                doctorHandling((Doctor)staff, false);
                            }
                        } 
                        break;

                    case 3: 
                        if (!(role_choice.substring(0,1).equals(roleLetter))) {
                            newHospitalID = getNextID(administrators);
                            Administrator admin = new Administrator(newHospitalID, name, gender, age); 
                            System.out.println(staff.getPassword());
                            admin.setPassword((staff.getPassword()));
                            System.out.println(admin.getPassword());
                            administrators.add(admin);

                            // If remove doctor, remove appts
                            if(roleLetter.equalsIgnoreCase("D")) {
                                doctorHandling((Doctor)staff, false);
                            }
                        } 
                        break;

                    default: System.out.println("Invalid role choice. Keeping the current role.");  break;
                }

                if (roleLetter.equals("P")) {
                    pharmacists.remove(staff);
                } else if (roleLetter.equals("D")) {
                    doctors.remove(staff);
                } else if (roleLetter.equals("A")) {
                    System.out.println("A");
                    administrators.remove(staff);
                }

                // if switch to diff role, handle change in ID or/and DoctorHandling 
                break;

            case 3:
                String gender_choice = InputValidator.getGender("New Gender: ");
                staff.setGender(gender_choice);
                break;

            case 4:
                String inputAge = String.valueOf(InputValidator.getIntegerInput("New Age: ", 1, 100));
                staff.setAge(inputAge);
                break;

            default:
                System.out.println("Invalid input noob.");
                break;
        }
        

        StaffCSVHandler.writeCSV();        
    }

    /**
     * Adds or removes appointments tagged to Doctor Object from Appointment.csv using AppointmentManager
     * @param Doctor 
     * @param addOrRemove If true, adds appointments for Doctor Object, if false, removes appointments for Doctor Object
     * @return void
     * @see AppointmentManager
     */
    public static boolean doctorHandling(Doctor doctor, boolean addOrRemove) {
        if (addOrRemove == true) {
            List<Doctor> temp = new ArrayList<>();
            temp.add(doctor);
            AppointmentManager.makeDailyAppointments(temp); 
            return true;
        } else {
            //Remove doc appointments from CSV
            AppointmentManager.removeAppointments(doctor);
            // If still have pending appointments for the doctor, stop removal, return false
            return true;
        }
    }

    /**
     * Runs through list of Staff Objects to find largest current ID, return next ID
     * @param objList
     * @return nextID Next largest ID
     * @see AppointmentManager
     */
    private static String getNextID(List<? extends Staff> objList) {
        int largest_ID  = 0;
        String role_letter = objList.get(0).getHospitalID().substring(0,1);

        for (Staff obj : objList) {
            largest_ID = Math.max(Integer.parseInt(obj.getHospitalID().substring(1)), largest_ID);
        }

        String formattedNumber = String.format("%03d", largest_ID+1);
        String nextID = role_letter + String.valueOf(formattedNumber);

        return nextID;
    }

    /**
     * With unique hospital ID searches through static list and returns appropriate Staff Object 
     * @param hospitalID 
     * @return Staff
     */
    public static Staff getStaffByID(String hospitalID) {
        String role_letter = hospitalID.substring(0,1);

        if(role_letter.equalsIgnoreCase("D")) {
            for(Doctor doctor :doctors) {
                if (doctor.getHospitalID().equalsIgnoreCase(hospitalID)) {return doctor;}
            }
        } else if (role_letter.equalsIgnoreCase("P")) {
            for(Pharmacist pharmacist :pharmacists) {
                if (pharmacist.getHospitalID().equalsIgnoreCase(hospitalID)) {return pharmacist;}
            }
        } else if (role_letter.equalsIgnoreCase("A")) {
            for(Administrator administrator :administrators) {
                if (administrator.getHospitalID().equalsIgnoreCase(hospitalID)) {return administrator;}
            }
        } else {
            System.out.println("\"Invalid Hospital ID: No such role prefix exists.");
            return null;
        }   

        // If not found
        System.out.println("No staff member found with Hospital ID: " + hospitalID);
        return null;
    }
}


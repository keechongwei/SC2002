import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StaffManager implements Manager {
    static List<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
    static List<Administrator> administrators = new ArrayList<Administrator>();
    static List<Doctor> doctors = new ArrayList<Doctor>();
    static boolean headerline = true;

    static Scanner input_scanner = new Scanner(System.in);

    public static void initialise() {
        if (!((StaffCSVHandler.csvFile).exists()) || (StaffCSVHandler.csvFile).length() == 0) {
            // File doesn't exist or is empty, create daily appointments
            System.out.println("Staff_List.csv is empty or missing.");
        } else {
            System.out.println("Loading Staff from Staff_List.csv...");
            StaffCSVHandler.loadCSV();
        }
    }
    public static void updateStaff() {
        System.out.println("Enter HospitalID: ");
        String hospitalID = input_scanner.nextLine();
    
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

        System.out.println("Pick category to update: ");
        System.out.println("1 - Name");
        System.out.println("2 - Role");
        System.out.println("3 - Gender");
        System.out.println("4 - Age");

        int input = InputValidator.getIntegerInput("Choice: ", 1, 4);

        switch (input) {
            case 1:
                System.out.println("New Name: ");
                String inputName = input_scanner.nextLine();
                staff.setName(inputName);
                break;

            case 2:
                System.out.println("Pick the Role: ");
                System.out.println("1 - Doctor");
                System.out.println("2 - Pharmacist");
                System.out.println("3 - Administrator");
                int role_choice = input_scanner.nextInt();
                input_scanner.nextLine();

                String role = "";
                String newHospitalID = "";

                String name = staff.getName();
                String gender = staff.getGender();
                String age = staff.getAge();
                
                switch (role_choice) {
                    case 1: 
                        role = "Doctor";
                        // Check if there is a switch of role, if yes make new obj;
                        if (!(role.substring(0,1).equals(roleLetter))) {

                            newHospitalID = getNextID(doctors);
                            Doctor doc = new Doctor(newHospitalID, name, gender, age); 
                            doc.setPassword((staff.getPassword()));
                            doctors.add(doc);

                            // Handle addition of new doc appt slots
                            doctorHandling(doc, true);
                        } 
                        break;

                    case 2: 
                        role = "Pharmacist";
                        if (!(role.substring(0,1).equals(roleLetter))) {
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
                        role = "Administrator"; 
                        if (!(role.substring(0,1).equals(roleLetter))) {
                            newHospitalID = getNextID(administrators);
                            Administrator admin = new Administrator(newHospitalID, name, gender, age); 
                            admin.setPassword((staff.getPassword()));
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
                System.out.println("Pick the Gender: ");
                System.out.println("1 - Male");
                System.out.println("2 - Female");
                System.out.println("3 - Others");
                int gender_choice = input_scanner.nextInt();
                input_scanner.nextLine();
                String inputGender = "";

                switch (gender_choice) {
                    case 1:
                        inputGender = "Male";
                        break;
                    case 2:
                        inputGender = "Female";
                        break;
                    case 3:
                        inputGender = "Others";
                        break;
                    default:
                        System.out.println("Invalid gender choice. Keeping the current gender.");
                        break;
                }

                staff.setGender(inputGender);
                break;

            case 4:
                System.out.println("New Age: ");
                String inputAge = input_scanner.nextLine();
                staff.setAge(inputAge);
                break;

            default:
                System.out.println("Invalid input noob.");
                break;
        }
        

        StaffCSVHandler.writeCSV();        
    }

    // true means adding new doctor
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


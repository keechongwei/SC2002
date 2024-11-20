import java.util.Scanner;

public class PatientRegistration {
    static Scanner sc = new Scanner(System.in);

    public static void printMenu(){
        int choice = 0;
        while(choice != 2){
            System.out.println("===PATIENT REGISTRATION===");
            System.out.println("(1) Register As New Patient");
            System.out.println("(2) Proceed to Hospital Login Management System");
            choice = sc.nextInt();
            switch(choice){
                case 1:
                register();
                System.out.println("PROCEEDING TO HOSPITAL MANAGEMENT SYSTEM");
                break;
                case 2:
                System.out.println("PROCEEDING TO HOSPITAL MANAGEMENT SYSTEM");
                break;
            }
        }
    }
    public static String queryForGender() {
        while (true) {
            System.out.println("===[3/6] GENDER ===\nPlease enter M for Male, F for Female, or O for Other:");
            String input = sc.nextLine().trim().toUpperCase();
            if (input.equals("M")) {
                return "Male";
            }
            else if(input.equals("F")){
                return "Female";
            }
            else if(input.equals("O")){
                return "Others";
            }
            System.out.println("Invalid gender. Please enter M, F, or O.");
        }
    }
    
    public static void register() {
            String name = InputValidator.getNonEmptyString("=== [1/6] NAME ===\n");
            String dob = InputValidator.getDateInput("=== [2/6] DATE OF BIRTH ===\n").toString();
            String gender = InputValidator.getGender("=== [3/6] GENDER ===\n");
            String bloodType = InputValidator.getBloodType("=== [4/6] BLOOD TYPE ===\n");
            String email = InputValidator.getEmailAddress("===[5/6] EMAIL ===\n");
            String phoneNumber = InputValidator.getPhoneNumber("===[6/6] PHONE NUMBER ===\n");
            String patientID = "P100" + PatientManager.nextPatientNumber;
            Patient p = new Patient(patientID, name, dob, gender, bloodType, phoneNumber, email);
            PatientManager.addPatient(p);
            System.out.println("=Patient Successfully Created!");
        }

}

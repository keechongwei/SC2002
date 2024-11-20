import java.util.Scanner;
import java.util.regex.Pattern;

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
            String name = queryInput("===[1/6] NAME ===", input -> !input.trim().isEmpty(), "Name cannot be empty.");
            String dob = queryInput("===[2/6] DATE OF BIRTH (YYYY-MM-DD) ===", 
                                    input -> input.matches("\\d{4}-\\d{2}-\\d{2}"), 
                                    "Invalid date format. Please use YYYY-MM-DD.");
            String gender = queryForGender();
            String bloodType = queryInput("===[4/6] BLOOD TYPE ===", 
                                          input -> Pattern.matches("^(A|B|AB|O)[+-]$", input.toUpperCase()), 
                                          "Invalid blood type. Use A+, A-, B+, etc.");
            String email = queryInput("===[5/6] EMAIL ===", 
                                      input -> input.matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"), 
                                      "Invalid email format.");
            String phoneNumber = queryInput("===[6/6] PHONE NUMBER ===", 
                                            input -> input.matches("\\d{8,15}"), 
                                            "Invalid phone number. Use 8-15 digits only.");
            
            String patientID = "P100" + PatientManager.nextPatientNumber;
            Patient p = new Patient(patientID, name, dob, gender, bloodType, phoneNumber, email);
            PatientManager.addPatient(p);
            System.out.println("=Patient Successfully Created!");
        }

    public static String queryInput(String prompt, InputValidator validator, String errorMessage) {
        while (true) {
            System.out.println(prompt);
            String input = sc.nextLine().trim();
            if (validator.isValid(input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }

    @FunctionalInterface
    interface InputValidator {
        boolean isValid(String input);
    }
}



public class PatientRegistration {

    public static void printMenu() {
        int choice = 0;
        while (choice != 2) {
            System.out.println("===PATIENT REGISTRATION===");
            System.out.println("(1) Register As New Patient");
            System.out.println("(2) Proceed to Hospital Login Management System");
            choice = InputValidator.getIntegerInput("Enter your choice: ", 1, 2);
            
            switch (choice) {
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
    
    public static void register() {
        String name = InputValidator.getName("===[1/6] NAME ===\n");
        String dob = InputValidator.getDateInput("===[2/6] DATE OF BIRTH\n").toString();
        String gender = InputValidator.getGender("===[3/6] GENDER ===\n");
        String bloodType = InputValidator.getBloodType("===[4/6] BLOOD TYPE ===\n");
        String email = InputValidator.getEmailAddress("===[5/6] EMAIL ===\n");
        String phoneNumber = InputValidator.getPhoneNumber("===[6/6] PHONE NUMBER ===\n");
        
        String patientID = "P" + String.format("%04d", PatientManager.nextPatientNumber);
        Patient p = new Patient(patientID, name, dob, gender, bloodType, phoneNumber, email);
        PatientManager.addPatient(p);
        System.out.println("Patient Successfully Created!\n");
    }
}
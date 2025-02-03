package utility;
import java.util.InputMismatchException;
import java.util.Scanner;

import managers.InventoryManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A utility class for validating and obtaining user input from the console.
 * Provides methods for validating integers, doubles, strings, patterns, dates, times,
 * and other specific input formats such as IDs, emails, and phone numbers.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    // Common patterns
    private static final String PATIENT_ID_PATTERN = "P\\d{4}";
    private static final String DOCTOR_ID_PATTERN = "D\\d{3}";
    private static final String APPOINTMENT_ID_PATTERN = "APT\\d{1,3}";
    private static final String PHONE_PATTERN = "\\d{8,12}";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String NAME_PATTERN = "^[A-Za-z\\s'-]+$";
    private static final String BLOOD_TYPE_PATTERN = "^(A|B|AB|O)[+-]$";
    private static final String GENDER_PATTERN = "^(Male|Female|Other)$";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm";
    private static final String PASSWORD_PATTERN = "^(?=.*\\d).{8,}$";
    private static final String ROLE_PATTERN = "^(Doctor|Pharmacist|Administrator)$";


    /**
     * Prompts the user to enter an integer within a specified range.
     *
     * @param prompt the message to display to the user
     * @param min    the minimum allowable value
     * @param max    the maximum allowable value
     * @return the validated integer input
     */
    public static int getIntegerInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.printf("Please enter a number between %d and %d%n", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    /**
     * Prompts the user to enter a double within a specified range.
     *
     * @param prompt the message to display to the user
     * @param min    the minimum allowable value
     * @param max    the maximum allowable value
     * @return the validated double input
     */
    public static double getDoubleInput(String prompt, double min, double max) {
        while (true) {
            try {
                System.out.print(prompt);
                double input = scanner.nextDouble();
                scanner.nextLine(); // Clear buffer

                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.printf("Please enter a number between %.2f and %.2f%n", min, max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }

    /**
     * Prompts the user to enter a string matching a specific pattern.
     *
     * @param prompt       the message to display to the user
     * @param pattern      the regex pattern for validation
     * @param errorMessage the error message to display if validation fails
     * @return the validated string input
     */
    public static String getPatternedInput(String prompt, String pattern, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (input.matches(pattern)) {
                return input;
            } else {
                System.out.println(errorMessage);
            }
        }
    }

    /**
     * Prompts the user to enter a non-empty string.
     *
     * @param prompt the message to display to the user
     * @return the validated non-empty string
     */
    public static String getNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }

    /**
     * Prompts the user to enter a date in the format "DD/MM/YYYY".
     *
     * @param prompt the message to display to the user
     * @return the validated {@link LocalDate} input
     */
    public static LocalDate getDateInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        while (true) {
            try {
                System.out.print(prompt + " (DD/MM/YYYY): ");
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use DD/MM/YYYY.");
            }
        }
    }

    /**
     * Prompts the user to enter a time in the 24-hour format "HH:mm".
     *
     * @param prompt the message to display to the user
     * @return the validated {@link LocalTime} input
     */
    public static LocalTime getTimeInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        while (true) {
            try {
                System.out.print(prompt + " (HH:mm): ");
                String input = scanner.nextLine().trim();
                return LocalTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please use 24-hour format (HH:mm).");
            }
        }
    }

    /**
     * Prompts the user to enter a valid patient ID in the format "PXXXX".
     *
     * @param prompt the message to display to the user
     * @return the validated patient ID
     */
    public static String getPatientId(String prompt) {
        return getPatternedInput(
            prompt,
            PATIENT_ID_PATTERN,
            "Invalid patient ID format. Please enter ID in format P001."
        ).toUpperCase();
    }

    /**
     * Prompts the user to enter a valid password with at least 8 characters and one number.
     *
     * @param prompt the message to display to the user
     * @return the validated password
     */
    public static String getPassword(String prompt){
        return getPatternedInput(
            prompt,
            PASSWORD_PATTERN,
            "Invalid Password. Ensure at least 8 characters and 1 number"
        );
    }

    /**
     * Prompts the user to enter a valid doctor ID in the format "DXXX".
     *
     * @param prompt the message to display to the user
     * @return the validated doctor ID
     */
    public static String getDoctorId(String prompt) {
        return getPatternedInput(
            prompt,
            DOCTOR_ID_PATTERN,
            "Invalid doctor ID format. Please enter ID in format D001."
        ).toUpperCase();
    }

    /**
     * Prompts the user to enter a valid appointment ID in the format "APTXXX".
     *
     * @param prompt the message to display to the user
     * @return the validated appointment ID
     */
    public static String getAppointmentId(String prompt) {
        return getPatternedInput(
            prompt,
            APPOINTMENT_ID_PATTERN,
            "Invalid appointment ID format. Please enter ID in format APT1."
        ).toUpperCase();
    }

    /**
     * Prompts the user to enter a valid phone number (8-12 digits).
     *
     * @param prompt the message to display to the user
     * @return the validated phone number
     */
    public static String getPhoneNumber(String prompt) {
        return getPatternedInput(
            prompt,
            PHONE_PATTERN,
            "Invalid phone number format. Please enter 8-12 digits."
        );
    }

    /**
     * Prompts the user to enter a valid email address.
     *
     * @param prompt the message to display to the user
     * @return the validated email address
     */
    public static String getEmailAddress(String prompt) {
        return getPatternedInput(
            prompt,
            EMAIL_PATTERN,
            "Invalid email format. Please enter a valid email address."
        );
    }

    /**
     * Prompts the user to enter a valid name (letters, spaces, hyphens, apostrophes).
     *
     * @param prompt the message to display to the user
     * @return the validated name
     */
    public static String getName(String prompt) {
        return getPatternedInput(
            prompt,
            NAME_PATTERN,
            "Invalid name format. Please use only letters, spaces, hyphens, and apostrophes."
        );
    }

    /**
     * Prompts the user to enter a valid blood type (A+, B-, O+, etc.).
     *
     * @param prompt the message to display to the user
     * @return the validated blood type
     */
    public static String getBloodType(String prompt) {
        return getPatternedInput(
            prompt,
            BLOOD_TYPE_PATTERN,
            "Invalid blood type. Please enter A+, A-, B+, B-, AB+, AB-, O+, or O-."
        ).toUpperCase();
    }

    /**
     * Prompts the user to enter a valid gender (e.g., Male, Female, Other).
     *
     * @param prompt the message to display to the user
     * @return the validated gender
     */
    public static String getGender(String prompt) {
        return getPatternedInput(
            prompt,
            GENDER_PATTERN,
            "Invalid gender. Please enter Male, Female, or Other."
        );
    }

    /**
     * Prompts the user to confirm with "y" or "n".
     *
     * @param prompt the message to display to the user
     * @return true for "yes", false for "no"
     */
    public static boolean getConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Please enter 'y' for yes or 'n' for no.");
            }
        }
    }

    /**
     * Prompts the user to enter consultation notes.
     * The input ends when "END" is typed on a new line.
     *
     * @param prompt the message to display to the user
     * @return the consultation notes as a string
     */
    public static String getConsultationNotes(String prompt) {
        System.out.println(prompt);
        StringBuilder notes = new StringBuilder();
        String line;
        
        System.out.println("Enter your notes (type 'END' on a new line to finish):");
        while (!(line = scanner.nextLine()).equals("END")) {
            notes.append(line);
        }
        
        return notes.toString().trim();
    }

    /**
     * Prompts the user to enter a medication name.
     *
     * @param prompt the message to display to the user
     * @return the validated medication name
     */
    public static String getMedicationName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            for (Medication med : InventoryManager.listOfMedications){
                if (input.equalsIgnoreCase(med.getMedicationName())){
                    return input;
                }
            }
            System.out.println("Input is not a valid Medication. Please try again.");
            System.out.println("List of Medications:");
            for (Medication med : InventoryManager.listOfMedications){
                System.out.println(med.getMedicationName());
            }
        }
    }

    /**
     * Prompts the user to enter a valid role (e.g., Doctor, Pharmacist, Administrator).
     *
     * @param prompt the message to display to the user
     * @return the validated role
     */
    public static String getRole(String prompt) {
        return getPatternedInput(
            prompt,
            ROLE_PATTERN,
            "Invalid role. Please enter Doctor, Pharmacist, or Administrator."
        );
    }
}
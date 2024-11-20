import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    // Common patterns
    private static final String PATIENT_ID_PATTERN = "P\\d{4}";
    private static final String DOCTOR_ID_PATTERN = "D\\d{3}";
    private static final String APPOINTMENT_ID_PATTERN = "A\\d{3}";
    private static final String PHONE_PATTERN = "\\d{8,12}";
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String NAME_PATTERN = "^[A-Za-z\\s'-]+$";
    private static final String BLOOD_TYPE_PATTERN = "^(A|B|AB|O)[+-]$";
    private static final String GENDER_PATTERN = "^(M|F|Male|Female|Other)$";
    private static final String DATE_PATTERN = "dd/MM/yyyy";
    private static final String TIME_PATTERN = "HH:mm";

    /**
     * Gets a valid integer input within a specified range
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
     * Gets a valid double input within a specified range
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
     * Gets a valid string input matching a specific pattern
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
     * Gets a non-empty string input
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
     * Gets a valid date input
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
     * Gets a valid time input
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
     * Validates and gets a patient ID
     */
    public static String getPatientId(String prompt) {
        return getPatternedInput(
            prompt,
            PATIENT_ID_PATTERN,
            "Invalid patient ID format. Please enter ID in format P001."
        ).toUpperCase();
    }

    /**
     * Validates and gets a doctor ID
     */
    public static String getDoctorId(String prompt) {
        return getPatternedInput(
            prompt,
            DOCTOR_ID_PATTERN,
            "Invalid doctor ID format. Please enter ID in format D001."
        ).toUpperCase();
    }

    /**
     * Validates and gets an appointment ID
     */
    public static String getAppointmentId(String prompt) {
        return getPatternedInput(
            prompt,
            APPOINTMENT_ID_PATTERN,
            "Invalid appointment ID format. Please enter ID in format A001."
        ).toUpperCase();
    }

    /**
     * Validates and gets a phone number
     */
    public static String getPhoneNumber(String prompt) {
        return getPatternedInput(
            prompt,
            PHONE_PATTERN,
            "Invalid phone number format. Please enter 8-12 digits."
        );
    }

    /**
     * Validates and gets an email address
     */
    public static String getEmailAddress(String prompt) {
        return getPatternedInput(
            prompt,
            EMAIL_PATTERN,
            "Invalid email format. Please enter a valid email address."
        );
    }

    /**
     * Validates and gets a name
     */
    public static String getName(String prompt) {
        return getPatternedInput(
            prompt,
            NAME_PATTERN,
            "Invalid name format. Please use only letters, spaces, hyphens, and apostrophes."
        );
    }

    /**
     * Validates and gets a blood type
     */
    public static String getBloodType(String prompt) {
        return getPatternedInput(
            prompt,
            BLOOD_TYPE_PATTERN,
            "Invalid blood type. Please enter A+, A-, B+, B-, AB+, AB-, O+, or O-."
        ).toUpperCase();
    }

    /**
     * Validates and gets a gender
     */
    public static String getGender(String prompt) {
        return getPatternedInput(
            prompt,
            GENDER_PATTERN,
            "Invalid gender. Please enter Male, Female, or Other."
        );
    }

    /**
     * Gets a yes/no confirmation
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
                System.out.println("Please enter 'y' or 'n'.");
            }
        }
    }

    /**
     * Validates and gets a consultation note
     */
    public static String getConsultationNotes(String prompt) {
        System.out.println(prompt);
        StringBuilder notes = new StringBuilder();
        String line;
        
        System.out.println("Enter your notes (type 'END' on a new line to finish):");
        while (!(line = scanner.nextLine()).equals("END")) {
            notes.append(line).append("\n");
        }
        
        return notes.toString().trim();
    }

    /**
     * Validates and gets a medication name
     */
    public static String getMedicationName(String prompt) {
        return getNonEmptyString(prompt);
    }
}
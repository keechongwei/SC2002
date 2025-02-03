package managers.csvhandlers;

/**
 * An Interface for all CSVHandlers to implement
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 * @see AppointmentCSVHandler
 * @see InventoryCSVHandler
 * @see PatientManagerCSVHandler
 */

public interface CSVHandler {
    public static void loadCSV(){};
    public static void writeCSV(){};
}

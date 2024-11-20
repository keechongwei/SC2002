/**
 * interface class
 * for all CSVHandlers to implement
 * @see AppointmentCSVHandler
 * @see InventoryCSVHandler
 * @see PatientManagerCSVHandler
 */
interface CSVHandler {
    public static void loadCSV(){};
    public static void writeCSV(){};
}

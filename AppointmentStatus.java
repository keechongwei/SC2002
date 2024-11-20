/**
 * Enumerator containing possible status of AppointmentSlots
 * Pending - Patient Scheduled with Doctor, pending Doctor's approval
 * Confirmed - Doctor accepted Patient request
 * Cancelled - Doctor rejected Patient request
 * Completed - Doctor has conducted appointment and completed Appointment Outcome Record
 * Available - Free slots for patient to schedule with doctor
 * Unavailable - Doctor has marked slot as unavailable  
 */
public enum AppointmentStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED,
    AVAILABLE,
    UNAVAILABLE
}

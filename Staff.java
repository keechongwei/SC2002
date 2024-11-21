/**
 * The {@code Staff} class is a subclass of {@code User} that represents staff members in a hospital system.
 * It includes additional attributes such as name, gender, and age, and provides methods to access
 * and modify these attributes. Staff details can also be converted to a CSV-formatted string.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class Staff extends User {

    /**
     * The name of the staff member.
     */
    protected String name;

    /**
     * The gender of the staff member.
     */
    protected String gender;

    /**
     * The age of the staff member.
     */
    protected String age;


    /**
     * Constructs a new {@code Staff} instance with the specified hospital ID and password.
     * 
     * @param HospitalID the unique hospital ID of the staff member.
     * @param password   the password associated with the staff member's account.
     */
    protected Staff(String HospitalID, String password) {
        super(HospitalID, password);
    }


    /**
     * Returns the name of the staff member.
     * 
     * @return the name of the staff member.
     */
    protected String getName() {
        return this.name;
    }
    /**
     * Returns the gender of the staff member.
     * 
     * @return the gender of the staff member.
     */
    protected String getGender() {
        return this.gender;
    }

    /**
     * Returns the age of the staff member.
     * 
     * @return the age of the staff member.
     */
    protected String getAge() {
        return this.age;
    }

    /**
     * Sets the name of the staff member.
     * 
     * @param name the new name of the staff member.
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the gender of the staff member.
     * 
     * @param gender the new gender of the staff member.
     */
    protected void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the age of the staff member.
     * 
     * @param age the new age of the staff member.
     */
    protected void setAge(String age) {
        this.age = age;
    }

    /**
     * Converts the staff member's details to a CSV-formatted string.
     * 
     * @return a CSV representation of the staff member's details in the format:
     *         {@code HospitalID;Password;Name;Staff;Gender;Age}.
     */
    protected String toCSV() {
        // Combine all attributes into a CSV string
        return super.getHospitalID() + ";" + super.getPassword() + ";" + name + ";" + "Staff" + ";" + gender + ";" + age;
    }
    
}

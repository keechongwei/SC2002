/**
 * The {@code User} class represents a generic user in a hospital management system.
 * It provides attributes for user authentication, such as a hospital ID and password,
 * as well as basic functionality to retrieve and update these attributes.
 * @author SCSKGroup2
 * @version 1.0
 * @since 2024-11-21
 */
public class User {

    /**
     * The unique hospital ID associated with the user.
     */
    private String HospitalID;

    /**
     * The password associated with the user's account.
     */
    private String Password;

    /**
     * Constructs a new {@code User} instance with the specified hospital ID and password.
     *
     * @param HospitalID the unique hospital ID of the user.
     * @param password   the password for the user's account.
     */
	public User(String HospitalID, String password){
		this.HospitalID = HospitalID;
		this.Password = password;
	}

    /**
     * Returns the hospital ID of the user.
     *
     * @return the hospital ID of the user.
     */
	public String getHospitalID(){
		return this.HospitalID;
	}

    /**
     * Returns the password of the user.
     *
     * @return the password of the user.
     */
	public String getPassword(){
		return this.Password;
	}

    /**
     * Updates the password for the user's account.
     *
     * @param password the new password to be set.
     */
	public void setPassword(String password){
		this.Password = password;
	}


    /**
     * Displays a menu to the user.
     * By default, this method notifies the user as "Unknown User" and exits the program.
     * Subclasses can override this method to provide a customized menu for different user types.
     */
	public void printMenu(){
		System.out.println("Unknown User Detected. Exiting Program.");
	};

}
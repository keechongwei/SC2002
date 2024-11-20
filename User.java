
public class User {

	private String HospitalID;
	private String Password;

	public User(String HospitalID, String password){
		this.HospitalID = HospitalID;
		this.Password = password;
	}

	public String getHospitalID(){
		return this.HospitalID;
	}

	public String getPassword(){
		return this.Password;
	}

	public void setPassword(String password){
		this.Password = password;
	}

	public void printMenu(){
		System.out.println("Unknown User Detected. Exiting Program.");
	};

}
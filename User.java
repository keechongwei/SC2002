
public class User {

	private String HospitalID;
	private String Password;
	private String test;

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

}
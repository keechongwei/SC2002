
public class User {
    enum Role{Unknown,Patient,Doctor,Pharmacist,Administrator}

	private String HospitalID;
	private String Password;
	private Role User_Role;

	public User(String HospitalID, String password, Role role){
		this.HospitalID = HospitalID;
		this.Password = password;
		this.User_Role = role;
	}

	public String getHospitalID(){
		return this.HospitalID;
	}

	public String getPassword(){
		return this.Password;
	}

}
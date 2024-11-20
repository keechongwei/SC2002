public class Staff extends User {

    protected String name;
    protected String gender;
    protected String age;

    protected Staff(String HospitalID, String password) {
        super(HospitalID, password);
    }

    protected String getName() {
        return this.name;
    }
    protected String getGender() {
        return this.gender;
    }
    protected String getAge() {
        return this.age;
    }
    protected void setName(String name) {
        this.name = name;
    }
    protected void setGender(String gender) {
        this.gender = gender;
    }
    protected void setAge(String age) {
        this.age = age;
    }
    
    protected String toCSV() {
        // Combine all attributes into a CSV string
        return super.getHospitalID() + ";" + super.getPassword() + ";" + name + ";" + "Staff" + ";" + gender + ";" + age;
    }
    
    
}

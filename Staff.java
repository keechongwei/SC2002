public class Staff extends User {

    protected String name;
    protected String gender;
    protected String age;

    protected Staff(String HospitalID, String password) {
        super(HospitalID, password);
    }

    protected String getName(String name) {
        return this.name;
    }
    protected String getName(String gender) {
        return this.gender;
    }
    protected String getName(String age) {
        return this.age;
    }
    protected void setName(String name) {
        this.name = name;
    }
    protected void setName(String gender) {
        this.gender = gender;
    }
    protected void setName(String age) {
        this.age = age;
    }

    
}

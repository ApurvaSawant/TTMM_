package i.ddsweb.ttmm;

public class Users {

    private String Name;
    private String Email;
    private String Phn;
    private String Dob;
    private String Pass;


    public Users()
    {

    }

    public Users(String name, String email, String phn, String dob, String pass) {
        Name = name;
        Email = email;
        Phn = phn;
        Dob = dob;
        Pass = pass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhn() {
        return Phn;
    }

    public void setPhn(String phn) {
        Phn = phn;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }
}
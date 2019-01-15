package forum.student.thebrooker;

public class UserProfile {
    public String Email;
    public String FirstName;
    public String Lastname;

    public UserProfile(String email, String firstName, String lastname) {
        this.Email = email;
        this.FirstName = firstName;
        this.Lastname = lastname;
    }

    public UserProfile() {
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        this.Lastname = lastname;
    }
}


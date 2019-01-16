package forum.student.thebrooker;

public class UserProfile {
    public String Email;
    public String FirstName;
    public String Lastname;
    public String AccountType;
    public String UserId;

    public UserProfile(String email, String firstName, String lastname, String UserId, String AccountType) {
        this.Email = email;
        this.FirstName = firstName;
        this.Lastname = lastname;
        this.AccountType = AccountType;
        this.UserId = UserId;
    }

    public UserProfile() {
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        this.AccountType = accountType;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
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


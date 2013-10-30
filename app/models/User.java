package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class User {
    // data members
    private int id;
    private String firstName;
    private String lastName;
    private String uniqueName;
    private String password;

    public User(ResultSet rs) throws SQLException {
	id = rs.getInt("id_user");
	firstName = rs.getString("first_name");
	lastName = rs.getString("last_name");
	uniqueName = rs.getString("unique_name");
	password = rs.getString("password");
    }

    // getters
    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUniqueName() { return uniqueName; }
    public String getPassword() { return password; }

    // setters
    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setUniqueName(String uniqueName) { this.uniqueName = uniqueName; }
    public void setPassword(String password) { this.password = password; }

    /**
     * @return the student's first and last names, joined by a space
     */
    public String getFullName() {
	return this.firstName + " " + this.lastName;
    }
    
    public static User authenticate(String username, String password) {
	User u = EJDatabase.getUser(username);
	User result = null;
	
	if (u != null && u.password.equals(password)) {
	    result = u;
	}
	
	return result;
    }

    public static class Form {
	public interface Login {};
	public interface SignUp {};
	
	@Required (groups = {Login.class, SignUp.class})
	@MinLength(value = 6, message = "too short (min is 6 chars)", groups = {Login.class, SignUp.class})
	@MaxLength(value = 45, message = "too long (max is 45 chars)", groups = {Login.class, SignUp.class})
	private String username;
	
	@Required (groups = {Login.class, SignUp.class})
	@MinLength(value = 6, message = "too short (min is 6 chars)", groups = {Login.class, SignUp.class})
	@MaxLength(value = 45, message = "too long (max is 45 chars)", groups = {Login.class, SignUp.class})
	private String password;
	
	@Required (groups = {SignUp.class})
	@MinLength(value = 6, message = "too short (min is 6 chars)", groups = {SignUp.class})
	@MaxLength(value = 45, message = "too long (max is 45 chars)", groups = {SignUp.class})
	private String passwordRetype;
	
	@Required (groups = {SignUp.class})
	@MinLength(value = 1, message = "too short (min is 1 char)", groups = {SignUp.class})
	@MaxLength(value = 45, message = "too long (max is 45 chars)", groups = {SignUp.class})
	private String firstName;
	
	@Required (groups = {SignUp.class})
	@MinLength(value = 1, message = "too short (min is 1 char)", groups = {SignUp.class})
	@MaxLength(value = 45, message = "too long (max is 45 chars)", groups = {SignUp.class})
	private String lastName;
	
	// getters
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public String getPasswordRetype() { return passwordRetype; }
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	
	// setters
	public void setUsername(String username) { this.username = username; }
	public void setPassword(String password) { this.password = password; }
	public void setPasswordRetype(String passwordRetype) { this.passwordRetype = passwordRetype; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
    }
}

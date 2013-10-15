package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    	// data members
	private String profileImageURI;
	private String firstName;
	private String lastName;
	private String uniqueName;

	public User(ResultSet rs) throws SQLException {
	    firstName = rs.getString("first_name");
	    lastName = rs.getString("last_name");
	    uniqueName = rs.getString("unique_name");
	}
	
	// getters
	public String getProfileImageURI() { return profileImageURI; }	
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getUniqueName() { return uniqueName; }
	
	// setters
	public void setProfileImageURI(String profileImageURI) { this.profileImageURI = profileImageURI; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setUniqueName(String uniqueName) { this.uniqueName = uniqueName; }
	
	/**
	 * @return the student's first and last names, joined by a space
	 */
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
}

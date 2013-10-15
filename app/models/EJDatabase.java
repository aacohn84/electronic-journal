package models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import play.db.DB;

public class EJDatabase {

    public static User getUser(String username) {
	String getUserCall = "{call GET_USER(?)}";
	User user = null;
	try (Connection c = DB.getConnection();
		CallableStatement getUserStmt = c.prepareCall(getUserCall)) {
	    getUserStmt.setString(1, username);
	    ResultSet rs = getUserStmt.executeQuery();
	    if (rs.first()) {
		user = new User(rs);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return user;
    }
}

package models;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.db.DB;
import util.Pair;

public class EJDatabase {

	public static void createGroup(int teacherId, int period, String subject,
			String passphrase) {
		
		final String createGroupQuery = 
				"INSERT INTO `group` (\r\n" + 
				"	id_teacher,\r\n" + 
				"	period,\r\n" + 
				"	subject,\r\n" + 
				"	passphrase\r\n" + 
				") VALUES (\r\n" + 
				"	?, \r\n" + 
				"	?, \r\n" + 
				"	?, \r\n" + 
				"	?\r\n" + 
				");";
		
		try (Connection c = DB.getConnection();
				PreparedStatement createGroupStmt = c
						.prepareStatement(createGroupQuery);) {

			createGroupStmt.setInt(1, teacherId);
			createGroupStmt.setInt(2, period);
			createGroupStmt.setString(3, subject);
			createGroupStmt.setString(4, passphrase);
			
			createGroupStmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a list of groups in which the specified user is a member. Group
	 * membership corresponds to an entry in the roster table.
	 * 
	 * @param userId
	 *            - Unique ID of the user for whom to retrieve a list of groups.
	 * @return A list of groups if successful, otherwise an empty list. Never
	 *         <code>null</code>.
	 */
	public static ArrayList<Group> getGroups(int userId) {
		final String getGroupsQuery = 
				"SELECT * \r\n" + 
				"FROM `roster` LEFT JOIN `group`\r\n" + 
				"USING(`id_group`)\r\n" + 
				"WHERE `id_user` = ?;";
	
		ArrayList<Group> groups = new ArrayList<Group>();
		
		try (Connection c = DB.getConnection();
				PreparedStatement getGroupsStmt = c.prepareStatement(getGroupsQuery);) {
	
			getGroupsStmt.setInt(1, userId);
	
			ResultSet rs = getGroupsStmt.executeQuery();
	
			// build list of groups from result set
			while (rs.next()) {
				Group g = new Group(rs);
				groups.add(g);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return groups;
	}

	public static ArrayList<Group> getGroupsTaughtByUser(int teacherId) {
		final String getGroupsTaughtByUserCall = "SELECT * FROM `group` WHERE `id_teacher` = ?;";
	
		ArrayList<Group> groups = new ArrayList<Group>();
	
		try (Connection c = DB.getConnection();
				PreparedStatement getGroupsTaughtByUserStmt = c
						.prepareStatement(getGroupsTaughtByUserCall);) {
	
			getGroupsTaughtByUserStmt.setInt(1, teacherId);
	
			ResultSet rs = getGroupsTaughtByUserStmt.executeQuery();
	
			while (rs.next()) {
				Group g = new Group(rs);
				groups.add(g);
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return groups;
	}

	/**
	 * Gets the most recent prompt for the group specified by
	 * <code>groupId</code>. If the user specified by <code>responderId</code>
	 * has responded to the prompt, then the response will also be retrieved.
	 * 
	 * @param groupId
	 *            - Unique ID of the group for which the most recent prompt will
	 *            be retrieved.
	 * @param responderId
	 *            - Unique ID of a user in the group specified by
	 *            <code>groupId</code>. If this user has responded to the most
	 *            recent prompt, their response will be fetched.
	 * @return a <code>Pair</code> containing <code>Prompt</code> and
	 *         <code>Response</code>. Returns <code>null</code> if no results
	 *         were found. <code>Response</code> is <code>null</code> if no
	 *         response exists.
	 */
	public static Pair<Prompt, Response> getMostRecentPromptAndResponse(int groupId,
			int responderId) {
	
		final String getMostRecentPromptAndResponseQuery = 
				"SELECT * \r\n" + 
				"FROM `prompt` LEFT JOIN (\r\n" + 
				"	SELECT * FROM `prompt_response` WHERE `id_responder` = ?\r\n" + 
				") `prompt_response`\r\n" + 
				"USING(`id_prompt`)\r\n" + 
				"WHERE `id_group` = ?\r\n" + 
				"ORDER BY `prompt`.`creation_date` DESC\r\n" + 
				"LIMIT 1;";
	
		Pair<Prompt, Response> pair = null;
		
		try (Connection c = DB.getConnection();
				PreparedStatement getMostRecentPromptAndResponseStmt = c
						.prepareStatement(getMostRecentPromptAndResponseQuery);) {
	
			getMostRecentPromptAndResponseStmt.setInt(1, responderId);
			getMostRecentPromptAndResponseStmt.setInt(2, groupId);
	
			ResultSet rs = getMostRecentPromptAndResponseStmt.executeQuery();
	
			if (rs.first()) {
				// a prompt is guaranteed to exist
				pair = new Pair<Prompt, Response>();
				pair.setA(new Prompt(rs));
				// response not guaranteed, so we check if response text is null
				if (rs.getString("prompt_response.text") != null) {
					pair.setB(new Response(rs));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return pair;
	}

	/**
	 * Gets the <code>User</code> record associated with the given
	 * <code>userId</code>.
	 * 
	 * @param userId
	 *            - the unique ID of the user being retrieved
	 * @return A <code>User</code> object if successful, otherwise
	 *         <code>null</code>.
	 */
	public static User getUser(int userId) {
		final String getUserQuery = 
				"SELECT * FROM `user` WHERE `id_user` = ?;";
		User user = null;
		try (Connection c = DB.getConnection();
				PreparedStatement getUserStmt = c.prepareStatement(getUserQuery)) {
			getUserStmt.setInt(1, userId);
			ResultSet rs = getUserStmt.executeQuery();
			if (rs.first()) {
				user = new User(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * Gets the <code>User</code> record associated with the given
	 * <code>username</code>.
	 * 
	 * @param username
	 *            - the unique name of the user being retrieved
	 * @return A <code>User</code> object if successful, otherwise
	 *         <code>null</code>.
	 */
	public static User getUser(String username) {
		final String getUserQuery = "SELECT * FROM `user` WHERE `unique_name` = ?;";
		User user = null;
		try (Connection c = DB.getConnection();
				PreparedStatement getUserStmt = c.prepareStatement(getUserQuery)) {
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

	/**
	 * Gets the <code>User</code> record associated with the given
	 * <code>username</code> and <code>password</code>.
	 * 
	 * @param username
	 *            - the unique name of the user to retrieve
	 * @param password
	 *            - the non-hashed password associated with
	 *            <code>username</code>
	 * @return A <code>User</code> object if successful, otherwise
	 *         <code>null</code>.
	 */
	public static User getUser(String username, String password) {
		final String getUserQuery = 
				"SELECT * FROM `user` WHERE `unique_name` = ? AND `password` = ?;";
		User user = null;
		try (Connection c = DB.getConnection();
				PreparedStatement getUserStmt = c.prepareStatement(getUserQuery)) {
			getUserStmt.setString(1, username);
			getUserStmt.setString(2, password);
			ResultSet rs = getUserStmt.executeQuery();
			if (rs.first()) {
				user = new User(rs);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public static boolean isTeacher(int userId) {
		final String isTeacherQuery = "SELECT * FROM `teacher` WHERE `id_teacher` = ?";
		
		boolean isTeacher = false;
		try (Connection c = DB.getConnection();
				PreparedStatement isTeacherStmt = c.prepareStatement(isTeacherQuery);) {
	
			isTeacherStmt.setInt(1, userId);
	
			ResultSet rs = isTeacherStmt.executeQuery();
			
			isTeacher = rs.first(); // true if a row was returned
			
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isTeacher;
	}

	public static void saveResponse(int responderId, int promptId,
			String responseText) throws EJDatabaseException {
	
		final String saveResponseQuery = 
				"INSERT INTO `prompt_response` (\r\n" + 
				"	`id_responder`,\r\n" + 
				"	`id_prompt`,\r\n" + 
				"	`text`,\r\n" + 
				"	`creation_date`\r\n" + 
				") VALUES (?, ?, ?, NOW())\r\n" +
				"ON DUPLICATE KEY UPDATE `text` = values(`text`);";
	
		try (Connection c = DB.getConnection();
				PreparedStatement saveResponseStmt = c
						.prepareStatement(saveResponseQuery);) {
	
			saveResponseStmt.setInt(1, responderId);
			saveResponseStmt.setInt(2, promptId);
			saveResponseStmt.setString(3, responseText);
	
			int numRowsUpdated = saveResponseStmt.executeUpdate();
			if (numRowsUpdated == 0) {
				throw new EJDatabaseException(
						"Problem saving response to database"
								+ "\nresponder_id: " + responderId
								+ "\nprompt_id: " + promptId + "\ntext: "
								+ responseText);
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves a new prompt to the database
	 * 
	 * @param creator
	 *            - id of the user creating the prompt
	 * @param groupId
	 *            - id of the group that can respond to this prompt
	 * @param promptText
	 *            - the prompt itself
	 * @throws EJDatabaseException
	 */
	public static void writePrompt(int groupId, String promptText)
			throws EJDatabaseException {

		final String writePromptQuery = 
				"INSERT INTO `prompt` (\r\n" + 
				"	`id_group`,\r\n" + 
				"	`text`,\r\n" + 
				"	`creation_date`\r\n" + 
				") VALUES (?, ?, NOW());";

		try (Connection c = DB.getConnection();
				PreparedStatement writePromptStmt = c
						.prepareStatement(writePromptQuery);) {

			writePromptStmt.setInt(1, groupId);
			writePromptStmt.setString(2, promptText);

			int numRowsUpdated = writePromptStmt.executeUpdate();
			if (numRowsUpdated == 0) {
				throw new EJDatabaseException(
						"Problem adding new prompt to database" 
								+ "\ngroup_id: " + groupId
								+ "\ntext: " + promptText);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createStudent(String firstName, String lastName,
			String username, String password) {
		
		final String createStudentQuery = 
				"INSERT INTO `user` (\r\n" + 
				"	unique_name,\r\n" + 
				"	password,\r\n" + 
				"	first_name,\r\n" + 
				"	last_name\r\n" + 
				") VALUES (\r\n" + 
				"	?,\r\n" + 
				"	?,\r\n" + 
				"	?,\r\n" + 
				"	?\r\n" + 
				");";
		
		try (Connection c = DB.getConnection();
				PreparedStatement createStudentStmt = c
						.prepareStatement(createStudentQuery);) {
			
			createStudentStmt.setString(1, username);
			createStudentStmt.setString(2, password);
			createStudentStmt.setString(3, firstName);
			createStudentStmt.setString(4, lastName);
			
			createStudentStmt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

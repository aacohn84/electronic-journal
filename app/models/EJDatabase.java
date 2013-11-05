package models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import play.db.DB;
import util.Pair;

public class EJDatabase {

	public static User getUser(int userId) {
		final String getUserCall = "{call GET_USER_BY_ID(?)}";
		User user = null;
		try (Connection c = DB.getConnection();
				CallableStatement getUserStmt = c.prepareCall(getUserCall)) {
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

	public static User getUser(String username) {
		final String getUserCall = "{call GET_USER_BY_NAME(?)}";
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
	public static void writePrompt(int creator, int groupId, String promptText)
			throws EJDatabaseException {

		final String writePromptCall = "{call WRITE_PROMPT(?,?,?)}";

		try (Connection c = DB.getConnection();
				CallableStatement writePromptStmt = c
						.prepareCall(writePromptCall);) {

			writePromptStmt.setInt(1, creator);
			writePromptStmt.setInt(2, groupId);
			writePromptStmt.setString(3, promptText);

			int numRowsUpdated = writePromptStmt.executeUpdate();
			if (numRowsUpdated == 0) {
				throw new EJDatabaseException(
						"Problem adding new prompt to database" + "\nuser_id: "
								+ creator + "\ngroup_id: " + groupId
								+ "\ntext: " + promptText);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void saveResponse(int responderId, int promptId,
			String responseText) throws EJDatabaseException {

		final String saveResponseCall = "{call SAVE_RESPONSE(?,?,?)}";

		try (Connection c = DB.getConnection();
				CallableStatement saveResponseStmt = c
						.prepareCall(saveResponseCall);) {

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
	 * Gets the most recent prompt for the group specified by <code>groupId</code>, and the response 
	 * of the user specified by <code>responderId</code>, if one exists.
	 * @param groupId - id of the group to fetch a prompt from
	 * @param responderId - id of the user whose response should be fetched
	 * @return a <code>Pair</code> containing <code>Prompt</code> and <code>Response</code>.
	 * Returns <code>null</code> if no results were found. <code>Response</code> is <code>null</code> 
	 * if no response exists.
	 */
	public static Pair<Prompt, Response> getMostRecentPromptAndResponse(int groupId,
			int responderId) {

		final String getMostRecentPromptAndResponseCall = "{call GET_MOST_RECENT_PROMPT_AND_RESPONSE(?,?)}";

		Pair<Prompt, Response> p = null;
		
		try (Connection c = DB.getConnection();
				CallableStatement getMostRecentPromptAndResponseStmt = c
						.prepareCall(getMostRecentPromptAndResponseCall);) {

			getMostRecentPromptAndResponseStmt.setInt(1, groupId);
			getMostRecentPromptAndResponseStmt.setInt(2, responderId);

			ResultSet rs = getMostRecentPromptAndResponseStmt.executeQuery();

			if (rs.first()) {
				printResultSetColumnNames(rs);
				p = new Pair<Prompt, Response>();
				p.setA(new Prompt(rs));
				if (rs.getString("prompt_response.text") != null) {
					p.setB(new Response(rs));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return p;
	}
	
	public static ArrayList<Group> getGroups(int userId) {
		final String getGroupsCall = "{call GET_GROUPS(?)}";

		ArrayList<Group> groups = new ArrayList<Group>();
		
		try (Connection c = DB.getConnection();
				CallableStatement getGroupsStmt = c.prepareCall(getGroupsCall);) {

			getGroupsStmt.setInt(1, userId);

			ResultSet rs = getGroupsStmt.executeQuery();

			while (rs.next()) {
				Group g = new Group(rs);
				groups.add(g);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return groups;
	}
	
	private static void printResultSetColumnNames(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i < columnCount; i++) {
			System.out.print(metaData.getColumnName(i) + ", ");
		}
		System.out.print(metaData.getColumnName(columnCount) + "\n");
	}
}

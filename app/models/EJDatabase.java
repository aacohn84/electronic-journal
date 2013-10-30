package models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import play.db.DB;

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

    public static WritingFeed getWritingFeed(int userId) {
	/*
	 * get all responses for user
	 * 	for each response
	 * 		get the original prompt
	 * 		get the comments
	 */
	WritingFeed writingFeed = new WritingFeed();
	
	final String getResponsesCall = "call {GET_RESPONSES(?)}";
	final String getPromptCall = "call {GET_PROMPT(?)}";
	final String getCommentsCall = "call {GET_COMMENTS(?)}";
	try (Connection c = DB.getConnection();
		CallableStatement getResponsesStmt = c.prepareCall(getResponsesCall);
		CallableStatement getPromptStmt = c.prepareCall(getPromptCall);
		CallableStatement getCommentsStmt = c.prepareCall(getCommentsCall);) {
	    
	    getResponsesStmt.setInt(1, userId);
	    ResultSet getResponsesResultSet = getResponsesStmt.executeQuery();
	    
	    while (getResponsesResultSet.next()) {
		// get the prompt
		final int promptId = getResponsesResultSet.getInt("id_prompt");
		getPromptStmt.setInt(1, promptId);
		ResultSet getPromptResultSet = getPromptStmt.executeQuery();
		
		Prompt prompt = null;
		if (getPromptResultSet.first()) {
		    prompt = new Prompt(getPromptResultSet);
		}
		
		// get the comments
		final int responseId = getResponsesResultSet.getInt("id_response");
		getCommentsStmt.setInt(1, responseId);
		ResultSet getCommentsResultSet = getCommentsStmt.executeQuery();
		
		ArrayList<Comment> comments = new ArrayList<Comment>();
		while (getCommentsResultSet.next()) {
		    Comment comment = new Comment(getCommentsResultSet);
		    comments.add(comment);
		}
		// build the response
		Response response = new Response(getResponsesResultSet);
		int responderId = getResponsesResultSet.getInt("id_responder");
		User responder = getUser(responderId); // TODO: Implement caching for users
		response.setResponder(responder); 
		response.setPrompt(prompt);
		response.setComments(comments);
		
		
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	return null;
    }

    /**
     * Saves a new prompt to the database
     * @param creator - id of the user creating the prompt
     * @param groupId - id of the group that can respond to this prompt
     * @param promptText - the prompt itself
     */
    public static void writePrompt(int creator, int groupId, String promptText) {
	final String writePromptCall = "call WRITE_PROMPT(?,?,?);";
	
	try (Connection c = DB.getConnection();
		CallableStatement writePromptStmt = c.prepareCall(writePromptCall);) {
	    
	    writePromptStmt.setInt(1, creator);
	    writePromptStmt.setInt(2, groupId);
	    writePromptStmt.setString(3, promptText);
	    
	    writePromptStmt.executeQuery();
	    
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }
}

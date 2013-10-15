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
		final int promptId = getResponsesResultSet.getInt("id_prompt");
		getPromptStmt.setInt(1, promptId);
		ResultSet getPromptResultSet = getPromptStmt.executeQuery();
		
		Prompt prompt = null;
		if (getPromptResultSet.first()) {
		    prompt = new Prompt(getPromptResultSet);
		}
		
		final int responseId = getResponsesResultSet.getInt("id_response");
		getCommentsStmt.setInt(1, responseId);
		ResultSet getCommentsResultSet = getCommentsStmt.executeQuery();
		
		ArrayList<Comment> comments = new ArrayList<Comment>();
		while (getCommentsResultSet.next()) {
		    Comment comment = new Comment(getCommentsResultSet);
		    comments.add(comment);
		}
		
		Response response = new Response(getResponsesResultSet);
		response.setResponder(responder); // TODO: Uhh, how do I do this? 
		response.setPrompt(prompt);
		response.setComments(comments);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	
	return null;
    }
}

package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Response {
	private int responderId;
	private int promptId;
	private String text;
	private Date creationDate;
	
	public Response(ResultSet rs) throws SQLException {
		responderId = rs.getInt("prompt_response.id_responder");
		promptId = rs.getInt("id_prompt");
		text = rs.getString("prompt_response.text");
		creationDate = rs.getDate("prompt_response.creation_date");
	}
	
	public int getResponder() { return responderId; }
	public int getint() { return promptId; }
	public String getText() { return text; }
	public Date getCreationDate() { return creationDate; }

	public void setResponder(int responder) { this.responderId = responder; }
	public void setint(int prompt) { this.promptId = prompt; }
	public void setText(String text) { this.text = text; }
	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
}

package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Response {
    // data members
    private User responder;
    private Prompt prompt;
    private String text;
    private Date creationDate;
    private ArrayList<Comment> comments;
    
    public Response(ResultSet rs) throws SQLException {
	text = rs.getString("text");
	creationDate = rs.getDate("creation_date");
    }
    
    // getters
    public User getResponder() { return responder; }
    public Prompt getPrompt() { return prompt; }
    public String getText() { return text; }
    public Date getCreationDate() { return creationDate; }
    public ArrayList<Comment> getComments() { return comments; }

    // setters
    public void setResponder(User responder) { this.responder = responder; }
    public void setPrompt(Prompt prompt) { this.prompt = prompt; }
    public void setText(String text) { this.text = text; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public void setComments(ArrayList<Comment> comments) { this.comments = comments; }
}

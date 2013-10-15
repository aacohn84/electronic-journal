package models;

import java.sql.ResultSet;

public class Comment {
    // data members
    private String text;
    private User commenter;

    public Comment(ResultSet rs) {
	// TODO Auto-generated constructor stub
    }
    // getters
    public String getText() { return text; }
    public User getCommenter() { return commenter; }
    
    // setters
    public void setText(String text) { this.text = text; }
    public void setCommenter(User commenter) { this.commenter = commenter; }
}

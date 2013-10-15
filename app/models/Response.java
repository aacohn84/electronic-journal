package models;

import java.util.ArrayList;

public class Response {
    // data members
    private String text;
    private ArrayList<Comment> comments;

    // getters
    public String getText() { return text; }
    public ArrayList<Comment> getComments() { return comments; }

    // setters
    public void setText(String text) { this.text = text; }
    public void setComments(ArrayList<Comment> comments) { this.comments = comments; }
}

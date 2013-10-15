package models;

public class Comment {
    // data members
    private String text;
    private User commenter;

    // getters
    public String getText() { return text; }
    public User getCommenter() { return commenter; }
    
    // setters
    public void setText(String text) { this.text = text; }
    public void setCommenter(User commenter) { this.commenter = commenter; }
}

package models;

import java.sql.ResultSet;

public class Prompt {
	private String text;
	private Group group;
	
	public Prompt(ResultSet rs) {
	    // TODO Auto-generated constructor stub
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
}

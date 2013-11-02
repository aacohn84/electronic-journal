package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Prompt {
	private int promptId;
	private int creatorId;
	private int groupId;
	private String text;
	private Date creationDate;

	public int getPromptId() { return promptId; }
	public int getCreator() { return creatorId; }
	public int getGroupId() { return groupId; }
	public String getText() { return text; }
	public Date getCreationDate() { return creationDate; }

	public void setPromptId(int promptId) { this.promptId = promptId; }
	public void setCreator(int creator) { this.creatorId = creator; }
	public void setGroupId(int groupId) { this.groupId = groupId; }
	public void setText(String text) { this.text = text; }
	public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

	public Prompt(ResultSet rs) throws SQLException {
		this.promptId = rs.getInt("id_prompt");
		this.creatorId = rs.getInt("id_creator");
		this.groupId = rs.getInt("id_group");
		this.text = rs.getString("text");
		this.creationDate = rs.getDate("creation_date");
	}
}

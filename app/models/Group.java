package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {
	private int groupId;
	private int teacherId;
	private int period;
	private String name;
	private String subject;
	
	public Group(ResultSet rs) throws SQLException {
		groupId = rs.getInt("id_group");
		teacherId = rs.getInt("group.id_teacher");
		period = rs.getInt("group.period");
		name = rs.getString("group.name");
		subject = rs.getString("group.subject");
	}
	
	public int getGroupId() { return groupId; }
	public int getTeacherId() { return teacherId; }
	public int getPeriod() { return period; }
	public String getName() { return name; }
	public String getSubject() { return subject; }
	
	public void setGroupId(int groupId) { this.groupId = groupId; }
	public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
	public void setPeriod(int period) { this.period = period; }
	public void setName(String name) { this.name = name; }
	public void setSubject(String subject) { this.subject = subject; }
}

package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import play.data.validation.Constraints.Required;

public class Group {
	private int groupId;
	private int teacherId;
	private int period;
	private String subject;
	
	public Group(ResultSet rs) throws SQLException {
		groupId = rs.getInt("id_group");
		teacherId = rs.getInt("group.id_teacher");
		period = rs.getInt("group.period");
		subject = rs.getString("group.subject");
	}
	
	public int getGroupId() { return groupId; }
	public int getTeacherId() { return teacherId; }
	public int getPeriod() { return period; }
	public String getSubject() { return subject; }
	
	public void setGroupId(int groupId) { this.groupId = groupId; }
	public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
	public void setPeriod(int period) { this.period = period; }
	public void setSubject(String subject) { this.subject = subject; }
	
	public static class Form {
		@Required private int period;
		@Required private String subject;
		private String passphrase;
		
		public int getPeriod() { return period; }
		public void setPeriod(int period) { this.period = period; }
		
		public String getSubject() { return subject; }
		public void setSubject(String subject) { this.subject = subject; }
		
		public String getPassphrase() { return passphrase; }
		public void setPassphrase(String passphrase) { this.passphrase = passphrase; }
	}
}

package models;

public class GroupManager {
    
    private static GroupManager instance;
    
    private Group[] groups;
    
    private GroupManager() {}
    
    public static GroupManager getInstance() {
	if (instance == null) {
	    instance = new GroupManager();
	    return instance;
	}
	return instance;
    }
}

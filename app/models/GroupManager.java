package models;

public class GroupManager extends RecordManager {
    
    private static GroupManager instance;
    
    private GroupManager() {}
    
    public static GroupManager getInstance() {
	if (instance == null) {
	    instance = new GroupManager();
	}
	return instance;
    }

    @Override
    protected Record fetchFromDB(Object key) {
	
	return null;
    }
}

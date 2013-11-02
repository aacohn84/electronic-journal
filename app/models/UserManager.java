package models;

public class UserManager extends RecordManager {

	private static UserManager instance;

	private UserManager() {}

	private static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	@Override
	protected Record fetchFromDB(Object key) {
		final int userId = (int) key;
		User user = EJDatabase.getUser(userId);
		return null;
	}

}

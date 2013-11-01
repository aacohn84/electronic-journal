package models;

public class EJDatabaseException extends Exception {

    private static final long serialVersionUID = 537778011954516604L;
    
    public EJDatabaseException() {
	super("Database error");
    }
    
    public EJDatabaseException(String message) {
	super(message);
    }
}

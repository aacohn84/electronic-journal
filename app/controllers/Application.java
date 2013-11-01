package controllers;

import models.EJDatabase;
import models.EJDatabaseException;
import models.Prompt;
import models.User;
import models.WritingFeed;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import static play.data.Form.form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
    
    final static Form<User.Form> loginForm = form(User.Form.class, User.Form.Login.class);
    final static Form<User.Form> signupForm = form(User.Form.class, User.Form.SignUp.class);

    public static Result index() {
        return ok(writePrompt.render(1));
    }
    
    /**
     * Submit new prompt
     */
    public static Result submitPrompt() {
	// parse POST request
	DynamicForm postData = form().bindFromRequest();
	String promptText = postData.get("promptText");
	String groupIdStr = postData.get("groupId");
	int groupId = Integer.parseInt(groupIdStr);
	/*String creatorStr = session().get("userId");
	int creator = Integer.parseInt(creatorStr);*/
	
	try {
	    EJDatabase.writePrompt(1, groupId, promptText);
	} catch (EJDatabaseException e) {
	    e.printStackTrace();
	}
	
	return ok();
    }
    
    public static Result showMostRecentPrompt() {
	String groupIdStr = session().get("currentGroupSelection");
	int groupId = Integer.parseInt(groupIdStr);
	String responderIdStr = session().get("userId");
	int responderId = Integer.parseInt(responderIdStr);
	
	Prompt p = EJDatabase.getMostRecentPromptAndResponse(groupId, responderId);
	
	return ok();
    }
    
    /**
     * Save response
     */
    public static Result saveResponse() {
	// parse POST request
	DynamicForm postData = form().bindFromRequest();
	String responseText = postData.get("responseText");
	String promptIdStr = postData.get("promptId");
	int promptId = Integer.parseInt(promptIdStr);
	/*String creatorStr = session().get("userId");
	int creator = Integer.parseInt(creatorStr);*/
	
	try {
	    EJDatabase.saveResponse(1, promptId, responseText);
	} catch (EJDatabaseException e) {
	    e.printStackTrace();
	}
	
	return ok();	
    }
    
    /**
     * Login page.
     */
    /*public static Result login() {
        return ok(
            login.render(loginForm)
        );
    }*/
    
    /**
     * Handle login form submission.
     */
    /*public static Result authenticate() {
        Form<User.Form> filledLoginForm = loginForm.bindFromRequest();
        
	if (filledLoginForm.hasErrors()) {
	    return badRequest(login.render(loginForm));
        } else {
            User.Form userInfo = filledLoginForm.get();
            String username = userInfo.getUsername();
            String password = userInfo.getPassword();
            
	    Logger.info("Authenticating user: " + username);
	    User user = User.authenticate(username, password);

	    if (user == null) {
		Logger.info(username + " failed to authenticate: bad password");
	    } else {
		Logger.info(username + " logged in.");
	    }
        }
    }*/

    /**
     * Logout and clean the session.
     */
    /*public static Result logout() {
    	Logger.info("Logging out user: " + session().get("username"));
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.login()
        );
    }*/
}

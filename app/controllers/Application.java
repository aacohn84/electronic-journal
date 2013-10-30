package controllers;

import models.EJDatabase;
import models.User;
import models.WritingFeed;
import play.*;
import play.data.Form;
import static play.data.Form.form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
    
    final static Form<User.Form> loginForm = form(User.Form.class, User.Form.Login.class);
    final static Form<User.Form> signupForm = form(User.Form.class, User.Form.SignUp.class);
    public static Result index() {
	String username = request().getQueryString("user");
	
	User user = EJDatabase.getUser(username);
	WritingFeed writingFeed = EJDatabase.getWritingFeed(user.getId());
	
        return ok(profile.render(null, null));
    }
    
    /**
     * Login page.
     */
    public static Result login() {
        return ok(
            login.render(loginForm)
        );
    }
    
    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
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
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
    	Logger.info("Logging out user: " + session().get("username"));
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
            routes.Application.login()
        );
    }
}

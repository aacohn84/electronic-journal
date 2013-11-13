package controllers;

import java.util.Map.Entry;

import models.User;
import play.*;
import play.data.Form;
import static play.data.Form.form;
import play.mvc.*;
import play.mvc.Http.Session;
import views.html.*;

public class Application extends Controller {

	final static Form<User.Form> loginForm = form(User.Form.class,
			User.Form.Login.class);
	final static Form<User.Form> signupForm = form(User.Form.class,
			User.Form.SignUp.class);

	public static Result index() {
		return redirect("/login");
	}
	
	/**
	 * Login page.
	 */
	 public static Result login() {
		 // authenticate the user
		 // decide which 
		 return ok(login.render());
	 }
	 
	 public static Result logout() {
		 System.out.println(session().get("username") + " logged out");
		 session().clear();
		 return ok(login.render());
	 }

	/**
	 * Handle login form submission.
	 */
	public static Result authenticate() {
		Form<User.Form> filledLoginForm = loginForm.bindFromRequest();

		if (filledLoginForm.hasErrors()) {
			return badRequest(login.render());
		}
		
		User.Form userInfo = filledLoginForm.get();
		String username = userInfo.getUsername();
		String password = userInfo.getPassword();

		Logger.info("Authenticating user: " + username);
		User user = User.authenticate(username, password);

		if (user == null) {
			Logger.info(username + " failed to authenticate: bad password");
			return redirect("/login");
		} else {
			session().put("username", username);
			session().put("userId", String.valueOf(user.getId()));
			Session s = session();
			for (Entry<String, String> e : s.entrySet()) {
				System.out.println(e.getKey() + ", " + e.getValue());
			}
			Logger.info(username + " logged in.");
			return redirect("/classes");
		}
	}
}

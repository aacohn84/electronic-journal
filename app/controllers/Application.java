package controllers;

import models.EJDatabase;
import models.User;
import play.*;
import play.data.Form;
import static play.data.Form.form;
import play.mvc.*;
import views.html.*;

public class Application extends BaseController {

	final static Form<User.Form> loginForm = form(User.Form.class,
			User.Form.Login.class);
	
	/**
	 * Verify user name and password
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
			printSession();
			Logger.info(username + " logged in.");
			return redirect(routes.General.showClasses());
		}
	}
	
	/**
	 * Display login page.
	 */
	 public static Result login() {
		 return ok(login.render());
	 }

	 /**
	  * Clear the session and display the login page.
	  * @return
	  */
	public static Result logout() {
		 System.out.println(session().get("username") + " logged out");
		 session().clear();
		 return redirect(routes.Application.login());
	 }

	final static Form<User.Form> signupForm = form(User.Form.class,
			User.Form.SignUp.class);

	public static Result createAccount() {
		Form<User.Form> filledSignUpForm = signupForm.bindFromRequest();
		
		String password = filledSignUpForm.field("password").value();
		String passwordRetype = filledSignUpForm.field("passwordRetype").value();
		
		if (password == null || passwordRetype == null
				|| !(password.equals(passwordRetype))) {
			
			filledSignUpForm.reject("password", "Passwords do not match");
		}
		
		if (filledSignUpForm.hasErrors()) {
			return badRequest(newStudent.render(filledSignUpForm));
		}
		
		User.Form concreteSignUpForm = filledSignUpForm.get();
		String firstName = concreteSignUpForm.getFirstName();
		String lastName = concreteSignUpForm.getLastName();
		String username = concreteSignUpForm.getUsername();
		
		EJDatabase.createStudent(firstName, lastName, username, password);
		
		return redirect(routes.Application.login());
	}
	
	public static Result index() {
		return redirect("/login");
	}
	 
	 public static Result showAccountCreationForm() {
		return ok(newStudent.render(signupForm));
	}
}

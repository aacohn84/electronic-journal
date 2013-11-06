package controllers;

import java.util.ArrayList;

import models.EJDatabase;
import models.EJDatabaseException;
import models.Group;
import models.Prompt;
import models.Response;
import models.User;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import static play.data.Form.form;
import play.mvc.*;
import util.Pair;
import views.html.*;

public class Application extends Controller {

	final static Form<User.Form> loginForm = form(User.Form.class,
			User.Form.Login.class);
	final static Form<User.Form> signupForm = form(User.Form.class,
			User.Form.SignUp.class);

	public static Result index() {
		return redirect("/login");
	}

	public static Result writePrompt() {
		DynamicForm requestData = form().bindFromRequest();
		String groupIdStr = requestData.get("groupId");
		int groupId = Integer.parseInt(groupIdStr);
		
		return ok(writePrompt.render(groupId));
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
		/*
		 * String creatorStr = session().get("userId"); int creator =
		 * Integer.parseInt(creatorStr);
		 */

		try {
			EJDatabase.writePrompt(groupId, promptText);
		} catch (EJDatabaseException e) {
			e.printStackTrace();
		}

		return ok();
	}

	public static Result showMostRecentPrompt() {
		DynamicForm requestData = form().bindFromRequest();
		String groupIdStr = requestData.get("groupId");
		int groupId = Integer.parseInt(groupIdStr);
		/*String responderIdStr = session().get("userId");
		int responderId = Integer.parseInt(responderIdStr);*/

		Pair<Prompt, Response> mostRecentPromptAndResponse = EJDatabase
				.getMostRecentPromptAndResponse(groupId, 2);
		
		if (mostRecentPromptAndResponse == null) {
			return ok("No prompts exist for this class yet.");
		}
		
		Prompt p = mostRecentPromptAndResponse.getA();
		Response r = mostRecentPromptAndResponse.getB();

		return ok(respond.render(p, r));
	}

	/**
	 * Save response
	 */
	public static Result saveResponse() {
		// parse POST request
		DynamicForm requestData = form().bindFromRequest();
		String responseText = requestData.get("responseText");
		String promptIdStr = requestData.get("promptId");
		String groupIdStr = requestData.get("groupId");
		
		int promptId = Integer.parseInt(promptIdStr);
		/*
		 * String creatorStr = session().get("userId"); int creator =
		 * Integer.parseInt(creatorStr);
		 */

		try {
			EJDatabase.saveResponse(2, promptId, responseText);
		} catch (EJDatabaseException e) {
			e.printStackTrace();
		}

		return redirect("/mostRecentPrompt?groupId=" + groupIdStr);
	}
	
	public static Result showClasses() {
		ArrayList<Group> groups = null;
		Call action = null;
		if (EJDatabase.isTeacher(1)) {
			groups = EJDatabase.getGroupsTaughtByUser(1);
			action = routes.Application.writePrompt();
		} else {
			groups = EJDatabase.getGroups(1);
			action = routes.Application.showMostRecentPrompt();
		}
		return ok(showClasses.render(groups, action));
	}
	
	/**
	 * Login page.
	 */
	 public static Result login() {
		 // authenticate the user
		 // decide which 
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
			Logger.info(username + " logged in.");
			return redirect("/classes");
		}
	}
	 

	/**
	 * Logout and clean the session.
	 */
	/*
	 * public static Result logout() { Logger.info("Logging out user: " +
	 * session().get("username")); session().clear(); flash("success",
	 * "You've been logged out"); return redirect( routes.Application.login() );
	 * }
	 */
}

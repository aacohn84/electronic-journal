package controllers;

import java.util.ArrayList;

import models.EJDatabase;
import models.EJDatabaseException;
import models.Group;
import models.Prompt;
import models.Response;
import models.User;
import models.WritingFeed;
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
		/*
		 * String creatorStr = session().get("userId"); int creator =
		 * Integer.parseInt(creatorStr);
		 */

		try {
			EJDatabase.writePrompt(1, groupId, promptText);
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
		ArrayList<Group> groups = EJDatabase.getGroups(2);
		return ok(showClasses.render(groups));
	}
	
	public static Result chooseClass() {
		String groupIdStr = request().getQueryString("groupId");
		int groupId = Integer.parseInt(groupIdStr);
		
		return null;
	}

	/**
	 * Login page.
	 */
	/*
	 * public static Result login() { return ok( login.render(loginForm) ); }
	 */

	/**
	 * Handle login form submission.
	 */
	/*
	 * public static Result authenticate() { Form<User.Form> filledLoginForm =
	 * loginForm.bindFromRequest();
	 * 
	 * if (filledLoginForm.hasErrors()) { return
	 * badRequest(login.render(loginForm)); } else { User.Form userInfo =
	 * filledLoginForm.get(); String username = userInfo.getUsername(); String
	 * password = userInfo.getPassword();
	 * 
	 * Logger.info("Authenticating user: " + username); User user =
	 * User.authenticate(username, password);
	 * 
	 * if (user == null) { Logger.info(username +
	 * " failed to authenticate: bad password"); } else { Logger.info(username +
	 * " logged in."); } } }
	 */

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

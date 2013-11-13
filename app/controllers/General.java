package controllers;

import java.util.ArrayList;

import models.EJDatabase;
import models.Group;
import play.mvc.Call;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.showClasses;

public class General extends SecuredController {
	
	public static Result showClasses() {
		ArrayList<Group> groups = null;
		Call action = null;
		String userIdStr = session().get("userId");
		int userId = Integer.parseInt(userIdStr);
		if (EJDatabase.isTeacher(userId)) {
			groups = EJDatabase.getGroupsTaughtByUser(userId);
			action = routes.Teacher.writePrompt();
		} else {
			groups = EJDatabase.getGroups(userId);
			action = routes.Student.showMostRecentPrompt();
		}
		return ok(showClasses.render(groups, action));
	}
	
	/**
	 * Given a prompt ID and user ID, shows the corresponding prompt and
	 * response
	 * 
	 * @return
	 */
	public static Result viewPrompt() {
		return null;
	}

}

package controllers;

import static play.data.Form.form;
import models.EJDatabase;
import models.EJDatabaseException;
import play.data.DynamicForm;
import play.mvc.Result;
import views.html.writePrompt;

public class Teacher extends SecuredController {

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

		try {
			EJDatabase.writePrompt(groupId, promptText);
		} catch (EJDatabaseException e) {
			e.printStackTrace();
		}

		return ok();
	}
}

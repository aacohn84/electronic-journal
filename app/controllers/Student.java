package controllers;

import static play.data.Form.form;
import models.EJDatabase;
import models.EJDatabaseException;
import models.Prompt;
import models.Response;
import play.data.DynamicForm;
import play.mvc.Result;
import util.Pair;
import views.html.student.respond;
import views.html.student.addClass;

public class Student extends SecuredController {
	
	public static Result showMostRecentPrompt() {
		DynamicForm requestData = form().bindFromRequest();
		String groupIdStr = requestData.get("groupId");
		int groupId = Integer.parseInt(groupIdStr);
		String responderIdStr = session().get("userId");
		int responderId = Integer.parseInt(responderIdStr);

		Pair<Prompt, Response> mostRecentPromptAndResponse = EJDatabase
				.getMostRecentPromptAndResponse(groupId, responderId);
		
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

		String creatorIdStr = session().get("userId");
		int creatorId = Integer.parseInt(creatorIdStr);

		try {
			EJDatabase.saveResponse(creatorId, promptId, responseText);
		} catch (EJDatabaseException e) {
			e.printStackTrace();
		}

		return redirect(routes.Student.showMostRecentPrompt().toString()
				+ "?groupId=" + groupIdStr);
	}
	
	public static Result showClassAdditionForm() {
		return ok(addClass.render());
	}
	
	public static Result addClass() {
		DynamicForm requestData = form().bindFromRequest();
		String passphrase = requestData.get("passphrase");
		
		int studentId = getUserIdFromSession();
		
		EJDatabase.addStudentToClass(studentId, passphrase);
		
		return redirect(routes.General.showClasses());
	}
}

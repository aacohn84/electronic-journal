package controllers;

import static play.data.Form.form;
import models.EJDatabase;
import models.EJDatabaseException;
import models.Group;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Result;
import views.html.teacher.writePrompt;
import views.html.teacher.addClass;

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

		return redirect("/classes");
	}

	final static Form<Group.Form> createClassForm = form(Group.Form.class);

	/**
	 * Shows a blank form for creating a new class
	 */
	public static Result showClassCreationForm() {
		return ok(addClass.render(createClassForm));
	}

	/**
	 * Attempts to create a new class from the submitted form
	 * 
	 * @return Teacher's course list if there are no errors in the form fields,
	 *         else returns the user to the course creation form.
	 */
	public static Result createClass() {
		Form<Group.Form> filledForm = createClassForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			return badRequest(addClass.render(filledForm));
		}

		// get the value of each property for the new course
		Group.Form concreteForm = filledForm.get();
		int period = concreteForm.getPeriod();
		String subject = concreteForm.getSubject();
		String passphrase = concreteForm.getPassphrase();
		int teacherId = getUserIdFromSession();

		// create the new course
		EJDatabase.createGroup(teacherId, period, subject, passphrase);

		// show the course list
		return General.showClasses();
	}
}

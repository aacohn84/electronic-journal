package controllers;

import java.util.List;

import models.EJDatabase;
import models.Group;
import play.mvc.Call;
import play.mvc.Result;
import views.html.showClasses;

public class General extends SecuredController {

	public static Result showClasses() {

		int userId = getUserIdFromSession();
		List<Group> groups = null;
		ShowClassesActions actions = new ShowClassesActions();
		if (EJDatabase.isTeacher(userId)) {
			groups = EJDatabase.getGroupsTaughtByUser(userId);
			actions.setChooseClass(routes.Teacher.writePrompt());
			actions.setAddClass(routes.Teacher.showClassCreationForm());
		} else {
			groups = EJDatabase.getGroups(userId);
			actions.setChooseClass(routes.Student.showMostRecentPrompt());
			actions.setAddClass(routes.Student.showClassAdditionForm());
		}

		return ok(showClasses.render(groups, actions));
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

	public static class ShowClassesActions {
		private Call chooseClass;
		private Call addClass;

		public Call getChooseClass() { return chooseClass; }
		public Call getAddClass() { return addClass; }
		public void setChooseClass(Call chooseClass) { this.chooseClass = chooseClass; }
		public void setAddClass(Call addClass) { this.addClass = addClass; }
	}
}

package controllers;

import play.mvc.Controller;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class SecuredController extends BaseController {
	
	public static String getUsername() {
		return session().get("username");
	}
	
	public static int getUserIdFromSession() {
		String idStr = session().get("userId");
		return Integer.parseInt(idStr);
	}
}

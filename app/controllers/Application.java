package controllers;

import models.EJDatabase;
import models.User;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
	String username = request().getQueryString("user");
	
	User user = EJDatabase.getUser(username);
	
        return ok(profile.render(null, null));
    }
  
}

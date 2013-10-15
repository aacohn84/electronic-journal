package controllers;

import models.EJDatabase;
import models.User;
import models.WritingFeed;
import play.*;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
	String username = request().getQueryString("user");
	
	User user = EJDatabase.getUser(username);
	WritingFeed writingFeed = EJDatabase.getWritingFeed(user.getId());
	
        return ok(profile.render(null, null));
    }
  
}

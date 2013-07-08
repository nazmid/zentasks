package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.Context;
import play.data.*;
import static play.data.Form.*;
import java.util.*;
import models.*;
import views.html.*;
import views.html.projects.*;

@Security.Authenticated(Secured.class)
public class Projects extends Controller {

	public static Result add() {
		Project newProject = Project.create("New project", form()
				.bindFromRequest().get("group"), request().username());
		return ok(item.render(newProject));
	}

	
	
	public static Result rename(Long project) {
	    if (Secured.isMemberOf(project)) {
	        return ok(
	            Project.rename(
	                project,
	                form().bindFromRequest().get("name")
	            )
	        );
	    } else {
	        return forbidden();
	    }
	}
	
	public static Result delete(Long project) {
	    if(Secured.isMemberOf(project)) {
	        Project.find.ref(project).delete();
	        return ok();
	    } else {
	        return forbidden();
	    }
	}
	
	public static Result addGroup() {
	    return ok(
	        group.render("New group", new ArrayList<Project>())
	    );
	}
}

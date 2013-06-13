import java.util.List;

import models.User;
import play.Application;
import play.GlobalSettings;
import play.db.ebean.Model;
import play.libs.Yaml;

import com.avaje.ebean.Ebean;


public class Global extends GlobalSettings{

	@Override
	public void onStart(Application app) {
		 // Check if the database is empty
        if (User.find.findRowCount() == 0) {
            Ebean.save((List<Model>) Yaml.load("initial-data.yml"));
        }
	}
}

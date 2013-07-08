package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import models.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class ProjectsTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }

    @Test
    public void newProject() {
        Result result = callAction(
            controllers.routes.ref.Projects.add(),
            fakeRequest().withSession("email", "bob@example.com")
                .withFormUrlEncodedBody(ImmutableMap.of("group", "Some Group"))
        );
        assertEquals(200, status(result));
        Project project = Project.find.where()
            .eq("folder", "Some Group").findUnique();
        assertNotNull(project);
        assertEquals("New project", project.name);
        assertEquals(1, project.members.size());
        assertEquals("bob@example.com", project.members.get(0).email);
    }
    
    @Test
    public void renameProject() {
        long id = Project.find.where()
            .eq("members.email", "bob@example.com")
            .eq("name", "Private").findUnique().id;
        Result result = callAction(
            controllers.routes.ref.Projects.rename(id),
            fakeRequest().withSession("email", "bob@example.com")
                .withFormUrlEncodedBody(ImmutableMap.of("name", "New name"))
        );
        assertEquals(200, status(result));
        assertEquals("New name", Project.find.byId(id).name);
    }
    
    @Test
    public void renameProjectForbidden() {
        long id = Project.find.where()
            .eq("members.email", "bob@example.com")
            .eq("name", "Private").findUnique().id;
        Result result = callAction(
            controllers.routes.ref.Projects.rename(id),
            fakeRequest().withSession("email", "jeff@example.com")
                .withFormUrlEncodedBody(ImmutableMap.of("name", "New name"))
        );
        assertEquals(403, status(result));
        assertEquals("Private", Project.find.byId(id).name);
    }
}
package controllers;

import java.util.List;

import models.ApplicationRole;
import models.Availability;
import models.Instructor;
import models.Lang;
import models.Location;
import models.User;
import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import play.data.validation.Valid;
import play.libs.Codec;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(Deadbolt.class)
@Restrictions({ @Restrict("instructor"), @Restrict("admin"), @Restrict("editor") })
public class AdminInstructor extends Controller {

	@Before
	static void setConnectedUser() {
		if (Security.isConnected()) {
			User user = User.find("byEmail", Security.connected()).first();
			renderArgs.put("user", user);
			renderArgs.put("tab1", "active");
			renderArgs.put("tab2", "passive");
			renderArgs.put("tab3", "passive");
			// if (user.roles.contains("instructor")) {
			Instructor instructor = Instructor.findById(user.id);
			List<Lang> languages = Lang.findAll();
			renderArgs.put("languages", languages);
			renderArgs.put("locations", Location.findAll());
			renderArgs.put("instructor", instructor);
			// }
		}
	}

	@Restrictions({ @Restrict("instructor"), @Restrict("editor"), @Restrict("admin") })
	public static void instructor() {
		render();
	}

	@Restrictions({ @Restrict("instructor") })
	public static void availabilities() {
		List<Availability> availabilities = Availability.find("instructor.email", Security.connected()).fetch();
		render(availabilities);
	}

	@Restrictions({ @Restrict("editor"), @Restrict("admin") })
	public static void timetable() {
		render();
	}

	@Restrictions({ @Restrict("instructor"), @Restrict("editor"), @Restrict("admin") })
	public static void updateUser(User user) {
		User updatedUser = User.find("byEmail", Security.connected()).first();
		updatedUser.firstname = user.firstname;
		updatedUser.surname = user.surname;
		updatedUser.phone = user.phone;
		updatedUser.email = user.email;
		validation.valid(updatedUser);
		renderArgs.put("tab1", "active");
		renderArgs.put("tab2", "passive");
		renderArgs.put("tab3", "passive");
		if (validation.hasErrors()) {
			render("@instructor", updatedUser);
		}
		updatedUser.save();
		availabilities();
	}

	@Restrict("instructor")
	public static void updateInstructor(Instructor instructor) {
		Instructor updatedInstructor = Instructor.find("byEmail", Security.connected()).first();
		updatedInstructor.positionSki = instructor.positionSki;
		updatedInstructor.positionSnowboard = instructor.positionSnowboard;
		updatedInstructor.languages = instructor.languages;
		validation.valid(updatedInstructor);
		renderArgs.put("tab1", "passive");
		renderArgs.put("tab2", "active");
		renderArgs.put("tab3", "passive");
		if (validation.hasErrors()) {
			render("@instructor", updatedInstructor);
		}
		updatedInstructor.save();
		availabilities();
	}

	@Restrictions({ @Restrict("instructor"), @Restrict("editor"), @Restrict("admin") })
	public static void updatePassword(String oldPassword, String newPassword, String passwordConfirm) {
		User updatedUser = User.find("byEmail", Security.connected()).first();
		validation.equals(Codec.hexSHA1(oldPassword), updatedUser.password).message("oldPasswordDiff");
		validation.equals(passwordConfirm, newPassword).message("newPasswordDiff");
		updatedUser.password = newPassword;
		validation.valid(updatedUser);
		renderArgs.put("tab1", "passive");
		renderArgs.put("tab2", "passive");
		renderArgs.put("tab3", "active");
		if (validation.hasErrors()) {
			render("@instructor", updatedUser, newPassword, passwordConfirm, oldPassword);
		}
		updatedUser.save();
		availabilities();
	}

}

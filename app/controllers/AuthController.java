package controllers;

import authority.Secured;
import models.Account;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import pojos.Credentials;
import services.DBFilter;
import services.DBService;
import views.html.login;

import javax.inject.Inject;

/**
 * @author seyi
 */

@Transactional
public class AuthController extends Controller {
	private final DBService db;
	private final FormFactory formFactory;
	private static Form<Credentials> loginForm;

	@Inject
	public AuthController(FormFactory formFactory, DBService db) {
		this.formFactory = formFactory;
		this.db = db;
		loginForm = this.formFactory.form(Credentials.class);
	}

    public Result index() {
		if(Auth.isLogin()) {
			return redirect(routes.IndexController.index());
		}
		return ok(login.render(loginForm));
    }

	public Result login() {
		Form<Credentials> filledForm = loginForm.bindFromRequest();
		if (filledForm.hasErrors()) {
            flash("message", "Invalid email/password combination.");
            return badRequest(login.render(filledForm));
		} else {
			String email = filledForm.get().getEmail().trim();
			String password = filledForm.get().getPassword().trim();

			DBFilter filter = DBFilter.get();
			filter.field("email", email);
			
			Account account = db.findOne(Account.class, filter);
			if(account != null && password.equals(account.getPassword())) {
				if(!account.isActive()) {
					filledForm.reject("Account is not active, please contact the administrator.");
					return badRequest(login.render(filledForm));
				}
				Auth.setAuth(account);
			} else {
				filledForm.reject("Invalid email/password combination.");
				return badRequest(login.render(filledForm));
			}

			String lastUrl = session().get(Secured.LAST_URL);
			if(Utility.isNotEmpty(lastUrl)) {
				return redirect(lastUrl);
			}
			return redirect(routes.IndexController.index());
		}
	}

	public Result logout() {
		session().clear();
		flash("message", "You've been logged out");
		return redirect(routes.AuthController.index());
	}
}

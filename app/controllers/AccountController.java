package controllers;

import authority.Secured;
import models.Account;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import pojos.Param;
import services.DBFilter;
import services.DBService;
import views.html.*;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

/**
 * @author seyi
 */

@Security.Authenticated(Secured.class)
@Transactional
public class AccountController extends Controller {

    private final DBService db;
    private final FormFactory formFactory;

    @Inject
    public AccountController(FormFactory formFactory, DBService db) {
        this.formFactory = formFactory;
        this.db = db;
    }

    public Result single(Long id) {
        Account account = db.findOne(Account.class, id);
        List<Account> list = Arrays.asList(account);
        return ok(accounts.render(list, 1L));
    }

    public Result index() {
        Param param = Utility.getParam();
        param.setSort("id DESC");

        DBFilter filter = DBFilter.get();

        String search = request().getQueryString("search");
        if(Utility.isNotEmpty(search)) {
            filter.brS()
                    .field("name").like(search)
                    .or().field("email").like(search)
                    .brE();
        }

        List<Account> list = db.find(Account.class, filter, param);
        Long total = db.count(Account.class, filter);

        return ok(accounts.render(list, total));
    }

    public Result add() {
        return ok(accountForm.render(this.formFactory.form(Account.class), new Account()));
    }

    public Result create() {
        Form<Account> filledForm = this.formFactory.form(Account.class).bindFromRequest();
        validate(filledForm, new Account());

        if (filledForm.hasErrors()) {
            return badRequest(accountForm.render(filledForm, new Account()));
        } else {
            Account created = new Account();
            fill(filledForm, created);
            created.setActive(true);
            db.save(created);
            return redirect(routes.AccountController.single(created.getId()));
        }
    }

    public Result edit(Long id) {
        Account account = db.findOne(Account.class, id);
        Form<Account> filledForm = this.formFactory.form(Account.class).fill(account);
        return ok(accountForm.render(filledForm, account));
    }

    public Result update(Long id) {
        Account account = db.findOne(Account.class, id);
        Form<Account> filledForm = this.formFactory.form(Account.class).bindFromRequest();
        validate(filledForm, account);
        if (filledForm.hasErrors()) {
            return badRequest(accountForm.render(filledForm, account));
        } else {
            fill(filledForm, account);
            db.save(account);
            return redirect(routes.AccountController.single(account.getId()));
        }
    }

    public Result activate(Long id) {
        Account account = db.findOne(Account.class, id);
        account.setActive(!account.isActive());
        db.save(account);
        return redirect(routes.AccountController.single(account.getId()));
    }

    private void validate(Form<Account> filledForm, Account account) {
        String email = filledForm.field("email").valueOr("").toLowerCase().trim();
        String password = filledForm.field("password").valueOr("");

        if(account.getId() == null) {
            if(Utility.isEmpty(password)) {
                filledForm.reject("password", "Enter a password");
            }
            if(password.length() < 6) {
                filledForm.reject("password", "Your password must be at least 6 characters");
            }
        }

        // Check confirmed password
        if (Utility.isNotEmpty(password)) {
            if (!password.equals(filledForm.field("confirmPass").value())) {
                filledForm.reject("confirmPass", "Passwords don't match");
            }
        }

        Account existingAccount = db.findOne(Account.class, "email", email);
        if(existingAccount != null && !existingAccount.getId().equals(account.getId())) {
            filledForm.reject("email", "This email is already registered");
        }
    }

    private void fill(Form<Account> filledForm, Account account) {
        Account form = filledForm.get();
        account.setEmail(form.getEmail());
        account.setName(form.getName());
        if(Utility.isNotEmpty(form.getPassword())) {
            account.setPassword(form.getPassword());
        }
    }
}
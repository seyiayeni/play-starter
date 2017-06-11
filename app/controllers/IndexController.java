package controllers;

import authority.Secured;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.mvc.*;
import services.*;

import javax.inject.Inject;

import views.html.index;

/**
 * @author seyi
 */

@Security.Authenticated(Secured.class)
@Transactional
public class IndexController extends Controller {
    private final DBService db;
    private final FormFactory formFactory;

    @Inject
    public IndexController(FormFactory formFactory, DBService db) {
        this.formFactory = formFactory;
        this.db = db;
    }

    public Result index() {
        return ok(index.render());
    }

    public Result untrail(String path) {
        return movedPermanently("/" + path);
    }
}

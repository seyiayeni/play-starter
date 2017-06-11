package authority;

import controllers.Auth;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * @author seyi
 */

public class Secured extends Security.Authenticator {
    public static final String LAST_URL = "lastUrl";
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get(Auth.ACCOUNT);
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
    	ctx.session().put(LAST_URL, ctx.request().uri());
        return redirect(controllers.routes.AuthController.index());
    }   
}
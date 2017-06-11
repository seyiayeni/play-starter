package authority;

import pojos.Credentials;
import org.apache.commons.codec.binary.Base64;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class SecuredApi extends Security.Authenticator {
    @Override
    public String getUsername(Context ctx) {
        String[] authHeaders = ctx.request().headers().get("Authorization");
        Credentials credentials = credentials(authHeaders);
        if(credentials != null) {
            //do db credentials check here - return null if credentials not correct
            return credentials.getEmail();
        }
        return null;
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
        ctx.response().setHeader("WWW-Authenticate", "Basic");
        return unauthorized();
    }

    private Credentials credentials(String[] authHeaders) {
        if ((authHeaders != null) && (authHeaders.length == 1) && (authHeaders[0] != null)) {
            String auth = authHeaders[0].substring(6);
            try {
                byte[] decodedAuth = new Base64().decode(auth);
                String[] credentials = new String(decodedAuth, "UTF-8").split(":");
                String username = credentials[0];
                String password = credentials[1];
                return new Credentials(username, password);
            } catch (Exception e) {}
        }
        return null;
    }
}
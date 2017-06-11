package controllers;

import authority.SecuredApi;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import models.Model;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.DBService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author seyi
 */

@Security.Authenticated(SecuredApi.class)
@Transactional
public class ApiController extends Controller {
    private final DBService db;

    @Inject
    public ApiController(DBService db) {
        this.db = db;
    }

    public Result sendModels() {
        List<Map<String, Object>> models = new ArrayList<>();
        JsonNode json = Json.toJson(models);
        return ok(json);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result updateModel() {
        JsonNode json = request().body().asJson();

        String j = json.toString();
        Gson gson = new Gson();

        Model model = gson.fromJson(j, Model.class);

        //update here

        return ok("Successful");
    }
}

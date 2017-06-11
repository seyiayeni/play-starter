package controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import play.data.validation.Constraints;
import play.mvc.Http;
import pojos.Param;

import java.lang.reflect.Field;

/**
 * Created by seyi on 1/26/15.
 */
public class Utility {

    public static boolean isNotEmpty(String text) {
        return StringUtils.isNotEmpty(text);
    }
    public static boolean isEmpty(String text) {
        return StringUtils.isEmpty(text);
    }
    public static boolean isNumeric(String text) {
        return NumberUtils.isNumber(text);
    }

    public static boolean isEmailValid(String email) {
        return new Constraints.EmailValidator().isValid(email);
    }

    public static Param getParam() {
        return getParam(50);
    }

    public static Param getParam(int lim) {
        int page = 0;
        try {
            String p = getQuery("page");
            if(StringUtils.isNumeric(p))
                page = Integer.valueOf(p);
        } catch(Exception e)  {}

        int limit = lim;
        try {
            String l = getQuery("limit");
            if(StringUtils.isNumeric(l))
                limit = Integer.valueOf(l);
        } catch(Exception e)  {}

        Param param = new Param(page, limit);
        String sort = getQuery("sort");
        if(Utility.isNotEmpty(sort)) {
            param.setSort(sort);
        }

        return param;
    }

    public static String getQuery(String query) { return Http.Context.current().request().getQueryString(query); }

    public static String getUri() { return Http.Context.current().request().uri(); }
    public static String getUriQuery() {
        String uri = getUri();
        String[] arr = uri.split("\\?");
        if(arr.length>1) return arr[1];
        return "";
    }

    public static String capitalize(String input) {
        if(input == null) return "";
        input = input.trim().replaceAll("_+", " ").replaceAll("-+", " ").replaceAll(" +", " ");
        return WordUtils.capitalizeFully(input);
    }

    public static Object field(Object obj, String name) {
        try {
            Field field = obj.getClass().getField(name);
            return field.get(obj);
        }catch (Exception e) {
            return null;
        }
    }

    public static String  getPagedUrl(String uri,  int index) {
        String url = uri.replaceAll("\\&page=\\d*", "").replaceAll("page=\\d*", "");
        if(index > 0) {
            if (url.contains("?")) {
                url = url + "&page=" + index;
            } else {
                url = url + "?page=" + index;
            }
        }
        url = url.replaceFirst("\\?\\&", "?");
        if(url.endsWith("?")) url = url.substring(0, url.length() - 1);
        return url;
    }
}
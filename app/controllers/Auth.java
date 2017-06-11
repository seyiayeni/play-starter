package controllers;

import models.Permission;
import models.Account;
import play.mvc.Http;
import services.CacheService;
import services.DBService;

import java.util.Set;

/**
 * @author seyi
 */

public class Auth {
    
    public static final String ACCOUNT = "account";

    public static Account getAuth() {
        String id = Http.Context.current().session().get(ACCOUNT);
        if(id != null) {
            Account account = (Account) CacheService.C.get(ACCOUNT + id);
            if (account == null) {
                account =  DBService.Q.findOne(Account.class, id);
                if (account != null) {
                    CacheService.C.set(ACCOUNT+account.getId(), account);
                }
            }
            return account;
        }
        return null;
    }

    public static void setAuth(Account account) {
        Http.Context.current().session().put(ACCOUNT, account.getId().toString());
        CacheService.C.set(ACCOUNT+account.getId(), account);
    }
    
    public static boolean isLogin() {
        return getAuth() != null;
    }

    public static boolean hasPerm(Permission perm) {
        if(isLogin()) {
            Account account = getAuth();
            Set<Permission> permissions = account.permissions();
            if(permissions.contains(Permission.SUPER)) {
                return true;
            }
            return permissions.contains(perm);
        }
        return false;
    }
    
    public static boolean hasAnyPerm(Permission... perms) {
        if(isLogin()) {
            Account account = getAuth();
            Set<Permission> permissions = account.permissions();
            if(permissions.contains(Permission.SUPER)) {
                return true;
            }
            for (Permission perm : perms) {
                if (permissions.contains(perm)) {
                    return true;
                }
            }
        }
        return false;
    }
}

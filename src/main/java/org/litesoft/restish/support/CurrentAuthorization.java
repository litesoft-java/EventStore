package org.litesoft.restish.support;

public class CurrentAuthorization {
    private static final ThreadLocal<AuthorizePair> CURRENT = new ThreadLocal<>();

    public static void set(AuthorizePair pair) {
        CURRENT.set(pair);
    }

    public static AuthorizePair get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}

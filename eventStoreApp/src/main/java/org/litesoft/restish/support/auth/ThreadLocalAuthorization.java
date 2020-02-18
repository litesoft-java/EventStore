package org.litesoft.restish.support.auth;

public class ThreadLocalAuthorization implements Authorization {
    private final ThreadLocal<AuthorizePair> mThreadLocalAuthorizePair = new ThreadLocal<>();

    @Override
    public void set( AuthorizePair pair ) {
        mThreadLocalAuthorizePair.set( pair );
    }

    @Override
    public AuthorizePair get() {
        return mThreadLocalAuthorizePair.get();
    }

    @Override
    public void clear() {
        mThreadLocalAuthorizePair.remove();
    }
}

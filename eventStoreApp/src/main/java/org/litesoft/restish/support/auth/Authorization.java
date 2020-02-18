package org.litesoft.restish.support.auth;

public interface Authorization {
    void set( AuthorizePair pair );

    AuthorizePair get();

    void clear();
}

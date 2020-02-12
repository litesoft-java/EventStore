package org.litesoft.events;

import org.litesoft.restish.support.auth.AbstractApiAuthFilter;
import org.litesoft.restish.support.auth.Authorization;
import org.litesoft.restish.support.auth.AuthorizePair;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ApiAuthFilter extends AbstractApiAuthFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final Authorization mAuthorization;

    public ApiAuthFilter(Authorization pAuthorization) {
        mAuthorization = pAuthorization;
    }

    @Override
    protected void grabAuth(String auth) throws BadAuthorizationFormException {
        if (auth == null) {
            mAuthorization.set(AuthorizePair.LOCALHOST);
            return;
        }
        if (auth.startsWith(BEARER_PREFIX)) {
            String fields = auth.substring(BEARER_PREFIX.length()).trim();
            int slashAt = fields.indexOf('/');
            if (slashAt != -1) {
                String secretToken = fields.substring(0, slashAt).trim();
                String userEmail = fields.substring(slashAt + 1).trim().toLowerCase();
                if (!secretToken.isEmpty() && !userEmail.isEmpty()) {
                    mAuthorization.set(AuthorizePair.withSecretToken(secretToken).withUserEmail(userEmail).build());
                    return;
                }
            }
        }
        throw new BadAuthorizationFormException(auth);
    }

    @Override
    protected void clearAuth() {
        mAuthorization.clear();
    }
}

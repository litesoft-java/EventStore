package org.litesoft.events;

import org.litesoft.restish.support.AbstractApiAuthFilter;
import org.litesoft.restish.support.AuthorizePair;
import org.litesoft.restish.support.CurrentAuthorization;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ApiAuthFilter extends AbstractApiAuthFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void grabAuth(String auth) {
        if (auth == null) {
            CurrentAuthorization.set(AuthorizePair.LOCALHOST);
            return;
        }
        if (auth.startsWith(BEARER_PREFIX)) {
            String fields = auth.substring(BEARER_PREFIX.length()).trim();
            int slashAt = fields.indexOf('/');
            if (slashAt != -1) {
                String secretToken = fields.substring(0, slashAt).trim();
                String userEmail = fields.substring(slashAt + 1).trim().toLowerCase();
                if (!secretToken.isEmpty() && !userEmail.isEmpty()) {
                    CurrentAuthorization.set(AuthorizePair.withSecretToken(secretToken).withUserEmail(userEmail).build());
                    return;
                }
            }
        }
        throw new RuntimeException("Bad 'Authorization': '" + auth + "'");
    }

    @Override
    protected void clearAuth() {
        CurrentAuthorization.clear();
    }
}

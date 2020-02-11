package org.litesoft.restish.support;

import org.litesoft.accessors.Require;

public class AuthorizePair {
    private final String userEmail, secretToken;

    private AuthorizePair(String userEmail, String secretToken) {
        this.userEmail = userEmail;
        this.secretToken = secretToken;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public static class Builder {
        private String userEmail, secretToken;

        public Builder withUserEmail(String userEmail) {
            this.userEmail = Require.normalize(userEmail);
            return this;
        }

        public Builder withSecretToken(String secretToken) {
            this.secretToken = Require.normalize(secretToken);
            return this;
        }

        public AuthorizePair build() {
            return new AuthorizePair(
                    Require.significant("UserEmail", userEmail),
                    Require.significant("SecretToken", secretToken)
            );
        }
    }

    public static Builder withUserEmail(String userEmail) {
        return new Builder().withUserEmail(userEmail);
    }

    public static Builder withSecretToken(String secretToken) {
        return new Builder().withSecretToken(secretToken);
    }
}

package org.litesoft.restish.support;

import org.litesoft.accessors.Require;

public class AuthorizePair {
    private final boolean localHost;
    private final String userEmail, secretToken;

    public static final AuthorizePair LOCALHOST = new AuthorizePair(true, null, null);

    private AuthorizePair(boolean localHost, String userEmail, String secretToken) {
        this.localHost = localHost;
        this.userEmail = userEmail;
        this.secretToken = secretToken;
    }

    public boolean isLocalHost() {
        return localHost;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getSecretToken() {
        return secretToken;
    }

    @Override
    public String toString() {
        return isLocalHost() ? "AuthorizePair(localHost)" :
                ("AuthorizePair(userEmail='" + userEmail + ", 'secretToken='" + secretToken + "')");
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
            return new AuthorizePair(false,
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

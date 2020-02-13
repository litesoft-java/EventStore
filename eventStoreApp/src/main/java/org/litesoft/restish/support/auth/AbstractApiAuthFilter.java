package org.litesoft.restish.support.auth;

import org.litesoft.accessors.Require;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractApiAuthFilter implements javax.servlet.Filter {
    private static final String SCHEME_HTTPS = "https";
    private static final String SCHEME_HTTP = "http";

    private final String mClassName;

    protected AbstractApiAuthFilter() {
        mClassName = getClass().getSimpleName();
        System.out.println(mClassName + " created");
    }

    protected abstract void grabAuth(String auth) throws BadAuthorizationFormException;

    protected abstract void clearAuth();

    private boolean requireHTTPS(HttpServletRequest req) {
        String scheme = req.getScheme();
        if (SCHEME_HTTPS.equals(scheme)) {
            return false;
        }
        String origScheme = req.getHeader("x-forwarded-proto");
        if (SCHEME_HTTPS.equals(origScheme)) {
            return false;
        }
        if (!SCHEME_HTTP.equals(scheme)) {
            System.out.println(mClassName + ".doFilter: origScheme='" + origScheme + "', scheme='" + scheme + "'");
        }
        return true;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String path = req.getRequestURI();
        if ((path == null) || !path.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        String serverName = req.getServerName();
        boolean notLocalhost = !"localhost".equals(serverName);

        if (notLocalhost && requireHTTPS(req)) {
            replyNeedHTTPS(response);
            return;
        }

        String auth = Require.normalize(req.getHeader("Authorization"));
        if (auth == null) {
            if (notLocalhost) {
                replyNeedAuthorization(response);
                return;
            }
        }
        try {
            grabAuth(auth);
        } catch (BadAuthorizationFormException e) {
            System.out.println(e.getMessage());
            replyBadAuthorizationForm(response);
            return;
        } catch (RuntimeException e) {
            e.printStackTrace();
            replyBadAuthorizationForm(response);
            return;
        }
        try {
            chain.doFilter(request, response);
        } finally {
            clearAuth();
        }
    }

    private void replyNeedHTTPS(ServletResponse response) {
        replyError(response, 451, "https is required");
    }

    private void replyNeedAuthorization(ServletResponse response) {
        replyError(response, 401, "'Authorization' needed");
    }

    private void replyBadAuthorizationForm(ServletResponse response) {
        replyError(response, 451, "'Authorization' form unacceptable");
    }

    private void replyError(ServletResponse response, int code, String message) {
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            res.sendError(code, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    protected static class BadAuthorizationFormException extends Exception {
        public BadAuthorizationFormException(String auth) {
            super("Bad 'Authorization': '" + auth + "'");
        }
    }
}

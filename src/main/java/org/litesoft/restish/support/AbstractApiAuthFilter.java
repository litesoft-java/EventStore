package org.litesoft.restish.support;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class AbstractApiAuthFilter implements javax.servlet.Filter {
    private final String mClassName;

    public AbstractApiAuthFilter() {
        mClassName = getClass().getSimpleName();
        System.out.println(mClassName + " created");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest res = (HttpServletRequest) request;

        String zAuthType = res.getAuthType();
        String zOfInterest = res.getHeader("Fred");

        System.out.println(mClassName + ".doFilter: " + zAuthType + " -+- " + zOfInterest);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }
}

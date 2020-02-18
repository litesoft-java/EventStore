package org.litesoft.http.client.support;

import javax.servlet.http.HttpServletResponse;

public interface ResponseErrorHandler {
    void errored( HttpServletResponse pResponse, int pStatusCode, String pError );
}

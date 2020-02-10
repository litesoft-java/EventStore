package org.litesoft.exceptions;

import org.litesoft.HttpStatusAccessor;

public class RestishException extends RuntimeException implements HttpStatusAccessor {
    private final int mStatusCode;

    public RestishException(int pStatusCode, String message) {
        super(message);
        mStatusCode = pStatusCode;
    }

    @Override
    public int httpStatus() {
        return mStatusCode;
    }
}

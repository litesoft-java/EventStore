package org.litesoft.restish.support;

import org.litesoft.HttpStatusAccessor;
import org.litesoft.restish.support.auth.Authorization;
import org.litesoft.restish.support.auth.AuthorizePair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public class AbstractRestishController<T> {
    private final Authorization mAuthorization;

    public AbstractRestishController(Authorization pAuthorization) {
        mAuthorization = pAuthorization;
    }

    protected AuthorizePair authorizePair() {
        return mAuthorization.get();
    }

    protected ResponseEntity<T> process(Supplier<T> pSuppler) {
        return process(HttpStatus.OK, pSuppler);
    }

    protected ResponseEntity<T> process(HttpStatus pSuccessStatus, Supplier<T> pSuppler) {
        T returnedEvent;
        try {
            returnedEvent = pSuppler.get();
        } catch (RuntimeException e) {
            return handle(e);
        }
        return ResponseEntity.status(pSuccessStatus).body(returnedEvent);
    }

    protected static <T> ResponseEntity<T> handle(RuntimeException e) {
        if (e instanceof HttpStatusAccessor) {
            int httpStatus = ((HttpStatusAccessor) e).httpStatus();
            return forceType(new ResponseEntity<>(e.getMessage(),
                    new HttpHeaders(), HttpStatus.valueOf(httpStatus)));
        }
        throw e;

    }

    @SuppressWarnings("unchecked")
    private static <T> ResponseEntity<T> forceType(ResponseEntity<?> pResponseEntity) {
        return (ResponseEntity<T>) pResponseEntity;
    }
}

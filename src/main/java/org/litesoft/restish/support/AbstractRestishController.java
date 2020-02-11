package org.litesoft.restish.support;

import org.litesoft.HttpStatusAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

public class AbstractRestishController<T> {
    protected AuthorizePair authorizePair() {
        return null; // TODO: XXX
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

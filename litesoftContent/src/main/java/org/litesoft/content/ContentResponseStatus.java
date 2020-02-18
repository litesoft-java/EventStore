package org.litesoft.content;

public interface ContentResponseStatus<Request> {
    int STATUS_CODE_GROUP_SIZE = 100;
    int OK_STATUS_CODE = 200;
    int NOT_FOUND_STATUS_CODE = 404;
    int SERVER_ERROR_STATUS_CODE = 500;

    Request request();

    boolean hasError();

    String getError();

    int getStatusCode();
}

package org.litesoft.bean;

@SuppressWarnings("unused")
public interface ErrorAccessor extends Accessor {
    String getError();

    default boolean hasError() {
        return (getError() != null);
    }

    static ErrorAccessor deNull( ErrorAccessor it ) {
        return (it != null) ? it : () -> null;
    }
}

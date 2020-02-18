package org.litesoft.alleviative;

public class Cast {
    @SuppressWarnings("unchecked")
    public static <T> T it( Object pObject ) {
        return (T)pObject;
    }
}

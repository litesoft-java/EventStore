package org.litesoft.alleviative;

import java.util.Optional;

@SuppressWarnings({"unused", "OptionalUsedAsFieldOrParameterType"})
public class Unwrap {
    /**
     * @param o nullable
     * @return nullable
     */
    public static <T> T it( Optional<T> o ) {
        if ( (o != null) && o.isPresent() ) {
            return o.get();
        }
        return null;
    }
}
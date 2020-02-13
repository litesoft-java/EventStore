package org.litesoft.accessors;

import java.util.function.Function;

public class NamedFunction<T> {
    private final String mName;
    private final Function<T, Object> mFunction;

    public NamedFunction(String pName, Function<T, Object> pFunction) {
        if (pName == null) {
            pName = "";
        }
        mName = pName.trim();
        mFunction = pFunction;
        if (mName.isEmpty()) {
            throw new IllegalArgumentException("Name required");
        }
    }

    public String getName() {
        return mName;
    }

    public Function<T, Object> getFunction() {
        return mFunction;
    }

    @Override
    public String toString() {
        return "NamedFunction('" + mName + "', " +
                "..." +
                ")";
    }
}

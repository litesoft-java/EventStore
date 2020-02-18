package org.litesoft.alleviative.functionalinterfaces;

import java.util.function.Supplier;

public class ConstantStringSupplier implements Supplier<String> {
    private final String mValue;

    public ConstantStringSupplier( String pValue ) {
        mValue = pValue;
    }

    @Override
    public String get() {
        return mValue;
    }
}

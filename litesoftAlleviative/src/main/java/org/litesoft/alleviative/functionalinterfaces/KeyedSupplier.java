package org.litesoft.alleviative.functionalinterfaces;

public interface KeyedSupplier<Key, Value> {
    Value get( Key pKey );
}

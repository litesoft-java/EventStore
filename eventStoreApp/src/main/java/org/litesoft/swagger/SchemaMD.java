package org.litesoft.swagger;

import org.litesoft.accessors.NamedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SchemaMD<T extends Schema<T>> {

    private volatile List<NamedFunction<T>> mEqualsSuppliers;
    private volatile List<NamedFunction<T>> mHashCodeSuppliers;
    private volatile List<NamedFunction<T>> mRequiredSuppliers;
    private volatile List<NamedFunction<T>> mToStringSuppliers;

    public final List<NamedFunction<T>> getEqualsSuppliers() {
        List<NamedFunction<T>> zList = mEqualsSuppliers;
        if ( zList == null ) {
            zList = new ArrayList<>();
            collectEqualsSuppliers( zList );
            mEqualsSuppliers = zList;
        }
        return zList;
    }

    public final List<NamedFunction<T>> getHashCodeSuppliers() {
        List<NamedFunction<T>> zList = mHashCodeSuppliers;
        if ( zList == null ) {
            zList = new ArrayList<>();
            collectHashCodeSuppliers( zList );
            mHashCodeSuppliers = zList;
        }
        return zList;
    }

    public final List<NamedFunction<T>> getRequiredSuppliers() {
        List<NamedFunction<T>> zList = mRequiredSuppliers;
        if ( zList == null ) {
            zList = new ArrayList<>();
            collectRequiredSuppliers( zList );
            mRequiredSuppliers = zList;
        }
        return zList;
    }

    public final List<NamedFunction<T>> getToStringSuppliers() {
        List<NamedFunction<T>> zList = mToStringSuppliers;
        if ( zList == null ) {
            zList = new ArrayList<>();
            collectToStringSuppliers( zList );
            mToStringSuppliers = zList;
        }
        return zList;
    }

    protected void collectEqualsSuppliers( List<NamedFunction<T>> pCollector ) {
    }

    protected void collectHashCodeSuppliers( List<NamedFunction<T>> pCollector ) {
    }

    protected void collectRequiredSuppliers( List<NamedFunction<T>> pCollector ) {
    }

    protected void collectToStringSuppliers( List<NamedFunction<T>> pCollector ) {
    }

    protected NamedFunction<T> nf( String pName, Function<T, Object> pFunction ) {
        return new NamedFunction<>( pName, pFunction );
    }
}

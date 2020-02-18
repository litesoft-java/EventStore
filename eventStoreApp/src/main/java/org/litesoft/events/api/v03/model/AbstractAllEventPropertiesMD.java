package org.litesoft.events.api.v03.model;

import org.litesoft.accessors.NamedFunction;

import java.util.Collections;
import java.util.List;

public class AbstractAllEventPropertiesMD<T extends AbstractAllEventProperties<T>> extends AbstractCommonEventPropertiesMD<T> {

    private final NamedFunction<T> nf_id = nf( "id", AbstractAllEventProperties::getId );
    private final NamedFunction<T> nf_updateToken = nf( "updateToken", AbstractAllEventProperties::getUpdateToken );

    public final NamedFunction<T> nfId() {
        return nf_id;
    }

    public final NamedFunction<T> nfUpdateToken() {
        return nf_updateToken;
    }

    @Override
    protected void collectEqualsSuppliers( List<NamedFunction<T>> pCollector ) {
        Collections.addAll( pCollector
                , nfId()
        );

        super.collectEqualsSuppliers( pCollector );
    }

    @Override
    protected void collectHashCodeSuppliers( List<NamedFunction<T>> pCollector ) {
        Collections.addAll( pCollector
                , nfId()
        );
    }

    @Override
    protected void collectToStringSuppliers( List<NamedFunction<T>> pCollector ) {
        Collections.addAll( pCollector
                , nfId()
                , nfUpdateToken()
        );

        super.collectToStringSuppliers( pCollector );
    }
}

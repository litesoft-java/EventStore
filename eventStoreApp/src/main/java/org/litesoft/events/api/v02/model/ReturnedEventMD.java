package org.litesoft.events.api.v02.model;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.swagger.SchemaMD;

import java.util.Collections;
import java.util.List;

public class ReturnedEventMD extends AbstractAllEventPropertiesMD<ReturnedEvent> {
    public static final SchemaMD<ReturnedEvent> INSTANCE = new ReturnedEventMD();

    private ReturnedEventMD() {
    }

    @Override
    protected void collectRequiredSuppliers( List<NamedFunction<ReturnedEvent>> pCollector ) {
        Collections.addAll( pCollector
                , getId()
                , getUser()
                , getWhat()
                , getWhen()
        );

        super.collectRequiredSuppliers( pCollector );
    }
}

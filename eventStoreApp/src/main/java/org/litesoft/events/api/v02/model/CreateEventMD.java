package org.litesoft.events.api.v02.model;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.swagger.SchemaMD;

import java.util.Collections;
import java.util.List;

public class CreateEventMD extends AbstractCommonEventPropertiesMD<CreateEvent> {
    public static final SchemaMD<CreateEvent> INSTANCE = new CreateEventMD();

    private CreateEventMD() {
    }

    @Override
    protected void collectRequiredSuppliers( List<NamedFunction<CreateEvent>> pCollector ) {
        Collections.addAll( pCollector
                , getUser()
                , getWhat()
        );

        super.collectRequiredSuppliers( pCollector );
    }
}

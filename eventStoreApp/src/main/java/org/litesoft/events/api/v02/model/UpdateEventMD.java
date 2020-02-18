package org.litesoft.events.api.v02.model;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.swagger.SchemaMD;

import java.util.Collections;
import java.util.List;

public class UpdateEventMD extends AbstractAllEventPropertiesMD<UpdateEvent> {
    public static final SchemaMD<UpdateEvent> INSTANCE = new UpdateEventMD();

    private UpdateEventMD() {
    }

    @Override
    protected void collectRequiredSuppliers( List<NamedFunction<UpdateEvent>> pCollector ) {
        Collections.addAll( pCollector
                , getId()
                , getUser()
                , getWhat()
                , getWhen()
        );

        super.collectRequiredSuppliers( pCollector );
    }
}

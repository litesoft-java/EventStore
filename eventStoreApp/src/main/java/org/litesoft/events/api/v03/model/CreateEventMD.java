package org.litesoft.events.api.v03.model;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.swagger.SchemaMD;

import java.util.Collections;
import java.util.List;

public class CreateEventMD extends AbstractCommonEventPropertiesMD<CreateEvent> {
    public static final CreateEventMD INSTANCE = new CreateEventMD();

    private CreateEventMD() {
    }

    @Override
    protected void collectRequiredSuppliers( List<NamedFunction<CreateEvent>> pCollector ) {
        Collections.addAll( pCollector
                , nfUser()
                , nfWhat()
        );

        super.collectRequiredSuppliers( pCollector );
    }
}

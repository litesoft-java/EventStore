package org.litesoft.events.api.v03.model;

import org.litesoft.accessors.NamedFunction;

import java.util.Collections;
import java.util.List;

public class AbstractAllEventPropertiesMD<T extends AbstractAllEventProperties<T>> extends AbstractCommonEventPropertiesMD<T> {

    private final NamedFunction<T> nf_id = nf("id", AbstractAllEventProperties::getId);
    private final NamedFunction<T> nf_updateToken = nf("updateToken", AbstractAllEventProperties::getUpdateToken);

    protected final NamedFunction<T> getId() {
        return nf_id;
    }

    protected final NamedFunction<T> getUpdateToken() {
        return nf_updateToken;
    }

    @Override
    protected void collectEqualsSuppliers(List<NamedFunction<T>> pCollector) {
        Collections.addAll(pCollector
                , getId()
        );

        super.collectEqualsSuppliers(pCollector);
    }

    @Override
    protected void collectHashCodeSuppliers(List<NamedFunction<T>> pCollector) {
        Collections.addAll(pCollector
                , getId()
        );
    }

    @Override
    protected void collectToStringSuppliers(List<NamedFunction<T>> pCollector) {
        Collections.addAll(pCollector
                , getId()
                , getUpdateToken()
        );

        super.collectToStringSuppliers(pCollector);
    }
}

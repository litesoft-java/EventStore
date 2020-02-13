package org.litesoft.events.api.v03.model;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.swagger.SchemaMD;

import java.util.Collections;
import java.util.List;

public class AbstractCommonEventPropertiesMD<T extends AbstractCommonEventProperties<T>> extends SchemaMD<T> {

    private final NamedFunction<T> nf_user = nf("user", AbstractCommonEventProperties::getUser);
    private final NamedFunction<T> nf_what = nf("what", AbstractCommonEventProperties::getWhat);
    private final NamedFunction<T> nf_when = nf("when", AbstractCommonEventProperties::getWhen);
    private final NamedFunction<T> nf_localTimeOffset = nf("localTimeOffset", AbstractCommonEventProperties::getLocalTimeOffset);
    private final NamedFunction<T> nf_localTzName = nf("localTzName", AbstractCommonEventProperties::getLocalTzName);
    private final NamedFunction<T> nf_where = nf("where", AbstractCommonEventProperties::getWhere);
    private final NamedFunction<T> nf_billable = nf("billable", AbstractCommonEventProperties::isBillable);
    private final NamedFunction<T> nf_client = nf("client", AbstractCommonEventProperties::getClient);
    private final NamedFunction<T> nf_done = nf("done", AbstractCommonEventProperties::isDone);

    protected final NamedFunction<T> getUser() {
        return nf_user;
    }

    protected final NamedFunction<T> getWhat() {
        return nf_what;
    }

    protected final NamedFunction<T> getWhen() {
        return nf_when;
    }

    protected final NamedFunction<T> getLocalTimeOffset() {
        return nf_localTimeOffset;
    }

    protected final NamedFunction<T> getLocalTzName() {
        return nf_localTzName;
    }

    protected final NamedFunction<T> getWhere() {
        return nf_where;
    }

    protected final NamedFunction<T> getBillable() {
        return nf_billable;
    }

    protected final NamedFunction<T> getClient() {
        return nf_client;
    }

    protected final NamedFunction<T> getDone() {
        return nf_done;
    }

    @Override
    protected void collectEqualsSuppliers(List<NamedFunction<T>> pCollector) {
        Collections.addAll(pCollector
                , getUser()
                , getWhat()
                , getWhen()
                , getLocalTimeOffset()
                , getLocalTzName()
                , getWhere()
                , getBillable()
                , getClient()
                , getDone()
        );

        super.collectRequiredSuppliers(pCollector);
    }

    @Override
    protected void collectHashCodeSuppliers(List<NamedFunction<T>> pCollector) {
        Collections.addAll(pCollector
                , getUser()
        );
    }

    @Override
    protected void collectToStringSuppliers(List<NamedFunction<T>> pCollector) {
        Collections.addAll(pCollector
                , getUser()
                , getWhat()
                , getWhen()
                , getLocalTimeOffset()
                , getLocalTzName()
                , getWhere()
                , getBillable()
                , getClient()
                , getDone()
        );

        super.collectRequiredSuppliers(pCollector);
    }
}

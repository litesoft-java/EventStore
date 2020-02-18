package org.litesoft.events.api.v03.model;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.swagger.SchemaMD;

import java.util.Collections;
import java.util.List;

public class AbstractCommonEventPropertiesMD<T extends AbstractCommonEventProperties<T>> extends SchemaMD<T> {

    private final NamedFunction<T> nf_user = nf( "user", AbstractCommonEventProperties::getUser );
    private final NamedFunction<T> nf_what = nf( "what", AbstractCommonEventProperties::getWhat );
    private final NamedFunction<T> nf_when = nf( "when", AbstractCommonEventProperties::getWhen );
    private final NamedFunction<T> nf_localTimeOffset = nf( "localTimeOffset", AbstractCommonEventProperties::getLocalTimeOffset );
    private final NamedFunction<T> nf_localTzName = nf( "localTzName", AbstractCommonEventProperties::getLocalTzName );
    private final NamedFunction<T> nf_where = nf( "where", AbstractCommonEventProperties::getWhere );
    private final NamedFunction<T> nf_billable = nf( "billable", AbstractCommonEventProperties::getBillable );
    private final NamedFunction<T> nf_client = nf( "client", AbstractCommonEventProperties::getClient );
    private final NamedFunction<T> nf_done = nf( "done", AbstractCommonEventProperties::getDone );

    public final NamedFunction<T> nfUser() {
        return nf_user;
    }

    public final NamedFunction<T> nfWhat() {
        return nf_what;
    }

    public final NamedFunction<T> nfWhen() {
        return nf_when;
    }

    public final NamedFunction<T> nfLocalTimeOffset() {
        return nf_localTimeOffset;
    }

    public final NamedFunction<T> nfLocalTzName() {
        return nf_localTzName;
    }

    public final NamedFunction<T> nfWhere() {
        return nf_where;
    }

    public final NamedFunction<T> nfBillable() {
        return nf_billable;
    }

    public final NamedFunction<T> nfClient() {
        return nf_client;
    }

    public final NamedFunction<T> nfDone() {
        return nf_done;
    }

    @Override
    protected void collectEqualsSuppliers( List<NamedFunction<T>> pCollector ) {
        Collections.addAll( pCollector
                , nfUser()
                , nfWhat()
                , nfWhen()
                , nfLocalTimeOffset()
                , nfLocalTzName()
                , nfWhere()
                , nfBillable()
                , nfClient()
                , nfDone()
        );

        super.collectRequiredSuppliers( pCollector );
    }

    @Override
    protected void collectHashCodeSuppliers( List<NamedFunction<T>> pCollector ) {
        Collections.addAll( pCollector
                , nfUser()
        );
    }

    @Override
    protected void collectToStringSuppliers( List<NamedFunction<T>> pCollector ) {
        Collections.addAll( pCollector
                , nfUser()
                , nfWhat()
                , nfWhen()
                , nfLocalTimeOffset()
                , nfLocalTzName()
                , nfWhere()
                , nfBillable()
                , nfClient()
                , nfDone()
        );

        super.collectRequiredSuppliers( pCollector );
    }
}

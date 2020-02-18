package org.litesoft.events.api.v03.model;

import org.litesoft.accessors.NamedFunction;
import org.litesoft.swagger.SchemaMD;

import java.util.Collections;
import java.util.List;

public class PageEventsMD extends SchemaMD<PageEvents> {
    private final NamedFunction<PageEvents> nf_nextToken = nf( "nextToken", PageEvents::getNextToken );
    private final NamedFunction<PageEvents> nf_data = nf( "data", PageEvents::getData );

    public static final SchemaMD<PageEvents> INSTANCE = new PageEventsMD();

    private PageEventsMD() {
    }

    public NamedFunction<PageEvents> getNextToken() {
        return nf_nextToken;
    }

    public NamedFunction<PageEvents> getData() {
        return nf_data;
    }

    @Override
    protected void collectEqualsSuppliers( List<NamedFunction<PageEvents>> pCollector ) {
        Collections.addAll( pCollector
                , getNextToken()
                , getData()
        );

        super.collectRequiredSuppliers( pCollector );
    }

    @Override
    protected void collectHashCodeSuppliers( List<NamedFunction<PageEvents>> pCollector ) {
        Collections.addAll( pCollector
                , getNextToken()
                , getData()
        );
    }

    @Override
    protected void collectToStringSuppliers( List<NamedFunction<PageEvents>> pCollector ) {
        Collections.addAll( pCollector
                , getNextToken()
                , getData()
        );

        super.collectRequiredSuppliers( pCollector );
    }
}

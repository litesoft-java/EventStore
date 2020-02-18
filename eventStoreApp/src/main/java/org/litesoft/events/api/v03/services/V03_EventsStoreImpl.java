package org.litesoft.events.api.v03.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.litesoft.alleviative.validation.NotNull;
import org.litesoft.alleviative.validation.Significant;
import org.litesoft.events.api.v03.model.CreateEvent;
import org.litesoft.events.api.v03.model.PatchEvent;
import org.litesoft.events.api.v03.model.ReturnedEvent;
import org.litesoft.events.services.AbstractEventStore;
import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.events.services.persistence.repos.EventLogRepository;
import org.litesoft.persisted.NextPageToken;
import org.litesoft.persisted.Page;
import org.litesoft.restish.support.PageData;
import org.litesoft.restish.support.auth.AuthorizePair;
import org.springframework.stereotype.Service;

@SuppressWarnings("DuplicatedCode")
@Service
public class V03_EventsStoreImpl extends AbstractEventStore implements V03_EventsStore {

    private static final String[] PO_SUPPORTED_FIELDS = {
            "User",
            "What",
            "When",
            "Where",
            "Done",
            "LocalTimeOffset",
            "LocalTzName",
            "Billable",
            "Client",
            };
    private static final String[] API_UNSUPPORTED_FIELDS = {};

    public V03_EventsStoreImpl( EventLogRepository pRepository ) {
        super( "v03", pRepository, EventLogPO.META_DATA, PO_SUPPORTED_FIELDS, API_UNSUPPORTED_FIELDS );
    }

    @Override
    public PageData<ReturnedEvent> latestEvents( AuthorizePair pAuthorizePair, String pUser, int pLimit ) {
        Page<EventLogPO> zPage = firstPage( pAuthorizePair, pUser, pLimit );
        return map( zPage );
    }

    @Override
    public PageData<ReturnedEvent> nextEvents( AuthorizePair pAuthorizePair, String pNextToken, Integer pLimit_inheritIfNull ) {
        Page<EventLogPO> zPage = nextPage( pAuthorizePair, pNextToken, pLimit_inheritIfNull );
        return map( zPage );
    }

    private PageData<ReturnedEvent> map( Page<EventLogPO> zPage ) {
        List<EventLogPO> zPOs = NotNull.or( zPage.getPOs(), Collections.emptyList() );
        List<ReturnedEvent> zData = zPOs.stream().map( this::mapPO2RE ).collect( Collectors.toList() );

        NextPageToken zNextPageToken = zPage.getNextPageToken();
        String zToken = (zNextPageToken == null) ? null : Significant.orNull( zNextPageToken.getEncodedToken() );
        return new PageData<>( zData, zToken );
    }

    @Override
    public ReturnedEvent createEvent( AuthorizePair pAuthorizePair, CreateEvent pEvent ) {
        if ( pEvent == null ) {
            return null;
        }

        EventLogPO zPO = EventLogPO.builder()
                .withUser( pEvent.getUser() )
                .withWhat( pEvent.getWhat() )
                .withWhen( pEvent.getWhen() )
                .withWhere( pEvent.getWhere() )
                .withDone( pEvent.getDone() )
                .withLocalTimeOffset( pEvent.getLocalTimeOffset() )
                .withLocalTzName( pEvent.getLocalTzName() )
                .withBillable( pEvent.getBillable() )
                .withClient( pEvent.getClient() )
                .build();

        EventLogPO zInsertedPO = mRepository.insert( zPO );

        return mapPO2RE( zInsertedPO );
    }

    @Override
    public ReturnedEvent deleteEvent( AuthorizePair pAuthorizePair, String pUpdateToken ) {
        EventLogPO zPO = getForChangeByUpdateToken( pUpdateToken );
        if ( zPO != null ) {
            mRepository.delete( zPO );
        }
        return mapPO2RE( zPO );
    }

    @Override
    public ReturnedEvent readEvent( AuthorizePair pAuthorizePair, String pID ) {
        return mapPO2RE( mRepository.findById( Significant.orNull( pID ) ) );
    }

    @Override
    public ReturnedEvent updateEvent( AuthorizePair pAuthorizePair, String pUpdateToken, PatchEvent pEvent ) {
        if ( pEvent == null ) {
            return null;
        }
        EventLogPO zPO = getForChangeByUpdateToken( pUpdateToken );
        if ( zPO == null ) {
            return null;
        }
        String zUser = requiredSignificantField( "User", pEvent.getUser() );
        String zWhat = requiredSignificantField( "What", pEvent.getWhat() );
        String zWhen = requiredSignificantField( "When", pEvent.getWhen() );
        String zWhere = Significant.orNull( pEvent.getWhere() );
        Boolean zDone = pEvent.getDone();
        Integer zLocalTimeOffset = pEvent.getLocalTimeOffset();
        String zLocalTzName = Significant.orNull( pEvent.getLocalTzName() );
        Boolean zBillable = pEvent.getBillable();
        String zClient = Significant.orNull( pEvent.getClient() );

        EventLogPO.Builder zBuilder = checkUpdateApiBasedChanges( pEvent.getId(),
                                                                  "User", zUser,
                                                                  "What", zWhat,
                                                                  "When", zWhen,
                                                                  "Where", zWhere,
                                                                  "Done", zDone,
                                                                  "LocalTimeOffset", zLocalTimeOffset,
                                                                  "LocalTzName", zLocalTzName,
                                                                  "Billable", zBillable,
                                                                  "Client", zClient );
        EventLogPO zUpdated = update( zBuilder );
        return mapPO2RE( zUpdated );
    }

    private ReturnedEvent mapPO2RE( EventLogPO pPO ) {
        return (pPO == null) ? null :
               new ReturnedEvent()
                       .withId( pPO.getId() )
                       .withUser( pPO.getUser() )
                       .withWhat( pPO.getWhat() )
                       .withWhen( pPO.getWhen() )
                       .withWhere( pPO.getWhere() )
                       .withDone( pPO.getDone() )
                       .withLocalTimeOffset( pPO.getLocalTimeOffset() )
                       .withLocalTzName( pPO.getLocalTzName() )
                       .withBillable( pPO.getBillable() )
                       .withClient( pPO.getClient() )
                       .withUpdateToken( encodeUpdateToken( pPO ) )
                ;
    }
}

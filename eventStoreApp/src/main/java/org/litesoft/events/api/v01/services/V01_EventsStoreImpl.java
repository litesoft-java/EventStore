package org.litesoft.events.api.v01.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.litesoft.alleviative.validation.NotNull;
import org.litesoft.alleviative.validation.Significant;
import org.litesoft.events.api.v01.model.CreateEvent;
import org.litesoft.events.api.v01.model.ReturnedEvent;
import org.litesoft.events.api.v01.model.UpdateEvent;
import org.litesoft.events.services.AbstractEventStore;
import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.events.services.persistence.repos.EventLogRepository;
import org.litesoft.persisted.Page;
import org.litesoft.restish.support.PageData;
import org.litesoft.restish.support.auth.AuthorizePair;
import org.springframework.stereotype.Service;

@SuppressWarnings("DuplicatedCode")
@Service
public class V01_EventsStoreImpl extends AbstractEventStore implements V01_EventsStore {
    private static final String[] PO_SUPPORTED_FIELDS = {
            "User",
            "What",
            "When",
            "Where",
            };
    private static final String[] API_UNSUPPORTED_FIELDS = {
            "LocalTimeOffset",
            "LocalTzName",
            "Billable",
            "Client",
            "Done",
            };

    public V01_EventsStoreImpl( EventLogRepository pRepository ) {
        super( "v01", pRepository, EventLogPO.META_DATA, PO_SUPPORTED_FIELDS, API_UNSUPPORTED_FIELDS );
    }

    @Override
    public PageData<ReturnedEvent> latestEvents( AuthorizePair pAuthorizePair, String pUser, int pLimit ) {
        Page<EventLogPO> zEventsPage = firstPage( pAuthorizePair, pUser, pLimit );

        List<EventLogPO> zPOs = NotNull.or( zEventsPage.getPOs(), Collections.emptyList() );
        List<ReturnedEvent> zData = zPOs.stream().map( this::mapPO2RE ).collect( Collectors.toList() );

        return new PageData<>( zData );
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
                .build();

        EventLogPO zInsertedPO = mRepository.insert( zPO );

        return mapPO2RE( zInsertedPO );
    }

    @Override
    public ReturnedEvent deleteEvent( AuthorizePair pAuthorizePair, String pID ) {
        EventLogPO zPO = mRepository.findById( Significant.orNull( pID ) );
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
    public ReturnedEvent updateEvent( AuthorizePair pAuthorizePair, UpdateEvent pEvent ) {
        if ( pEvent == null ) {
            return null;
        }
        String zUser = requiredSignificantField( "User", pEvent.getUser() );
        String zWhat = requiredSignificantField( "What", pEvent.getWhat() );
        String zWhen = requiredSignificantField( "When", pEvent.getWhen() );
        String zWhere = Significant.orNull( pEvent.getWhere() );

        EventLogPO.Builder zBuilder = checkUpdateApiBasedChanges( pEvent.getId(),
                                                                  "User", zUser,
                                                                  "What", zWhat,
                                                                  "When", zWhen,
                                                                  "Where", zWhere );
        EventLogPO zUpdated = update( zBuilder );
        return mapPO2RE( zUpdated );
    }

    private ReturnedEvent mapPO2RE( EventLogPO pPO ) {
        return (pPO == null) ? null :
               new ReturnedEvent()
                       .id( pPO.getId() )
                       .user( pPO.getUser() )
                       .what( pPO.getWhat() )
                       .when( pPO.getWhen() )
                       .where( pPO.getWhere() )
                ;
    }
}

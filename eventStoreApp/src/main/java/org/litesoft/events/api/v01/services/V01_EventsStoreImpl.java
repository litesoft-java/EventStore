package org.litesoft.events.api.v01.services;

import java.util.ArrayList;
import java.util.List;

import org.litesoft.alleviative.validation.Significant;
import org.litesoft.events.api.v01.model.CreateEvent;
import org.litesoft.events.api.v01.model.ReturnedEvent;
import org.litesoft.events.api.v01.model.UpdateEvent;
import org.litesoft.events.services.persistence.persisted.EventLogPO;
import org.litesoft.events.services.persistence.repos.EventLogRepository;
import org.litesoft.persisted.Page;
import org.litesoft.restish.support.PageData;
import org.litesoft.restish.support.auth.AuthorizePair;
import org.springframework.stereotype.Service;

@Service
public class V01_EventsStoreImpl implements V01_EventsStore {

    private final EventLogRepository mRepository;

    public V01_EventsStoreImpl( EventLogRepository pRepository ) {
        mRepository = pRepository;
    }

    @Override
    public PageData<ReturnedEvent> latestEvents(AuthorizePair pAuthorizePair, String pUser, int pLimit) {
        System.out.println("V01_EventsStoreImpl.latestEvents: " + pAuthorizePair);
        pUser = Significant.orNull( pUser );

        Page<EventLogPO> zEventsPage = (pUser == null) ?
                                           mRepository.firstPageAllUsers( pLimit ) :
                                           mRepository.firstPageByUser( pUser, pLimit );
        List<EventLogPO> zPOs = zEventsPage.getPOs();
        List<ReturnedEvent> zData = new ArrayList<>( zPOs.size() );

        return new PageData<>(zData);
    }

    @Override
    public ReturnedEvent createEvent(AuthorizePair pAuthorizePair, CreateEvent pEvent) {
        return null;
    }

    @Override
    public ReturnedEvent deleteEvent(AuthorizePair pAuthorizePair, String pID) {
        return null;
    }

    @Override
    public ReturnedEvent readEvent(AuthorizePair pAuthorizePair, String pID) {
        return null;
    }

    @Override
    public ReturnedEvent updateEvent(AuthorizePair pAuthorizePair, UpdateEvent pEvent) {
        return null;
    }
}

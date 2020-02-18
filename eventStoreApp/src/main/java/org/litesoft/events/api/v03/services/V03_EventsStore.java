package org.litesoft.events.api.v03.services;

import org.litesoft.events.api.v03.model.CreateEvent;
import org.litesoft.events.api.v03.model.PatchEvent;
import org.litesoft.events.api.v03.model.ReturnedEvent;
import org.litesoft.restish.support.PageData;
import org.litesoft.restish.support.auth.AuthorizePair;

public interface V03_EventsStore {
    PageData<ReturnedEvent> latestEvents( AuthorizePair pAuthorizePair, String pUser, int pLimit );

    PageData<ReturnedEvent> nextEvents( AuthorizePair pAuthorizePair, String pNextToken, Integer pLimit_inheritIfNull );

    ReturnedEvent createEvent( AuthorizePair pAuthorizePair, CreateEvent pEvent );

    ReturnedEvent deleteEvent( AuthorizePair pAuthorizePair, String updateToken );

    ReturnedEvent readEvent( AuthorizePair pAuthorizePair, String pID );

    ReturnedEvent updateEvent( AuthorizePair pAuthorizePair, String updateToken, PatchEvent pEvent );
}

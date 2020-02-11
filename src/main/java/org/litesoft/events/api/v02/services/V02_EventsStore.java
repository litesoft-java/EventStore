package org.litesoft.events.api.v02.services;

import org.litesoft.events.api.v02.model.CreateEvent;
import org.litesoft.events.api.v02.model.ReturnedEvent;
import org.litesoft.events.api.v02.model.UpdateEvent;
import org.litesoft.restish.support.AuthorizePair;
import org.litesoft.restish.support.PageData;

public interface V02_EventsStore {
    PageData<ReturnedEvent> latestEvents(AuthorizePair pAuthorizePair, String pUser, int pLimit);

    PageData<ReturnedEvent> nextEvents(AuthorizePair pAuthorizePair, String pNextToken, Integer pLimit_inheritIfNull);

    ReturnedEvent createEvent(AuthorizePair pAuthorizePair, CreateEvent pEvent);

    ReturnedEvent deleteEvent(AuthorizePair pAuthorizePair, String pID);

    ReturnedEvent readEvent(AuthorizePair pAuthorizePair, String pID);

    ReturnedEvent updateEvent(AuthorizePair pAuthorizePair, UpdateEvent pEvent);
}

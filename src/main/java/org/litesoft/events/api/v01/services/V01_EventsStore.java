package org.litesoft.events.api.v01.services;

import org.litesoft.events.api.v01.model.CreateEvent;
import org.litesoft.events.api.v01.model.ReturnedEvent;
import org.litesoft.events.api.v01.model.UpdateEvent;
import org.litesoft.restish.support.AuthorizePair;
import org.litesoft.restish.support.PageData;

public interface V01_EventsStore {
    PageData<ReturnedEvent> latestEvents(AuthorizePair pAuthorizePair, String pUser, int pLimit);

    ReturnedEvent createEvent(AuthorizePair pAuthorizePair, CreateEvent pEvent);

    ReturnedEvent deleteEvent(AuthorizePair pAuthorizePair, String pID);

    ReturnedEvent readEvent(AuthorizePair pAuthorizePair, String pID);

    ReturnedEvent updateEvent(AuthorizePair pAuthorizePair, UpdateEvent pEvent);
}

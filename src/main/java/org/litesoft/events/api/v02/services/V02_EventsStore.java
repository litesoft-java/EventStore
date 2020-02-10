package org.litesoft.events.api.v02.services;

import org.litesoft.events.api.v02.model.CreateEvent;
import org.litesoft.events.api.v02.model.ReturnedEvent;
import org.litesoft.events.api.v02.model.UpdateEvent;
import org.litesoft.restish.support.PageData;

public interface V02_EventsStore {
    PageData<ReturnedEvent> latestEvents(String pUser, int pLimit);

    PageData<ReturnedEvent> nextEvents(String pNextToken, int pLimit);

    ReturnedEvent createEvent(CreateEvent pEvent);

    ReturnedEvent deleteEvent(String pID);

    ReturnedEvent readEvent(String pID);

    ReturnedEvent updateEvent(UpdateEvent pEvent);
}
